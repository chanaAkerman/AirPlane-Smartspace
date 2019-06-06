package smartspace.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import smartspace.data.ActionEntity;
import smartspace.init.Demo;
import smartspace.init.Dummy;
import smartspace.layout.ActionBoundary;

public class ActionReflection {
	public void queryObject (Object object)  {
		try {
			Class<?> theObjectClass = object.getClass();
			String className = theObjectClass.getName();
			System.out.println(className);
			
			Component annotation = theObjectClass.getAnnotation(Component.class);
			if (annotation != null) {
				System.out.println("is @Component");
			}
			
			RestController anotherAnnotation = theObjectClass.getAnnotation(RestController.class);
			if (anotherAnnotation != null) {
				System.out.println("is @RestController");
				Method[] controllersMethods = theObjectClass.getMethods();
				Stream.of(controllersMethods)
					.filter(m->m.getAnnotation(RequestMapping.class) != null)
					.peek(m->System.out.print("\t" + m.getName() + "("))
					.peek(m->System.out.print(printParams(m.getParameterTypes())))
					.peek(m->System.out.println(")"))
					.map(m->m.getAnnotation(RequestMapping.class))
					.peek(reqMap->System.out.println("\t\tMapped to: " + Arrays.toString(reqMap.path())))
					.peek(reqMap->System.out.println("\t\t\twith HTTP Methods: " + Arrays.toString(reqMap.method())))
					.peek(reqMap->System.out.println("\t\t\tconsumes input in the following formats: " + Arrays.toString(reqMap.consumes())))
					.forEach(reqMap->System.out.println("\t\t\tproducing output in the following formats: " + Arrays.toString(reqMap.produces())));
			}
			
			if (annotation == null && anotherAnnotation == null){
				System.out.println("could not find any significant class annotation");
			}
			
			Constructor<?>[] constructors = theObjectClass.getConstructors();
			for (Constructor<?> c : constructors) {
				Parameter[] params = c.getParameters();
				if (params.length == 0) {
					System.out.println("\thas an empty constructor!");
//					Object newInstance = c.newInstance();
//					System.out.println("\tnew instance output: " + newInstance);
					
					Class<?> aClassFromName = Class.forName(className);
					Object newInstance = aClassFromName.getConstructor().newInstance();
					System.out.println("\tnew instance output: " + newInstance);
				}
				
				if (params.length == 1 && params[0].getType().equals(String.class)) {
					Object newInstance = c.newInstance("8");
					System.out.println("\tnew instanace created with string argument: " + newInstance);
					
				}
			}
			
			System.out.println();
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private String output;
	private String printParams(Class<?>[] parameterTypes) {
		output = "";
		Stream.of(parameterTypes)
			.map(cls->cls.getSimpleName())
			.peek(name->output += name)
			.forEach(name->output += ",");
		if (output.length() > 0) {
			return output.substring(0, output.length() - 1);
		}else {
			return output;
		}
	}

	public static void main(String[] args) {
		ActionReflection demo = new ActionReflection();
		Stream.of(
				"hello",
				new Long(12),
				new ActionEntity(),
				new ActionBoundary(),
				new Dummy(new Demo()),
				new Demo(),
				new ActionRepository(),
				new ActionDummyController())
			.forEach(demo::queryObject);
			
	}
}
