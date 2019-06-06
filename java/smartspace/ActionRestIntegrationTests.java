package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import smartspace.dao.AnhancedActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.util.EntityFactory;
import smartspace.layout.ActionBoundary;
import smartspace.layout.UserBoundary;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class ActionRestIntegrationTests {
	@LocalServerPort
	private int port;
	
	private String baseUrl;
	private RestTemplate restTemplate;
	
	private AnhancedActionDao<String> actionDao;
	private EntityFactory factory;
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void setActionDao(AnhancedActionDao<String> actionDao) {
		this.actionDao = actionDao;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/actions";
		this.restTemplate = new RestTemplate();
	}
	@Before
	public void setUp() {
		this.actionDao.deleteAll();
	}
	@After
	public void teardown() {
		this.actionDao.deleteAll();
	}
	@Test
	public void insertOneActionTest() throws Exception{
		ActionEntity action = this.factory.createNewAction("1", "1", "1", "1", "1", new Date(), new HashMap<>());
		
		ActionBoundary actionBoundary=new ActionBoundary(action);
		ActionBoundary newUser = restTemplate.postForObject(baseUrl,
				actionBoundary,
				ActionBoundary.class);
		
		ActionBoundary[] response= this.restTemplate.getForObject(
				this.baseUrl+"?page={page}&size={size}", 
				ActionBoundary[].class,0,2);
		
		assertThat(response)
		.hasSize(1);
		
	}
	@Test
	public void testGetActionsWithPagination() throws Exception{
		ActionEntity action1 = this.factory.createNewAction("1", "1", "1", "1", "1", new Date(), new HashMap<>());
		this.actionDao.create(action1);
		
		ActionEntity action2 = this.factory.createNewAction("2", "2", "2", "2", "2", new Date(), new HashMap<>());
		this.actionDao.create(action2);
		
		ActionBoundary actionBoundary1=new ActionBoundary(action1);
		ActionBoundary actionBoundary2=new ActionBoundary(action2);
		
		ActionBoundary[] result = new ActionBoundary[2];
		result[0]=actionBoundary1;
		result[1]=actionBoundary2;
		
		ActionBoundary[] response= this.restTemplate.getForObject(
				this.baseUrl+"?page={page}&size={size}", 
				ActionBoundary[].class,0,2);
		
		assertThat(response)
		.hasSize(2);
		
	}
	@Test
	public void testGetManyActionsWithPagination() throws Exception{
		// GIVEN the database contains 400 Actions
		int size = 400;
		
		IntStream.range(1,size+1)
		.mapToObj(i->this.factory.createNewAction(""+i,""+i,"" + i, ""+i, "" +i, new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);
		
		// WHEN I get Actions of the page = 0 and size = 100
		ActionBoundary[] result = 
			this.restTemplate
			.getForObject(
					this.baseUrl + "?page={page}&size={size}", 
					ActionBoundary[].class, 
					0, 100);
		// THEN I receive 100 Actions
		assertThat(result)
			.hasSize(100);
	}
	@Test
	public void testGetActionsWithPaginationOfSecondPage() throws Exception{
		// GIVEN the database contains 40 Actions
		int size = 40;
		IntStream.range(1,size+1)
		.mapToObj(i->this.factory.createNewAction(""+i,""+i,"" + i, ""+i, "" +i, new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);
		
		// WHEN I getActions of the page = 1 and size = 100
		ActionBoundary[] result = 
			this.restTemplate
			.getForObject(
					this.baseUrl + "?page={page}&size={size}", 
					ActionBoundary[].class,
					1,100);
		
		// THEN I receive 0 Actions
		assertThat(result)
		.isEmpty();
	}

	@Test
	public void testGetActionsWithPaginationOfExistingSecondPage() throws Exception{
		// GIVEN the database contains 40 Actions
		int size = 40;
		IntStream.range(1,size+1)
		.mapToObj(i->this.factory.createNewAction(""+i,""+i,"" + i, ""+i, "" +i, new Date(), new HashMap<>()))
		.forEach(this.actionDao::create);
		
		// WHEN I getActions of the page = 1 and size = 30
		ActionBoundary[] result = 
				this.restTemplate
				.getForObject(
						this.baseUrl + "?page={page}&size={size}", 
						ActionBoundary[].class, 
						0, 20);
		// THEN I receive 10 actions
		assertThat(result)
			.hasSize(20);
	}
	
	@Test
	public void testInsertAndGetActionsUsingRest() throws Exception{
		// GIVEN the database is empty
		
		// WHEN I POST 3 new actions
		// AND I get actions of page 0 of size 2
		int size=3;
		List<ActionBoundary> newBoundaries = 
		IntStream.range(1, size + 1)
		.mapToObj(i->this.factory.createNewAction(""+i,""+i,"" + i, ""+i, "" +i, new Date(), new HashMap<>()))
			.map(name->new ActionBoundary("1#1","1","1","1","1","1",new Date(), new HashMap<>()))
			.map(boudary->this.restTemplate.postForObject(baseUrl,boudary, ActionBoundary.class))
			.collect(Collectors.toList());
			
			ActionBoundary[] getResult
		  = this.restTemplate.getForObject(
					this.baseUrl + "?size={size}&page={page}", 
					ActionBoundary[].class, 
					2, 0);
			
		
		// THEN the received actions are similar to 2 of the new actions
		assertThat(getResult)
			.hasSize(2)
			.usingElementComparatorOnFields("actionType")
			.containsAnyElementsOf(newBoundaries);
	}
	@Test
	public void testDeleteActionWithInvalidKey() throws Exception {
		// GIVEN the database is empty
		
		// WHEN we delete a element with id = 5
		this.restTemplate.delete(this.baseUrl+"/{key}", "5");
		// THEN the database remains of the original size
		assertThat(this.actionDao.readAll())
			.isEmpty();
			
	}
}

