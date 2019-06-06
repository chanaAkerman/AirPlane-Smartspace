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

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class ActionDaoIntegrationTests {
	private ActionDao dao;
	private EntityFactory factory;
	public ActionDaoIntegrationTests() {
		super();
		factory = new EntityFactoryImpl();
	}
	
	@Autowired
	public void setDao(ActionDao dao) {
		this.dao = dao;
	}
	@Before
	public void setup() {
		this.dao.deleteAll();	
	}
	@After
	public void teardown() {
		this.dao.deleteAll();
	}
	@Test
	public void CreateNewActionTest() throws Exception {
		/// GIVEN a new MemoryActionDao
		
		/// WHEN I create a ActionEntity with all of the required qualities
		ActionEntity action=factory.createNewAction("1", "1", "1", "1", "1", new Date(), Collections.synchronizedMap(new HashMap<>()));
		ActionEntity storedAction = dao.create(action);
		// Then No exception is thrown
				// And the UserEntity was stored in h2
				// And the returned UserEntity has an id
				assertThat(this.dao.readAll())
				.hasSize(1)
				.containsOnly(storedAction);
	}
	@Test(expected=Exception.class)
	public void CreateNewActionWithEmptyStringQualityTest() throws Exception {
		/// GIVEN a new MemoryActionDao
		
		/// WHEN I create a ActionEntity with empty String on required qualities 
		ActionEntity action =this.factory.createNewAction("1", "1", "1", "", "", null, null);
		dao.create(action);
		
		// Then exception is thrown
		// And the ActionEntity was not created and stored in h2 database
		assertThat(this.dao.readAll())
			.hasSize(0);
	}
	@Test(expected=Exception.class)
	public void CreateNewActionWithNullTest() throws Exception{
		// Given the dao is initialized
		
		// When I create a null action
		this.dao.create(null);
	}
	@Test
	public void DeleteAllReadAllActionsTest()throws Exception  {
		/// GIVEN a new MemoryActionDao
		
		/// WHEN I delete all objects
		ActionEntity action1 =this.factory.createNewAction("1", "1", "1", "1", "1", new Date(), new HashMap<>());
		dao.create(action1);
		
		ActionEntity action2 =this.factory.createNewAction("2", "2", "2", "2", "2", new Date(), new HashMap<>());
		dao.create(action2);

		// Then No exception is thrown
		// And the Dao size dropped to 0
	    assertThat(this.dao.readAll())
	    .hasSize(2)
	    .containsExactlyInAnyOrder(action1,action2);
		 dao.deleteAll();
		
		assertThat(dao.readAll())
		.hasSize(0);
	}

}
