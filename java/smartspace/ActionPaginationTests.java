package smartspace;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.data.util.EntityFactory;
import smartspace.dao.AnhancedActionDao;
import smartspace.data.ActionEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties={"spring.profiles.active=default"})
public class ActionPaginationTests {
	private AnhancedActionDao<String> actionDao;
	private EntityFactory factory;
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void setMessageDao(AnhancedActionDao<String> actionDao) {
		this.actionDao = actionDao;
	}

	
    @After
	public void setup() {
		this.actionDao.deleteAll();
	}
	
	@Test
	public void readAllUsersWithPaginationTest() throws Exception{
		// GIVEN the database contains 20 users
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewAction(
				"" + i, ""+i, "" +i,""+i,""+i,new Date(),new HashMap<>()))
		.forEach(this.actionDao::create);

			
			
		// WHEN I get a page of 3 after skipping first two pages 
		int size = 3;
		int page = 2;
		List<ActionEntity> page2Results = 
			this.actionDao.readAll(size, page);
		
		// THEN the result contains 3 results
		assertThat(page2Results)
			.hasSize(3);
	}
	@Test
	public void testGetAllUsersWithPaginationAndSorting() throws Exception{
		// GIVEN the database contains 10 actions
		IntStream.range(0, 9)
		.mapToObj(i->this.factory.createNewAction(""+i, ""+i, ""+i, ""+i, ""+i, new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);

		// WHEN I get a page of 3 after skipping first two pages 
		int size = 3;
		int page = 2;
		List<ActionEntity> page2Results = 
			this.actionDao.readAll("actionType", size, page);
		
		// THEN the result contains specific 3 results
		assertThat(page2Results
				.stream()
				.map(ActionEntity::getActionType)
				.collect(Collectors.toList()))
			.containsExactly("6", "7", "8");
	}
	@Test
	public void testGetByNamePatternWithPagination() throws Exception{
		// GIVEN the database contains 20 actions with the text "abc"
		// AND the database contains 3 actions with the text "xyz"
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewAction
				(""+i, ""+i, ""+i, ""+i, "abc", new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);

		IntStream.range(20, 23)
		.mapToObj(i->this.factory.createNewAction
				(""+i, ""+i, ""+i, ""+i, "xyz", new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);

			
		// WHEN I get a page of 3 with no skipping with the text "xyz"
		int size = 3;
		int page = 0;
		String text = "xyz";
		List<ActionEntity> results = 
			this.actionDao.readActionContainingText(
					text, size, page);
		// THEN the result contains 3 results
		assertThat(results)
			.hasSize(3);
	}
	@Test
	public void testGetByNamePatternWithPaginationAndSkipping() throws Exception{
		// GIVEN the database contains 20 messages with the text "abc"
		// AND the database contains 4 messages with the text "xyz"
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewAction
				(""+i, ""+i, ""+i, ""+i, "abc", new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);

		IntStream.range(20, 24)
		.mapToObj(i->this.factory.createNewAction
				(""+i, ""+i, ""+i, ""+i, "xyz", new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);

			
		// WHEN I get a page of 3 with skipping first page with the text "xyz"
		int size = 3;
		int page = 1;
		String text = "xyz";
		List<ActionEntity> results = 
			this.actionDao.readActionContainingText(
					text, "actionType", size, page);
		// THEN the result contains 1 results
		assertThat(results)
			.hasSize(1);
	}
	@Test
	public void testGetDateWithPagination() throws Exception{
		// GIVEN the database contains 17 actions from today
		// AND the database contains 3 are in the date range of two days ago to yesterday
		IntStream.range(0, 17)
		.mapToObj(i->this.factory.createNewAction
				(""+i, ""+i, ""+i, ""+i, "abc", new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);
		
		Date twentyEightHoursAgo = new Date(System.currentTimeMillis() - 28*3600*1000); 
		
		IntStream.range(17, 20)
		.mapToObj(i->this.factory.createNewAction
				(""+i, ""+i, ""+i, ""+i, "xyz", new Date(), new HashMap<>()))
		.peek(ac->ac.setCreationTimestamp(twentyEightHoursAgo))
		.forEach(this.actionDao::create);

			
		// WHEN I get a page of 5 with skipping no pages with actions from two days ago and to last day
		int size = 5; // number of actions in one page
		int page = 0;
		Date twoDaysAgo = new Date(System.currentTimeMillis() - 1000*3600*48);
		Date yesterday = new Date(System.currentTimeMillis() - 1000*3600*24);
		List<ActionEntity> results = this.actionDao.readAllCreationTimestamp(twoDaysAgo, yesterday, size, page);
		
		// THEN the result contains 3 results
		assertThat(results)
			.hasSize(3);
	}
}