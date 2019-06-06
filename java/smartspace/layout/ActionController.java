 package smartspace.layout;
 
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import smartspace.aop.PerformanceMonitor;
import smartspace.logic.ActionNotFoundException;
import smartspace.logic.ActionService;

@RestController
public class ActionController {
	private ActionService actions;
	
	@Autowired
	public ActionController(ActionService actions) {
		super();
		this.actions=actions;
	}
	public ActionService getActions() {
		return actions;
	}
	public void setActions(ActionService actions) {
		this.actions = actions;
	}
	@RequestMapping(
			method=RequestMethod.DELETE,
			path="/smartspace/actions/{key}")
	public void deleteSepcificAction (
			@PathVariable("key") String key) {
		try {
		this.actions
			.deleteOneActionByKey(key);
		}catch(Exception e) {
			//couldn't find the element => the element does not exist in Database
			System.err.println("Element: "+key+" does not exist");
		}
	}
	@PerformanceMonitor
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/actions",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
		public ActionBoundary insert(
				@RequestBody ActionBoundary action) {
//			ActionEntity input = action.toEntity();
//			
//			ActionEntity output = this.actions.insert(input);
//			
//			return new ActionBoundary(output);
			return new ActionBoundary(
					this.actions
						.insert(action.toEntity()));
		}
		
		@RequestMapping(
				method=RequestMethod.GET,
				path="/smartspace/actions",
				produces=MediaType.APPLICATION_JSON_VALUE)
		public ActionBoundary[] 
				getActions (
						@RequestParam(name="size", required=false, defaultValue="10") int size, 
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			return 
				this.actions
				.getActions(size, page)
				.stream()
				.map(ActionBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ActionBoundary[0]);
		}
		
		@RequestMapping(
				path="/smartspace/actions/{pattern}",
				method=RequestMethod.GET,
				produces=MediaType.APPLICATION_JSON_VALUE)
		public ActionBoundary[]
				getActionsByContent (
						@PathVariable("pattern") String pattern,
						@RequestParam(name="size", required=false, defaultValue="10") int size,
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			return
				this.actions
					.getActionsByContent(pattern, size, page)
					.stream()
					.map(ActionBoundary::new)
					.collect(Collectors.toList())
					.toArray(new ActionBoundary[0]);
		}
		@ExceptionHandler
		@ResponseStatus(HttpStatus.NOT_FOUND)
		public ErrorAction handleException (ActionNotFoundException e) {
			String message = e.getMessage();
			
			if (message == null || message.trim().isEmpty()) {
				message = "Could not find action";
			}
			
			return new ErrorAction(message);
		}

}
