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
import smartspace.data.ElementEntity;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.logic.ElementNotFoundException;
import smartspace.logic.ElementService;
import smartspace.logic.UserService;
import smartspace.plugins.AlertBox;

@RestController
public class ElementController {
	private ElementService elements;
	private UserService users;
	@Autowired
	public ElementController(ElementService elements,UserService users) {
		super();
		this.elements = elements;
		this.users=users;
	}

	public ElementService getElements() {
		return elements;
	}

	public void setElements(ElementService elements) {
		this.elements = elements;
	}
	@RequestMapping(
			method=RequestMethod.DELETE,
			path="/smartspace/elements/{key}") 
	public void deleteSepcificElement (
			@PathVariable("key") String key) {
		try {
		this.elements
			.deleteOneElementByKey(key);
		}catch(Exception e) {
			//couldn't find the element => the element does not exist in Database
			System.err.println("Element: "+key+" does not exist");
		}
	}
	@PerformanceMonitor
	@RequestMapping(
			method=RequestMethod.PUT,
			path="/smartspace/elements/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void update(
			@PathVariable("managerSmartspace") String managerSmartspace,
			@PathVariable("managerEmail") String managerEmail,
			@PathVariable("elementSmartspace") String elementSmartspace,
			@PathVariable("elementId") String elementId,
			@RequestBody ElementBoundary element) {
		String managerKey = managerSmartspace+"#"+managerEmail;
		String elementKey = elementId+"#"+elementSmartspace;
		UserEntity user = this.users.getUserByKey(managerKey);
		if(user!=null && (user.getRole()==UserRole.ADMIN || user.getRole()==UserRole.MANAGER))
		{
			this.elements.update(elementKey, element.toEntity());
		}
		
	}
	@PerformanceMonitor
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/elements/{managerSmartspace}/{managerEmail}",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary insert(
				@PathVariable("managerSmartspace") String managerSmartspace,
				@PathVariable("managerEmail") String managerEmail,
				@RequestBody ElementBoundary element) {
//			ElementEntity input = element.toEntity();
//			
//			ElementEntity output = this.elements.insert(input);
//			
//			return new ElementBoundary(output);
		String managerKey = managerSmartspace+"#"+managerEmail;
		UserEntity user = this.users.getUserByKey(managerKey);
		if(user !=null && (user.getRole()==UserRole.MANAGER || user.getRole()==UserRole.ADMIN))
		{
			return new ElementBoundary(this.elements.insert(element.toEntity()));
		}
		return null;
		}
		
		@RequestMapping(
				method=RequestMethod.GET,
				path="/smartspace/elements/{userSmartspace}/{userEmail}",
				produces=MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary[] 
				getElements (
						@PathVariable("userSmartspace") String userSmartspace,
						@PathVariable("userEmail") String userEmail,
						@RequestParam(name="size", required=false, defaultValue="10") int size, 
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			String key=userSmartspace+"#"+userEmail;
			UserEntity user = this.users.getUserByKey(key);
			if(user!=null & (user.getRole()==UserRole.ADMIN || user.getRole()==UserRole.MANAGER))
			{
			return 
				this.elements
				.getElements(size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
			}
			return null;
		}
		@RequestMapping(
				method=RequestMethod.GET,
				path="/smartspace/elements/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				produces=MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary
				getElementByElementId (
						@PathVariable("userSmartspace") String userSmartspace,
						@PathVariable("userEmail") String userEmail, 
						@PathVariable("elementSmartspace") String elementSmartspace,
						@PathVariable("elementId") String elementId, 
						@RequestParam(name="size", required=false, defaultValue="10") int size, 
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			String userKey = userSmartspace+"#"+userEmail;
			UserEntity user = this.users.getUserByKey(userKey);
			if(user!=null && (user.getRole()==UserRole.MANAGER || user.getRole()==UserRole.ADMIN))
			{
				ElementEntity element = this.elements.getElementById(elementId);
				if(this.elements.getElementById(elementId)!=null) {;
					return new ElementBoundary(element);
				}
				else {
					System.err.println("Element "+elementId+" doesn't exist");
				}
			}
			return null;
		}
		
		@RequestMapping(
				method=RequestMethod.GET,
				path="smartspace/elements/{userSmartspace}/{userEmail}?search=location&x={x}&y={y}&distance={distance}&page={page}&size={size}",
				produces=MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary[] 
				getElementsByLocation (
						@PathVariable(name="userSmartspace", required=true) String userSmartspace, 
						@PathVariable(name="userEmail", required=true) String userEmail, 
						@PathVariable(name="search", required=true) String location ,
						@PathVariable(name="x", required=true) double x, 
						@PathVariable(name="y", required=true) double y ,
						@PathVariable(name="distance", required=true) double distance, 
						@RequestParam(name="size", required=false, defaultValue="10") int size, 
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			String userKey = userSmartspace+"#"+userEmail;
			UserEntity user = this.users.getUserByKey(userKey);
			if(user!=null)
			{
			return 
				this.elements
				.getElementsByLocation(x,y,distance,size, page)
				.stream()
				.map(ElementBoundary::new)
				.collect(Collectors.toList())
				.toArray(new ElementBoundary[0]);
			}
			return null;
		}
		@RequestMapping(
				path="smartspace/elements/{userSmartspace}/{userEmail}?search=name&value={name}&page={page}&size={size}",
				method=RequestMethod.GET,
				produces=MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary[]
				getElementsByName (
						@PathVariable(name="userSmartspace",required=true) String userSmartspace,
						@PathVariable(name="userEmail", required=true) String userEmail, 
						@PathVariable(name="search", required=true) String name ,
						@RequestParam(name="size", required=false, defaultValue="10") int size,
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			String key = userSmartspace+"#"+userEmail;
			UserEntity user = this.users.getUserByKey(key);
			if(user!=null)
			{
			return
				this.elements
					.getElementsByName(name, size, page)
					.stream()
					.map(ElementBoundary::new)
					.collect(Collectors.toList())
					.toArray(new ElementBoundary[0]);
			}
			return null;
		}
		@RequestMapping(
				path="smartspace/elements/{userSmartspace}/{userEmail}?search=type&value={type}&page={page}&size={size}",
				method=RequestMethod.GET,
				produces=MediaType.APPLICATION_JSON_VALUE)
		public ElementBoundary[]
				getElementsByType (
						@PathVariable(name="userSmartspace",required=true) String userSmartspace,
						@PathVariable(name="userEmail", required=true) String userEmail, 
						@PathVariable(name="search", required=true) String type ,
						@RequestParam(name="size", required=false, defaultValue="10") int size,
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			String userKey = userSmartspace+"#"+userEmail;
			UserEntity user = this.users.getUserByKey(userKey);
			if(user!=null)
			{
			return
				this.elements
					.getElementsByType(type, size, page)
					.stream()
					.map(ElementBoundary::new)
					.collect(Collectors.toList())
					.toArray(new ElementBoundary[0]);
			}
			return null;
		}
		@ExceptionHandler
		@ResponseStatus(HttpStatus.NOT_FOUND)
		public ErrorElement handleException (ElementNotFoundException e) {
			String message = e.getMessage();
			
			if (message == null || message.trim().isEmpty()) {
				message = "Could not find element";
			}
			
			return new ErrorElement(message);
		}
	

}
