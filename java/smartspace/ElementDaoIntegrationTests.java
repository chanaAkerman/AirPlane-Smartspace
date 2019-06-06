package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.AnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.plugins.AirplaneSeat;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ElementDaoIntegrationTests {
	private AnhancedElementDao<String> dao;
	private EntityFactory factory;
	public ElementDaoIntegrationTests() {
		super();
		factory = new EntityFactoryImpl();
	}
	@Before
	public void setup() {
		this.dao.deleteAll();	
	}
	@After
	public void teardown() {
		this.dao.deleteAll();
	}
	@Autowired
	public void setElementDao(AnhancedElementDao<String> elementDao) {
		this.dao = elementDao;
	}
	@Test
	public void insertAttributes() {
		ElementEntity element = factory.createNewElement(
				new Location(0,0), "demo", "new", new Date(), false, "creatorDemo", "demo@gmail.com", new HashMap<>());
		AirplaneSeat res = new AirplaneSeat(false, 1, 123, "219B.chana");
		element.addAttribute(res);
	    this.dao.create(element);
	}
	@Test
	public void createNewElementTest() throws Exception {
		/// GIVEN a clean database
		
		/// WHEN I create a ElementEntity with all of the required qualities
		
		ElementEntity element= factory.createNewElement(new Location(1,1), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		ElementEntity storedElement = dao.create(element);	
		// Then No exception is thrown
		// And the ElementEntity was stored in h2
		// And the returned ElementEntity has an id
		
		assertThat(this.dao.readById(storedElement.getIdentifier()))
			.isPresent()
			.get()
			.isEqualTo(storedElement);
	}
	@Test(expected=Exception.class)
	public void CreateNewElementWithEmptyStringQualityTest() throws Exception {
		/// GIVEN a clean database
		
		/// WHEN I create an ElementEntity with empty String on required qualities 
		ElementEntity element= factory.createNewElement(new Location(1,1), "", "", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		dao.create(element);	
		
		// Then exception is thrown
		// And the ElementEntity was not created and stored in h2 database
		assertThat(this.dao.readAll())
			.hasSize(0);
	}
	@Test(expected=Exception.class)
	public void CreateNewUserWithNullTest() throws Exception{
		// Given clean database
		
		// When I create a null element
		this.dao.create(null);
		// Then exception is thrown
	}
	@Test
	public void ReadAllTest() { 
		/// GIVEN a clean database

		/// WHEN I insert objects to h2
		ElementEntity element1= factory.createNewElement(new Location(1,1), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		dao.create(element1);	
				
		ElementEntity element2= factory.createNewElement(new Location(2,2), "2", "2", new Date(), true, "2", "2", Collections.synchronizedMap(new HashMap<>()));
		dao.create(element2);
		
		
		/// THEN No exception is thrown
		/// AND i can read all of them
		/// AND h2 Database has 2 objects.
		assertThat(dao.readAll())
		    .containsExactlyInAnyOrder(element1,element2)
			.isNotEmpty()
			.hasSize(2);
	}
	@Test
	public void DeleteElementTest() { 
	/// GIVEN a clean database

	/// WHEN I insert objects to h2
	/// AND delete one them
	ElementEntity element1= factory.createNewElement( new Location(1,1), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
	dao.create(element1);	
	
	assertThat(dao.readAll())
	    .containsExactlyInAnyOrder(element1)
		.hasSize(1);
		    
	// THEN No exception is thrown
	// AND there is one less object in mongo Database after delete.
	dao.delete(element1);
	assertThat(dao.readAll())
	.hasSize(0);
			
	assertThat(dao.readAll()==null);
}
	@Test
	public void DeleteElementsTest() { 
	/// GIVEN a clean database

	/// WHEN I insert objects to h2
	/// AND delete one them
	ElementEntity element1= factory.createNewElement( new Location(1,1), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
	dao.create(element1);	
			
	ElementEntity element2= factory.createNewElement(new Location(2,2), "2", "2", new Date(), true, "2", "2", Collections.synchronizedMap(new HashMap<>()));
	dao.create(element2);
	
	assertThat(dao.readAll())
	    .containsExactlyInAnyOrder(element1,element2)
		.hasSize(2);
		    
	// THEN No exception is thrown
	// AND there is one less object in mongo Database after delete.
	dao.delete(element2);
	assertThat(dao.readAll())
    .containsExactlyInAnyOrder(element1)
	.hasSize(1);
			
	assertThat(dao.readAll()==null);
}
	@Test
	public void DeleteAllElementsTest() { 
		/// GIVEN a clean database

		/// WHEN I insert objects to h2
		/// AND read all of them
		/// AND delete them
		ElementEntity element1= factory.createNewElement(new Location(1,1), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		dao.create(element1);	
				
		ElementEntity element2= factory.createNewElement ( new Location(2,2), "2", "2", new Date(), true, "2", "2", Collections.synchronizedMap(new HashMap<>()));
		dao.create(element2);
		
		assertThat(dao.readAll())
		    .containsExactlyInAnyOrder(element1,element2)
			.isNotEmpty()
			.hasSize(2);
			    
		// THEN No exception is thrown
		// AND there is nothing in the h2 Database after delete.
		dao.deleteAll();
				
		assertThat(dao.readAll()==null);
	}
	@Test
	public void UpdateElementTest() throws Exception{
		// GIVEN a clean database
		
		// WHEN I Update Element
		ElementEntity element1= factory.createNewElement(new Location(0,0), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		ElementEntity insertedElement = dao.create(element1);
		
		assertThat(dao.readAll())
		.hasSize(1);
		
		ElementEntity update = new ElementEntity();
		update.setKey(insertedElement.getKey());
		//update.setKey(insertedElement.getIdentifier());
		update.setCreatorEmail("newEmail@gmail.com");
		this.dao.update(update);
		
		//THEN the specific element has been updated.
		// AND the Dao size has not changed
		insertedElement = this.dao.readByKey(insertedElement.getKey());
		if(insertedElement==null)
				throw new RuntimeException("Element is not available after update");
		assertThat(dao.readAll())
		.hasSize(1);
	}
	@Test(expected=Exception.class)
	public void UpdateNotExistingElementTest() throws Exception{
		// GIVEN a clean database
		
		// WHEN I Update not existing Element
		ElementEntity element1= factory.createNewElement( new Location(0,0), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		
		
		ElementEntity update = new ElementEntity();
		update.setKey(element1.getKey());
		update.setCreatorEmail("newEmail@gmail.com");
		this.dao.update(update);
		
		//THEN exception is thrown 
	}

}
