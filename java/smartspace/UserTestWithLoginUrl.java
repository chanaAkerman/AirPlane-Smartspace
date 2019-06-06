package smartspace;

import static org.assertj.core.api.Assertions.assertThat;
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

import smartspace.dao.AnhancedUserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.layout.UserBoundary;
import smartspace.logic.UserNotFoundException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class UserTestWithLoginUrl {
	@LocalServerPort
	private int port;
	
	private String baseUrl;
	private RestTemplate restTemplate;
	
	private AnhancedUserDao<String> userDao;
	private EntityFactory factory;
	
	@Autowired
	public void setUserDao(AnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@PostConstruct
	public void init() {
		this.baseUrl = "http://localhost:" + port + "/smartspace/users/login"; 
		this.restTemplate = new RestTemplate();
		this.factory = new EntityFactoryImpl();
	}
	
	@Before
	public void setUp() {
		this.userDao.deleteAll();
	}
	@After
	public void teardown() {
		this.userDao.deleteAll();
	}
	@Autowired
	public EntityFactory getFacotry() {
		return factory;
	}

	public void setFacotry(EntityFactory factory) {
		this.factory = factory;
	}
	@Test
	public void testGet1Users() throws Exception{
		UserEntity user1 = this.factory.createNewUser("1", "1", "1", UserRole.PLAYER, new Long(1));
		this.userDao.create(user1);		
		UserBoundary[] response= this.restTemplate.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}/{pattern}",
						UserBoundary[].class,user1.getUserSmartspace(),user1.getUserEmail(),user1.getUserEmail());
		assertThat(response).hasSize(1);
	}
	@Test
	public void testGet1UsersWithOther() throws Exception{
		UserEntity user1 = this.factory.createNewUser("1", "1", "1", UserRole.PLAYER, new Long(1));
		this.userDao.create(user1);		
	UserEntity user2 = this.factory.createNewUser("2", "2", "2", UserRole.PLAYER, new Long(1));
	this.userDao.create(user2);	
	UserEntity user3 = this.factory.createNewUser("3", "3", "3", UserRole.PLAYER, new Long(1));
	this.userDao.create(user3);	
	UserBoundary[] response= this.restTemplate.getForObject(
					this.baseUrl+"/{userSmartspace}/{userEmail}/{pattern}",
					UserBoundary[].class,user1.getUserSmartspace(),user1.getUserEmail(),user1.getUserEmail());
	assertThat(response).hasSize(1);
	}
	/*@Test
	public void testGet2UsersWithPagination() throws Exception{
		UserEntity user1 = this.factory.createNewUser("1", "1", "1", UserRole.PLAYER, new Long(1));
		this.userDao.create(user1);
		
		UserEntity user2 = this.factory.createNewUser("2", "2", "2", UserRole.PLAYER, new Long(2));
		this.userDao.create(user2);
		
		UserBoundary userBoundary1=new UserBoundary(user1);
		UserBoundary userBoundary2=new UserBoundary(user2);
		
		UserBoundary[] result = new UserBoundary[2];
		result[0]=userBoundary1;
		result[1]=userBoundary2;
		
		UserBoundary[] response= this.restTemplate.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}/{pattern}",
						UserBoundary[].class,user1.getUserSmartspace(),user1.getUserEmail(),user1.getUserEmail());
		assertThat(response)
		.hasSize(2);
	}*/
	@Test
	public void testGetByNULLUser() throws Exception{
		UserEntity user = this.factory.createNewUser("1", "1", "1", UserRole.PLAYER, new Long(1));
		this.userDao.create(user);
		
		UserBoundary userBoundary=new UserBoundary(user);
		
		UserBoundary[] result = new UserBoundary[2];
		result[0]=userBoundary;
		
		UserBoundary[] response= this.restTemplate.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}",
						UserBoundary[].class,"3","4"); //Invalid key
		assertThat(response==null);
	}
	@Test
	public void testPutUserWithValidId() throws Exception {
		// GIVEN the database contains a message
		UserEntity entity= this.factory.createNewUser("1", "1", "1", UserRole.ADMIN, new Long(0));
		UserEntity updateUser = this.userDao.create(entity);
		UserBoundary original = new UserBoundary(updateUser);
		
		// WHEN we PUT an update for this messages
		original.setAvatar("3");
		original.setRole(UserRole.MANAGER+"");
		this.restTemplate
		.put(this.baseUrl+"/{userSmartspace}/{userEmail}", original,original.getUserSmartspace(),original.getUserEmail());
		// THEN the message is updated in the db
		String theStringKey = original.getKey(); 
		
		String[] args =theStringKey.split("#");
		
		String userEmail="",elementSmartspace="";
		if (args.length == 2) {
			elementSmartspace=args[0];
			userEmail=args[1];
		}
		
		assertThat(this.userDao.readByKey(theStringKey))
			.extracting("userSmartspace","userEmail","userName","avatar","role","points")
			.containsExactly(elementSmartspace, userEmail, "1","3",UserRole.MANAGER,Long.parseLong("0",10) );
	}
	@Test
	public void testGetUsersWithPagination() throws Exception{
		UserEntity user1 = this.factory.createNewUser("1", "1", "1", UserRole.MANAGER, new Long(1));
		this.userDao.create(user1);
		
		UserEntity user2 = this.factory.createNewUser("2", "2", "2", UserRole.PLAYER, new Long(2));
		this.userDao.create(user2);

		UserBoundary result = 
				this.restTemplate
				.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}",
						UserBoundary.class,user1.getUserSmartspace(),user1.getUserEmail());
		assertThat(result.getKey()==user1.getKey());
		assertThat(result.getRole().equals(user1.getRole().toString()));
		
	}
	/*@Test
	public void testGetUsersWithPaginationOfExistingSecondPage() throws Exception{
		UserEntity admin=this.factory.createNewUser("admin@gmail.com", "Ron", ":)", UserRole.ADMIN, new Long(0));
		this.userDao.create(admin);
		// GIVEN the database contains 40 elements
		int size = 40;
		IntStream.range(1, size)
		.mapToObj(i->this.factory.createNewUser(i+"", ""+i,""+i, UserRole.PLAYER, new Long(i)))
        .forEach(this.userDao::create);
		
		// WHEN I getUsers of the page = 1 and size = 30
		UserBoundary[] result = 
				this.restTemplate
				.getForObject(
						this.baseUrl + "/{userSmartspace}/{userEmail}/{pattern}?page={page}&size={size}", 
						UserBoundary[].class,admin.getUserSmartspace(),admin.getUserEmail(), admin.getUserEmail(),
						0, 20);
		// THEN I receive 10 users
		assertThat(result)
			.hasSize(20);
	}
	@Test
	public void testGetManyUsersWithPagination() throws Exception{
		UserEntity admin=this.factory.createNewUser("admin@gmail.com", "Ron", ":)", UserRole.ADMIN, new Long(0));
		this.userDao.create(admin);
		// GIVEN the database contains 400 users
		int size = 400;
		
		// TO DO if we change to another number the test fail.
		
		IntStream.range(1, size)
			.mapToObj(i->this.factory.createNewUser(""+i, ""+i,""+i, UserRole.PLAYER, new Long(i)))
			.forEach(this.userDao::create);
		// WHEN I get Users of the page = 0 and size = 100
		UserBoundary[] result = 
			this.restTemplate
			.getForObject(
					this.baseUrl+"/{userSmartspace}/{userEmail}/{pattern}?page={page}&size={size}", 
					UserBoundary[].class, admin.getUserSmartspace(),admin.getUserEmail(),admin.getUserEmail(),
					0, 100);
		// THEN I receive 100 users
		assertThat(result)
			.hasSize(100);
	}*/
	@Test
	public void testGetUsersByPattern() throws Exception{
		UserEntity admin=this.factory.createNewUser("admin@gmail.com", "Ron", ":)", UserRole.ADMIN, new Long(0));
		this.userDao.create(admin);
		// GIVEN the database contains 400 users
		int size = 400;
		
		// TO DO if we change to another number the test fail.
		
		IntStream.range(1, size)
			.mapToObj(i->this.factory.createNewUser(""+i, ""+i,""+1, UserRole.PLAYER, new Long(i)))
			.forEach(this.userDao::create);
		// WHEN I get Users of the page = 0 and size = 100
		UserBoundary[] result = 
			this.restTemplate
			.getForObject(
					this.baseUrl+"/{userSmartspace}/{userEmail}/{pattern}?page={page}&size={size}", 
					UserBoundary[].class, admin.getUserSmartspace(),admin.getUserEmail(),"1",
					0, 100);
		// THEN I receive 100 users
		assertThat(result)
			.hasSize(100);
	}
	@Test
	public void testGsetUsersByPattern2() throws Exception{
		UserEntity user1 = this.factory.createNewUser("1", "1", "1", UserRole.PLAYER, new Long(1));
		//UserEntity user1=new UserEntity("1","1","1","1",null,null);
		this.userDao.create(user1);
		
		UserEntity user2 = this.factory.createNewUser("2", "2", "2", UserRole.PLAYER, new Long(2));
		//UserEntity user2=new UserEntity("2","2","2","2",null,null);
		this.userDao.create(user2);

		UserBoundary[] result = 
				this.restTemplate
				.getForObject(
						this.baseUrl+"/{userSmartspace}/{userEmail}/{pattern}",
						UserBoundary[].class,user1.getUserSmartspace(),user1.getUserEmail(),"12");
		assertThat(result)
		.hasSize(0);
		
	}

}
