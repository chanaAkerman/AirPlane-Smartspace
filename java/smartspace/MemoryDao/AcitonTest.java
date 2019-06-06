package smartspace.MemoryDao;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.memory.MemoryActionDao;
import smartspace.data.ActionEntity;
@SpringBootTest
//@RunWith(SpringRunner.class)
public class AcitonTest {

	private MemoryActionDao dao;
	
	@Before
	public void setup() {
		dao=new MemoryActionDao();	
	}
	@After
	public void teardown() {
		this.dao=null;
	}
	
	@Test
	public void CreateNewActionTest() throws Exception {
		/// GIVEN a new MemoryActionDao
		
		/// WHEN I create a ActionEntity with all of the required qualities
		ActionEntity action =new ActionEntity("1", "1", "1", "1", "1", "1", "1", new Date(),Collections.synchronizedMap(new HashMap<>()) );
		ActionEntity storedAction = dao.create(action);
		
		
		// Then No exception is thrown
		// And the ActionEntity was stored to the Dao
		// And the Dao size was increased by 1
		// And the returned ActionEntity has an id
		assertThat(dao.getActions().values())
			.usingElementComparator((a1, a2)->a1.getActionId().compareTo(a2.getActionId()))
			.contains(action);
	
		assertThat(dao.getActions())
			.hasSize(1);
	
		assertThat(storedAction.getKey())
			.isNotNull();
	}
	@Test
	public void DeleteAllActionsReadAllTest() throws Exception {
		/// GIVEN a new MemoryActionDao
		
		/// WHEN I delete all objects
		ActionEntity action1 =new ActionEntity("1", "1", "1", "1", "1", "1", "1", new Date(), null);
		dao.create(action1);
		
		ActionEntity action2 =new ActionEntity("2", "2", "2", "2", "2", "2", "2", new Date(), null);
		dao.create(action2);

		// Then No exception is thrown
		// And the Dao size dropped to 0
	    assertThat(this.dao.readAll())
	    .containsExactlyInAnyOrder(action1,action2)
		.isNotEmpty();
		dao.deleteAll();
		
		assertThat(dao.getActions())
		.hasSize(0);
	}
}
