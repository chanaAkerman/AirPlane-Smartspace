package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.swing.text.DefaultStyledDocument.ElementBuffer;

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

import smartspace.dao.AnhancedElementDao;
import smartspace.dao.AnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;
import smartspace.plugins.AirplaneSeat;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class ElementRestIntegrationTests {
	@LocalServerPort
	private int port;
	
	private String baseUrl;
	private RestTemplate restTemplate;
	
	private AnhancedElementDao<String> elementDao;
	private AnhancedUserDao<String> userDao;
	private EntityFactory factory;
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@Autowired
	public void setUserDao(AnhancedElementDao<String> elementDao,AnhancedUserDao<String> userDao) {
		this.elementDao = elementDao;
		this.userDao = userDao;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/elements";
		this.restTemplate = new RestTemplate();
		this.factory = new EntityFactoryImpl();
	}
	@Before
	public void setUp() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}
	@After
	public void teardown() {
		this.elementDao.deleteAll();
		this.userDao.deleteAll();
	}
	@Test
	public void testPostAndGetTest() throws Exception {
		UserEntity user=this.userDao.create(this.factory.createNewUser("user2@gmail.com", "adminUser", ":)", UserRole.ADMIN, new Long(1)));

		ElementEntity element =this.factory.createNewElement(new Location(0, 0),"test" ,
        		"FIRST", new Date(), false, "2019B.chana", user.getUserEmail(), new HashMap<>());
        ElementBoundary elementBoundary = new ElementBoundary(element);
        ElementBoundary result =  this.restTemplate
		.postForObject(
				this.baseUrl+"/{managerSmartspace}/{managerEmail}", 
				elementBoundary, 
				ElementBoundary.class,user.getUserSmartspace(),user.getUserEmail());
        
        ElementBoundary response= this.restTemplate.getForObject(
				this.baseUrl+"/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}",
				ElementBoundary.class,"2019B.chana","user2@gmail.com",result.getElementSmartspace(),result.getElementId());
        assertThat(element.equals(response.toEntity()));
		
	}
	@Test
	public void testPutMoreAttributesWithPostTest() throws Exception {
		UserEntity user=this.userDao.create(this.factory.createNewUser("user2@gmail.com", "adminUser", ":)", UserRole.ADMIN, new Long(1)));

		Location location = new Location(0,0);
		
		AirplaneSeat res = new AirplaneSeat(false, 1, 870.0, "1A");
		HashMap<String,Object>map = new HashMap<>();
		map.put("userKey", res.getUserKey());
		map.put("category", res.getCategory());
		map.put("meal", res.getMeal().toString());
		map.put("confrimed", res.isConfirmed());
		map.put("reserved", res.isReserved());
		
		// GIVEN the database contains an element
		ElementEntity entity= this.factory.createNewElement(location, "1", "1",new Date(), true, "1", "1", new HashMap<>());
		ElementBoundary elementBoundary = new ElementBoundary(entity);
		
		ElementBoundary result = 
				  this.restTemplate
					.postForObject(
							this.baseUrl+"/{managerSmartspace}/{managerEmail}", 
							elementBoundary, 
							ElementBoundary.class,user.getUserSmartspace(),user.getUserEmail());
		
		//ElementEntity updateElement = this.elementDao.create(entity);
		ElementBoundary original = new ElementBoundary(result.toEntity());
		// WHEN we PUT an update for this element
		
		Date date = new Date();
		result.setCreationTimestamp(date);
		result.setMoreAttributes(map);
		
		this.restTemplate
			.put(this.baseUrl+"/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
					result,user.getUserSmartspace(),user.getUserEmail(),result.getElementSmartspace(),result.getElementId());
		
		
		// THEN the element is updated in mongodb
		String theStringKey = original.getKey(); 
		String[] args =theStringKey.split("#");
		
		String elementId="",elementSmartspace="";
		if (args.length == 2) {
			elementId=args[0];
			elementSmartspace=args[1];
		}
		assertThat(this.elementDao.readByKey(theStringKey))
			.isNotNull()
			.extracting("elementSmartspace","elementId","location","name","type","creationTimestamp","Expired","creatorSmartspace","creatorEmail","moreAttributes")
			.containsExactly(elementSmartspace, elementId, location,"1","1",date,true,"1","1", map);
	}
	@Test
	public void testPutMoreAttributesTest() throws Exception {
		UserEntity user=this.userDao.create(this.factory.createNewUser("user2@gmail.com", "adminUser", ":)", UserRole.ADMIN, new Long(1)));

		Location location = new Location(0,0);
		Date date = new Date();
		
		AirplaneSeat res = new AirplaneSeat(false, 1, 870.0, "1A");
		HashMap<String,Object>map = new HashMap<>();
		map.put("userKey", res.getUserKey());
		map.put("category", res.getCategory());
		map.put("meal", res.getMeal().toString());
		map.put("confrimed", res.isConfirmed());
		map.put("reserved", res.isReserved());
		
		// GIVEN the database contains a message
		ElementEntity entity= this.factory.createNewElement(location, "1", "1",date, true, "1", "1", new HashMap<>());
		ElementEntity updateElement = this.elementDao.create(entity);
		ElementBoundary original = new ElementBoundary(updateElement);
		// WHEN we PUT an update for this element
		original.setMoreAttributes(map);
		
		this.restTemplate
			.put(this.baseUrl+"/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
					original,user.getUserSmartspace(),user.getUserEmail(),original.getElementSmartspace(),original.getElementId());
		// THEN the element is updated in mongodb
		String theStringKey = original.getKey(); 
		String[] args =theStringKey.split("#");
		
		String elementId="",elementSmartspace="";
		if (args.length == 2) {
			elementId=args[0];
			elementSmartspace=args[1];
		}
		assertThat(this.elementDao.readByKey(theStringKey))
			.isNotNull()
			.extracting("elementSmartspace","elementId","location","name","type","creationTimestamp","Expired","creatorSmartspace","creatorEmail","moreAttributes")
			.containsExactly(elementSmartspace, elementId, location,"1","1",date,true,"1","1", map);
	}
	@Test
	public void testInsertSingleElement() throws Exception{
		// GIVEN the database is clean
		UserEntity adminUser = this.factory.createNewUser("admin@gmail.com", "Ron", ":-)", UserRole.ADMIN, new Long(0));
		this.userDao.create(adminUser);
		// WHEN I post a new MessageBoundary
		ElementEntity element1=this.factory.createNewElement(new Location(),"1","1",new Date(),false,"1","1",new HashMap<>());
		ElementBoundary elementBoundary = new ElementBoundary(element1);
		
		ElementBoundary result = 
		  this.restTemplate
			.postForObject(
					this.baseUrl+"/{managerSmartspace}/{managerEmail}", 
					elementBoundary, 
					ElementBoundary.class,adminUser.getUserSmartspace(),adminUser.getUserEmail());
		
		// THEN the database contains the new message
		String theKey = result.getKey();
		assertThat(result.getKey() == element1.getKey());
			
	}
	@Test
	public void postTest() {
		UserEntity adminUser = this.factory.createNewUser("admin@gmail.com", "Ron", ":-)", UserRole.ADMIN, new Long(0));
		this.userDao.create(adminUser);
		
		ElementEntity element1=this.factory.createNewElement(new Location(),"1","1",new Date(),false,"1","1",new HashMap<>());
		ElementBoundary elementBoundary = new ElementBoundary(element1);

		ElementBoundary result = 
				  this.restTemplate
					.postForObject(
							this.baseUrl+"/{managerSmartspace}/{managerEmail}", 
							elementBoundary, 
							ElementBoundary.class,adminUser.getUserSmartspace(),adminUser.getUserEmail());
		
		
		ElementBoundary[] response= this.restTemplate.getForObject(
				this.baseUrl+"/{userSmartspace}/{userEmail}?page={page}&size={size}",
				ElementBoundary[].class,adminUser.getUserSmartspace(),adminUser.getUserEmail(),0,2);
		assertThat(response).hasSize(1);
	}
	@Test
	public void testGetUsersByAdminWithPagination() throws Exception{
		UserEntity admin = this.factory.createNewUser("admin@gmail.com", "Ron", ":-)", UserRole.ADMIN, new Long(0));
		this.userDao.create(admin);
		
		ElementEntity element1=this.factory.createNewElement(new Location(),"1","1",new Date(),false,"1","1",new HashMap<>());
		this.elementDao.create(element1);
		
		ElementEntity element2=this.factory.createNewElement(new Location(),"2","2",new Date(),false,"2","2",new HashMap<>());
		this.elementDao.create(element2);
		
		ElementBoundary elementBoundary1=new ElementBoundary(element1);
		ElementBoundary elementBoundary2=new ElementBoundary(element2);
		
		ElementBoundary[] result = new ElementBoundary[2];
		result[0]=elementBoundary1;
		result[1]=elementBoundary2;
		ElementBoundary[] response= this.restTemplate.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}?page={page}&size={size}",
						ElementBoundary[].class,admin.getUserSmartspace(),admin.getUserEmail(),0,2);
		assertThat(response)
		.hasSize(2);
		
	}
	@Test
	public void testGetUsersByMangaerWithPagination() throws Exception{
		UserEntity manager = this.factory.createNewUser("admin@gmail.com", "Ron", ":-)", UserRole.MANAGER, new Long(0));
		this.userDao.create(manager);
		
		ElementEntity element1=this.factory.createNewElement(new Location(),"1","1",new Date(),false,"1","1",new HashMap<>());
		this.elementDao.create(element1);
		
		ElementEntity element2=this.factory.createNewElement(new Location(),"2","2",new Date(),false,"2","2",new HashMap<>());
		this.elementDao.create(element2);
		
		ElementBoundary elementBoundary1=new ElementBoundary(element1);
		ElementBoundary elementBoundary2=new ElementBoundary(element2);
		
		ElementBoundary[] result = new ElementBoundary[2];
		result[0]=elementBoundary1;
		result[1]=elementBoundary2;
		
		ElementBoundary[] response= this.restTemplate.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}?page={page}&size={size}",
						ElementBoundary[].class,manager.getUserSmartspace(),manager.getUserEmail(),0,2);
		assertThat(response).hasSize(2);
		
	}
	@Test
	public void testGetUsersByPlayerWithPagination() throws Exception{
		UserEntity player = this.factory.createNewUser("admin@gmail.com", "Ron", ":-)", UserRole.PLAYER, new Long(0));
		this.userDao.create(player);
		
		ElementEntity element1=this.factory.createNewElement(new Location(),"1","1",new Date(),false,"1","1",new HashMap<>());
		this.elementDao.create(element1);
		
		ElementEntity element2=this.factory.createNewElement(new Location(),"2","2",new Date(),false,"2","2",new HashMap<>());
		this.elementDao.create(element2);
		
		ElementBoundary elementBoundary1=new ElementBoundary(element1);
		ElementBoundary elementBoundary2=new ElementBoundary(element2);
		
		ElementBoundary[] result = new ElementBoundary[2];
		result[0]=elementBoundary1;
		result[1]=elementBoundary2;
		
		ElementBoundary[] response= this.restTemplate.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}?page={page}&size={size}",
						ElementBoundary[].class,player.getUserSmartspace(),player.getUserEmail(),0,2);
		assertThat(response==null);
		
	}
	@Test
	public void testGetElements() throws Exception{
		// GIVEN the database contains 400 elements
		// AND 3 different roles users
		UserEntity AdminUser = this.factory.createNewUser("admin1@gmail.com", "test", ":)", UserRole.ADMIN, new Long(0));
		UserEntity ManagerUser = this.factory.createNewUser("manager1@gmail.com", "test", ":)", UserRole.MANAGER, new Long(0));
		UserEntity PlayerUser = this.factory.createNewUser("player1@gmail.com", "test", ":)", UserRole.PLAYER, new Long(0));
		this.userDao.create(AdminUser);
		this.userDao.create(ManagerUser);
		this.userDao.create(PlayerUser);
		
		ElementEntity element = this.factory.createNewElement(new Location(0,0), "element", "1", new Date(), false, "1", "1", new HashMap<>());
		this.elementDao.create(element);
		int size = 400;
		IntStream.range(1, size)
		.mapToObj(i->this.factory.createNewElement(new Location(0, 0), ""+i, ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
		.forEach(this.elementDao::create);
		// WHEN I get Elements of the page = 0 and size = 100
		// AND user from type ADMIN
		ElementBoundary[] result = 
			this.restTemplate
			.getForObject(
					this.baseUrl + "/{userSmartspace}/{userEmail}?page={page}&size={size}", 
					ElementBoundary[].class, 
					AdminUser.getUserSmartspace(),AdminUser.getUserEmail(),
					0, 100);
		// THEN I receive 100 elements
		assertThat(result)
			.hasSize(100);
	}
	@Test
	public void testGetElementsWithPaginationOfSecondPage() throws Exception{
		UserEntity admin = this.factory.createNewUser("admin@gmail.com", "Ron", ":-)", UserRole.ADMIN, new Long(0));
		this.userDao.create(admin);
		// GIVEN the database contains 40 elements
		int size = 40;
		IntStream.range(1, size + 1)
		.mapToObj(i->this.factory.createNewElement(new Location(0, 0), ""+i, ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);
		
		// WHEN I getUsers of the page = 1 and size = 100
		ElementBoundary[] result = 
			this.restTemplate
			.getForObject(
					this.baseUrl + "/{userSmartspace}/{userEmail}?page={page}&size={size}", 
					ElementBoundary[].class,admin.getUserSmartspace(),admin.getUserEmail(), 
					1, 100);
		
		// THEN I receive 0 elements
		assertThat(result)
			.isEmpty();
	}

	@Test
	public void testGetElementsWithPaginationOfExistingSecondPage() throws Exception{
		UserEntity admin = this.factory.createNewUser("admin@gmail.com", "Ron", ":-)", UserRole.ADMIN, new Long(0));
		this.userDao.create(admin);
		// GIVEN the database contains 40 elements
		int size = 40;
		IntStream.range(1, size + 1)
		.mapToObj(i->this.factory.createNewElement(new Location(0, 0), ""+i, ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);
		
		// WHEN I getUsers of the page = 1 and size = 30
		ElementBoundary[] result = 
				this.restTemplate
				.getForObject(
						this.baseUrl + "/{userSmartspace}/{userEmail}?page={page}&size={size}", 
						ElementBoundary[].class, admin.getUserSmartspace(),admin.getUserEmail(),
						0, 20);
		// THEN I receive 10 elements
		assertThat(result)
			.hasSize(20);
	}
	
	@Test
	public void testInsertAndGetElementsUsingRest() throws Exception{
		UserEntity manager = this.factory.createNewUser("manager@gmail", "Ron", ":-)", UserRole.MANAGER, new Long(0));
		this.userDao.create(manager);
		// GIVEN the database is empty
		
		// WHEN I POST 3 new users
		// AND I get users of page 0 of size 2
		ElementEntity element1 = this.factory.createNewElement(new Location(1.1, 1.1), "1", "1", new Date(), false, "1", "1", new HashMap<>());
		ElementEntity element2 = this.factory.createNewElement(new Location(2.2, 2.2), "2", "2", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element3 = this.factory.createNewElement(new Location(3.3, 3.3), "3", "3", new Date(), false, "3", "3", new HashMap<>());
		List<ElementBoundary> bounderies = new ArrayList<ElementBoundary>(); 
		ElementBoundary b1 = new ElementBoundary(this.elementDao.create(element1));
		ElementBoundary b2 = new ElementBoundary(this.elementDao.create(element2));
		ElementBoundary b3 = new ElementBoundary(this.elementDao.create(element3));
        bounderies.add(b1);
		bounderies.add(b2);
		bounderies.add(b3);
		ElementBoundary[] getResult
		  = this.restTemplate.getForObject(
					this.baseUrl +"/{userSmartspace}/{userEmail}?page={page}&size={size}",
					ElementBoundary[].class , manager.getUserSmartspace(),manager.getUserEmail(),
					0, 2);
			
		
		// THEN the received users are similar to 2 of the new elements
		assertThat(getResult)
			.hasSize(2)
			.usingElementComparatorOnFields("key")
			.containsExactlyInAnyOrder(b1,b2);
	}
	@Test
	public void testInsertAndGetElementsURLUsingRest() throws Exception{
		UserEntity manager = this.factory.createNewUser("manager@gmail", "Ron", ":-)", UserRole.MANAGER, new Long(0));
		this.userDao.create(manager);
		// GIVEN the database is empty
		
		// WHEN I POST 3 new users
		// AND I get users of page 0 of size 2
		ElementEntity element1 = this.factory.createNewElement(new Location(1.1, 1.1), "1", "1", new Date(), false, "1", "1", new HashMap<>());
		ElementEntity element2 = this.factory.createNewElement(new Location(2.2, 2.2), "2", "2", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element3 = this.factory.createNewElement(new Location(3.3, 3.3), "3", "3", new Date(), false, "3", "3", new HashMap<>());
		List<ElementBoundary> bounderies = new ArrayList<ElementBoundary>(); 
		bounderies.add(new ElementBoundary(this.elementDao.create(element1)));
		bounderies.add(new ElementBoundary(this.elementDao.create(element2)));
		bounderies.add(new ElementBoundary(this.elementDao.create(element3)));

		ElementBoundary result = this.restTemplate.getForObject(this.baseUrl +"/{userSmartspace}/{userEmail}/{elementSmartspace}/{elementId}?page={page}&size={size}", 
				ElementBoundary.class, manager.getUserSmartspace(),manager.getUserEmail(),
				element1.getElementSmartspace(),element1.getElementId(), 
				2, 0 );
			
		
		// THEN the received users are similar to element1
		assertThat(result.equals(new ElementBoundary(element1)));
	}
	@Test
	public void testGetElementByLocationUsingRest() throws Exception{
		//GIVEN the distance is 1
	
		
		ElementEntity element1 = this.factory.createNewElement(new Location(1.0, 1.0), "1", "1", new Date(), false, "1", "1", new HashMap<>());
		ElementEntity element2 = this.factory.createNewElement(new Location(2.0, 2.0), "2", "2", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element3 = this.factory.createNewElement(new Location(3.0,3.0), "3", "3", new Date(), false, "3", "3", new HashMap<>());
		ElementEntity element4 = this.factory.createNewElement(new Location(4.0,4.0), "4", "4", new Date(), false, "4", "4", new HashMap<>());
		
		this.elementDao.create(element1);
		this.elementDao.create(element2);
		this.elementDao.create(element3);
		this.elementDao.create(element4);
		
		List<ElementEntity> list =this.elementDao.readAllByLocationBetween(element1.getLocation(), element4.getLocation(), 1, 1);

		assertThat(list)
		.hasSize(1);
		
		
	}
	@Test
	public void testGetElementByNameUsingRest() throws Exception{
		//GIVEN the distance is 1
	
		
		ElementEntity element1 = this.factory.createNewElement(new Location(2.2, 2.2), "name", "2", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element2 = this.factory.createNewElement(new Location(2.2, 2.2), "name", "2", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element3 = this.factory.createNewElement(new Location(3.3, 3.3), "3", "3", new Date(), false, "3", "3", new HashMap<>());
		ElementEntity element4 = this.factory.createNewElement(new Location(33.3, 33.3), "name", "4", new Date(), false, "4", "4", new HashMap<>());
		
		this.elementDao.create(element1);
		this.elementDao.create(element2);
		this.elementDao.create(element3);
		this.elementDao.create(element4);
		
		List<ElementEntity> list =this.elementDao.readAllByName("name", 1, 2);

		assertThat(list)
		.hasSize(1);
		
		
	}
	@Test
	public void testGetElementByTypeUsingRest() throws Exception{
		//GIVEN the distance is 1
	
		
		ElementEntity element1 = this.factory.createNewElement(new Location(1.1, 1.1), "1", "type", new Date(), false, "1", "1", new HashMap<>());
		ElementEntity element2 = this.factory.createNewElement(new Location(2.2, 2.2), "1", "type", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element3 = this.factory.createNewElement(new Location(3.3, 3.3), "3", "3", new Date(), false, "3", "3", new HashMap<>());
		ElementEntity element4 = this.factory.createNewElement(new Location(33.3, 33.3), "1", "type", new Date(), false, "4", "4", new HashMap<>());
		
		this.elementDao.create(element1);
		this.elementDao.create(element2);
		this.elementDao.create(element3);
		this.elementDao.create(element4);
		
		List<ElementEntity> list =this.elementDao.readAllByTypeLike("type", 1, 2);

		assertThat(list)
		.hasSize(1);
	}
	@Test
	public void testDeleteElementByKeyUsingRest() throws Exception{
		//GIVEN the distance is 1
	
		
		ElementEntity element1 = this.factory.createNewElement(new Location(1.1, 1.1), "1", "1", new Date(), false, "1", "1", new HashMap<>());
		ElementEntity element2 = this.factory.createNewElement(new Location(2.2, 2.2), "2", "1", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element3 = this.factory.createNewElement(new Location(3.3, 3.3), "3", "3", new Date(), false, "3", "3", new HashMap<>());
		ElementEntity element4 = this.factory.createNewElement(new Location(33.3, 33.3), "4", "4", new Date(), false, "4", "4", new HashMap<>());
		
		ElementEntity e1 = this.elementDao.create(element1);
		this.elementDao.create(element2);
		this.elementDao.create(element3);
		this.elementDao.create(element4);
		List<ElementEntity> list = this.elementDao.readAll();
		assertThat(list)
		.hasSize(4);

		this.elementDao.deleteByKey(e1.getKey());
		 list = this.elementDao.readAll();

		assertThat(list)
		.hasSize(3);
	}
	@Test
	public void testGetElementByIdUsingRest() throws Exception{
		//GIVEN the distance is 1
	
		
		ElementEntity element1 = this.factory.createNewElement(new Location(1.1, 1.1), "1", "1", new Date(), false, "1", "1", new HashMap<>());
		ElementEntity element2 = this.factory.createNewElement(new Location(2.2, 2.2), "2", "2", new Date(), false, "2", "2", new HashMap<>());
		ElementEntity element3 = this.factory.createNewElement(new Location(3.3, 3.3), "3", "3", new Date(), false, "3", "3", new HashMap<>());
		ElementEntity element4 = this.factory.createNewElement(new Location(33.3, 33.3), "4", "4", new Date(), false, "4", "4", new HashMap<>());
		
		this.elementDao.create(element1);
		this.elementDao.create(element2);
		this.elementDao.create(element3);
		this.elementDao.create(element4);
		
		List<ElementEntity> list =this.elementDao.readByElementElementId(element3.getKey(), 1, 1);

		assertThat(list).hasSize(0);
	}
	@Test
	public void testPutWithValidKey() throws Exception {
		UserEntity user=this.userDao.create(this.factory.createNewUser("user2@gmail.com", "adminUser", ":)", UserRole.ADMIN, new Long(1)));
		// GIVEN the database contains a message
		Location location = new Location(0,0);
		ElementEntity entity= this.factory.createNewElement(location, "1", "1",new Date(), true, "1", "1", new HashMap<>());
		ElementEntity updateElement = this.elementDao.create(entity);
		ElementBoundary original = new ElementBoundary(updateElement);
		// WHEN we PUT an update for this messages
		
		original.setExpired("false");
		original.setName("2");
		Date date = new Date();
		original.setCreationTimestamp(date);
		this.restTemplate
			.put(this.baseUrl+"/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
					original,user.getUserSmartspace(),user.getUserEmail(),original.getElementSmartspace(),original.getElementId());
		// THEN the message is updated in the db
		String theStringKey = original.getKey(); 
		String[] args =theStringKey.split("#");
		
		String elementId="",elementSmartspace="";
		if (args.length == 2) {
			elementId=args[0];
			elementSmartspace=args[1];
		}
		assertThat(this.elementDao.readByKey(theStringKey))
			.isNotNull()
			.extracting("elementSmartspace","elementId","location","name","type","creationTimestamp","Expired","creatorSmartspace","creatorEmail","moreAttributes")
			.containsExactly(elementSmartspace, elementId, location,"2","1",date,false,"1","1", new HashMap<>());
	}
	@Test
	public void testDeleteElementWithInvalidKey() throws Exception {
		// GIVEN the database is empty
		
		// WHEN we delete a element with id = 5
		this.restTemplate.delete(this.baseUrl+"/{key}", "5");
		// THEN the database remains of the original size
		assertThat(this.elementDao.readAll())
			.isEmpty();
			
	}

	
}

