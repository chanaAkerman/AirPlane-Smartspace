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
import smartspace.data.UserEntity;
import smartspace.logic.UserNotFoundException;
import smartspace.logic.UserService;

@RestController
public class UserController {
	private UserService users;
    @Autowired
	public UserController(UserService users) {
		super();
		this.users=users;
	}
    public UserService getUsers() {
		return users;
	}
	public void setUsers(UserService users) {
		this.users = users;
	}
	@RequestMapping(
			method=RequestMethod.DELETE,
			path="/smartspace/users/{key}")
	public void deleteSepcificUser (
			@PathVariable("key") String key) {
		try {
		this.users
			.deleteOneUserByKey(key);
		}catch(Exception e) {
			//couldn't find the element => the element does not exist in Database
			System.err.println("Element: "+key+" doesn't exist");
		}
	}
	@PerformanceMonitor
	@RequestMapping(
			method=RequestMethod.PUT,
			path="/smartspace/users/login/{userSmartspace}/{userEmail}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void update(
			@PathVariable("userSmartspace") String userSmartspace,
			@PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary user) {
		String key = userSmartspace+"#"+userEmail;
		UserEntity userEntity = user.toEntity();
		if(this.users.getUserByKey(key)!=null) {
			this.users.update (userEntity.getKey(), userEntity);
		}
		
	}
	@PerformanceMonitor
	@RequestMapping(
			method=RequestMethod.POST,
			path="/smartspace/users",
			consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary insert(
				@RequestBody UserBoundary user) {
//			UserEntity input = user.toEntity();
//			
//			UserEntity output = this.users.insert(input);
//			
//			return new UserBoundary(output);
			return new UserBoundary(
					this.users.insert(user.toEntity()));
		}
		
		@RequestMapping(
				method=RequestMethod.GET,
				path="/smartspace/users/login/{userSmartspace}/{userEmail}",
				produces=MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary
				getUsers (
						@PathVariable("userSmartspace") String userSmartspace,
						@PathVariable("userEmail") String userEmail,
						@RequestParam(name="size", required=false, defaultValue="10") int size, 
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			String userKey = userSmartspace+"#"+userEmail;
			UserEntity user = this.users.getUserByKey(userKey);
			if(this.users.getUserByKey(userKey)!=null) {
				return new UserBoundary(user);
			}
			return null;
		}
		@RequestMapping(
				path="/smartspace/users/login",
				method=RequestMethod.GET,
				produces=MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[]
				getUsers(

						@RequestParam(name="size", required=false, defaultValue="10") int size,
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
				return
						this.users
							.getUsers(size, page)
							.stream()
							.map(UserBoundary::new)
							.collect(Collectors.toList())
							.toArray(new UserBoundary[0]);
		}
		
		
		@RequestMapping(
				path="/smartspace/users/login/{userSmartspace}/{userEmail}/{pattern}",
				method=RequestMethod.GET,
				produces=MediaType.APPLICATION_JSON_VALUE)
		public UserBoundary[]
				getUsersByContent (
						@PathVariable("userSmartspace") String userSmartspace,
						@PathVariable("userEmail") String userEmail,
						@PathVariable("pattern") String pattern,
						@RequestParam(name="size", required=false, defaultValue="10") int size,
						@RequestParam(name="page", required=false, defaultValue="0") int page) {
			String userKey = userSmartspace+"#"+userEmail;
			if(this.users.getUserByKey(userKey)!=null) {
				return
						this.users
							.getUsersByContent(pattern, size, page)
							.stream()
							.map(UserBoundary::new)
							.collect(Collectors.toList())
							.toArray(new UserBoundary[0]);
			}
			return null;

				
		}
		@ExceptionHandler
		@ResponseStatus(HttpStatus.NOT_FOUND)
		public ErrorUser handleException (UserNotFoundException e) {
			String message = e.getMessage();
			
			if (message == null || message.trim().isEmpty()) {
				message = "Could not find user";
			}
			
			return new ErrorUser(message);
		}
	

}
