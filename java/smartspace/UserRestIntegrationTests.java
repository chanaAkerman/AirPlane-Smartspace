package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
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

import smartspace.dao.AnhancedUserDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;
import smartspace.logic.UserServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties="spring.profiles.active=default")
public class UserRestIntegrationTests {
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
		this.baseUrl = "http://localhost:" + port + "/smartspace/users"; 
		this.restTemplate = new RestTemplate();
		this.factory = new EntityFactoryImpl();
	}
	@Before
	public void setUp() {
		this.userDao.deleteAll();
	}
	//@After
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
	public void putPostGetTest() {
		// GIVEN the database contains one manager User
		UserEntity user1 = this.factory.createNewUser("2", "2", ":-0", UserRole.MANAGER, new Long(0));
		this.userDao.create(user1);
		
		UserEntity user2 = this.factory.createNewUser("3", "3", ":-0", UserRole.ADMIN, new Long(0));
		UserBoundary newUser = restTemplate.postForObject(baseUrl,
				new UserBoundary(user2.getKey(), user2.getUserName(), user2.getAvatar(), user2.getRole().toString(), user2.getPoints()),
				UserBoundary.class);

		UserBoundary response= this.restTemplate.getForObject(
				this.baseUrl+"/login/{userSmartspace}/{userEmail}",
				UserBoundary.class,user2.getUserSmartspace(),user2.getUserEmail());
		//assertThat(user.equals(response.toEntity()));
		
		response.setPoints(new Long(10));
		
		this.restTemplate.put(
				this.baseUrl+"/login/{userSmartspace}/{userEmail}", 
				response,response.getUserSmartspace(),response.getUserEmail());
		Long updatePoints = new Long(10);
		assertThat(response.getPoints()==updatePoints);
		assertThat(this.userDao.readAll()).hasSize(2).containsExactly(user1,response.toEntity());
	}
	@Test
	public void insertNewUserTest() {
		UserEntity user = this.factory.createNewUser("2019B.chana", "chana", ":", UserRole.MANAGER, new Long(0));
		UserBoundary newUser = restTemplate.postForObject(baseUrl,
				new UserBoundary(user.getKey(), user.getUserName(), user.getAvatar(), user.getRole().toString(), user.getPoints()),
				UserBoundary.class);
		assertThat(this.userDao.findUserByKey(newUser.getKey())!=null);
	}
	@Test
	public void testDeleteUesrWithInvalidKey() throws Exception {
		// GIVEN the database is empty
		this.userDao.deleteAll();
		// WHEN we delete a user with id = 5
		this.restTemplate.delete(this.baseUrl+"/{key}", "5");
		// THEN the database remains of the original size
		assertThat(this.userDao.readAll())
			.isEmpty();		
	}
}








