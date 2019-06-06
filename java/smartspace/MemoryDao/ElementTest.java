package smartspace.MemoryDao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smartspace.dao.memory.MemoryElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

public class ElementTest {
	private MemoryElementDao dao;
	
	@Before
	public void setup() {
		dao=new MemoryElementDao();	
	}
	@After
	public void teardown() {
		this.dao=null;
	}
	
	@Test
	public void CreateNewElementTest() throws Exception {
		/// GIVEN a new MemoryElementDao
		
		/// WHEN I create a ElementEntity with all of the required qualities
		ElementEntity element= new ElementEntity("1","1", new Location(0,0), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		ElementEntity storedElement = dao.create(element);
		
		
		// Then No exception is thrown
		// And the ElementEntity was stored to the Dao
		// And the Dao size was increased by 1
		// And the returned ElementEntity has an id
		assertThat(dao.getElements().values())
			.usingElementComparator((e1, e2)->e1.getElementId().compareTo(e2.getElementId()))
			.contains(element);
	
		assertThat(dao.getElements())
			.hasSize(1);
	
		assertThat(storedElement.getKey())
			.isNotNull();
	}
	@Test
	public void DeleteElementsTest() { 
	/// GIVEN a clean database

	/// WHEN I insert objects to h2
	/// AND delete one them
	ElementEntity element1= new ElementEntity("1","1", new Location(1,1), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
	dao.create(element1);	
			
	ElementEntity element2= new ElementEntity("2","2", new Location(2,2), "2", "2", new Date(), true, "2", "2", Collections.synchronizedMap(new HashMap<>()));
	dao.create(element2);
	
	assertThat(dao.readAll())
	    .containsExactlyInAnyOrder(element1,element2)
		.isNotEmpty()
		.hasSize(2);
		    
	// THEN No exception is thrown
	// AND there is one less object in h2 Database after delete.
	dao.delete(element2);
	assertThat(dao.readAll())
    .containsExactlyInAnyOrder(element1)
	.hasSize(1);
			
	assertThat(dao.readAll()==null);
}
	@Test
	public void DeleteAllElementsReadAllTest() {
		/// GIVEN a new MemoryElementDao
		
		/// WHEN I delete all objects
		ElementEntity element1= new ElementEntity("1","1", new Location(0,0), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		dao.create(element1);
		
		ElementEntity element2= new ElementEntity("2","2", new Location(0,0), "2", "2", new Date(), true, "2", "2", Collections.synchronizedMap(new HashMap<>()));
		dao.create(element2);

		// THEN You can read all the objects
	    assertThat(this.dao.readAll())
	    .containsExactlyInAnyOrder(element1,element2)
		.isNotEmpty();
	    
	    // AND No exception is thrown
	 	// AND the Dao size dropped to 0
		dao.deleteAll();
		
		assertThat(dao.getElements())
		.hasSize(0);
		
		
	}
	@Test
	public void UpdateElementTest() throws Exception{
		// GIVEN we have a dao
		
		// WHEN I Update Element
		ElementEntity element1= new ElementEntity("1","1", new Location(0,0), "1", "1", new Date(), true, "1", "1", Collections.synchronizedMap(new HashMap<>()));
		ElementEntity insertedElement = dao.create(element1);
		
		assertThat(dao.getElements())
		.hasSize(1);
		
		ElementEntity update = new ElementEntity();
		update.setKey(insertedElement.getKey());
		update.setCreatorEmail("newEmail@gmail.com");
		this.dao.update(update);
		
		//THEN the specific element has been updated.
		// AND the Dao size has not changed
		insertedElement = this.dao.readById(insertedElement.getKey())
				.orElseThrow(()->new RuntimeException("Element is not available after update"));
		assertThat(dao.getElements())
		.hasSize(1);
	}

}
