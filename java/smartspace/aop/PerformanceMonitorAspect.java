package smartspace.aop;

import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class PerformanceMonitorAspect {
	private Log logger = LogFactory.getLog(PerformanceMonitorAspect.class);

	@Before("@annotation(smartspace.aop.PerformanceMonitor)")
	public void measureBuginTime(JoinPoint jp) {
		if (logger.isTraceEnabled()) {
			String methodName = jp.getSignature().getName();
			String className = jp.getTarget().getClass().getName();
			String args = 
			Stream.of(jp.getArgs())
				.map(obj->obj.getClass().getSimpleName())
				.collect(
						()->new StringBuffer(), 
						(buf,par)->buf.append(par).append(", "), 
						(str1, str2)->str1.append(str2))
				.toString();
			if(args.length() > 1) {
				args = args.substring(0, args.length() - 2);
			}
			
			logger.trace("**********" + className + "." + methodName + "(" + args + ") - ");
		}
	}
	
	@Around("@annotation(smartspace.aop.PerformanceMonitor) && args(id,..)")
	public Object measureBuginTime(ProceedingJoinPoint pjp, Long id) throws Throwable{
		String methodName = pjp.getSignature().getName();
		String className = pjp.getTarget().getClass().getName();

		// before....
		long begineTime = System.currentTimeMillis();
		boolean success = false;
		try {
			Object rv = pjp.proceed();
			success = true;
			return rv;
		} catch (Throwable e) {
			throw e;
		}finally {
			// after...
			long endTime = System.currentTimeMillis();
			long elapsed = endTime - begineTime;
			logger.debug("******** finished " + ((success)?"successfully":"with errors") + " elapesed " + className + "." + methodName + "(" + id +",..) - " + elapsed + "ms");
			
		}
	}
}
