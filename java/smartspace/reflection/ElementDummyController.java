package smartspace.reflection;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import smartspace.init.Demo;

@RestController
public class ElementDummyController {
	
	@RequestMapping(
			path="/elementDummy", 
			method= RequestMethod.POST, 
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public Demo doSomething (@RequestBody Demo input) {
		return new Demo();
	}

	@RequestMapping(
			path="/elementDummy", 
			method= RequestMethod.GET, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Demo[] doSomething () {
		return new Demo[] {new Demo()};
	}

	@RequestMapping(
			path="/elementDummy/{id}", 
			method= RequestMethod.GET, 
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Demo doSomething (@PathVariable("id") String id) {
		return new Demo();
	}
	
	@RequestMapping(
			path="/elementDummy/{id}", 
			method= RequestMethod.PATCH, 
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void doSomething (
			@PathVariable("id") String id, 
			@RequestBody Demo input) {
		// do some patching
	}


	@RequestMapping(
			path="/elementDummy/{id}", 
			method= RequestMethod.PUT, 
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void doSomethingElse (
			@PathVariable("id") String id, 
			@RequestBody Demo input) {
		// do some patching
	}

	@RequestMapping(
			path="/elementDummy{id}", 
			method= RequestMethod.DELETE)
	public void doSomethingElse (@PathVariable("id") String id) {
		// delete...
	}

}
