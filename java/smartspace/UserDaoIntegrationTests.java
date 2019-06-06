package smartspace;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import smartspace.dao.AnhancedUserDao;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;



@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(properties= {"spring.profiles.active=default"})
public class UserDaoIntegrationTests {
	private AnhancedUserDao<String> dao;
	private EntityFactory factory;
	public UserDaoIntegrationTests() {
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
	/*@Test
	public void findUserByEmailTest() {
		return this.dao.
	}*/
	@Test
	public void CreateUserTest() throws Exception {
		/// GIVEN a new MemoryElementDao
		
		/// WHEN I create a ElementEntity with all of the required qualities
		UserEntity user= factory.createNewUser("1", "1", "1", null, null); 
		UserEntity storedUser = dao.create(user);	
				
		// Then No exception is thrown
		// And the UserEntity was stored in h2
		// And the returned UserEntity has an id
		assertThat(this.dao.readByKey(storedUser.getKey()))
		.isEqualToComparingFieldByField(storedUser);
	}

	@Autowired
	public void setUserDao(AnhancedUserDao<String> userDao) {
		this.dao = userDao;
	}
	@Test(expected=Exception.class)
	public void CreateNewUserWithEmptyStringQualityTest() throws Exception {
		/// GIVEN a clean database
		
		/// WHEN I create a UserEntity with empty String on required qualities 
		UserEntity user =factory.createNewUser("1", "", "", null, null);
		dao.create(user);
		
		// THEN exception is thrown
		// AND the UserEntity was not created and stored in h2 database
		assertThat(this.dao.readAll())
			.hasSize(0);
	}
	@Test(expected=Exception.class)
	public void CreateNewUserWithNullTest() throws Exception{
		// GIVEN a clean database
		
		// WHEN I create a null user
		this.dao.create(null);
		// THEN No exception is thrown
	}
	@Test
	public void ReadAllUsersTest() { 
		/// GIVEN a clean database

		/// WHEN I create an objects
		UserEntity user1= factory.createNewUser("1", "1", "1", null, null);
		dao.create(user1);
				
		UserEntity user2= factory.createNewUser("2", "2", "2", null, null); 
		dao.create(user2);
		
        // THEN i can read all the objects
		// AND No exception is thrown
		assertThat(dao.readAll())
			.isNotEmpty()
			.hasSize(2)
		    .containsExactlyInAnyOrder(user1,user2);
	}
	@Test
	public void DeleteAllUsersTest() { 
		/// GIVEN a clean database

		/// WHEN I create objects and delete all of them
		UserEntity user1= factory.createNewUser("1", "1", "1", null, null);
		dao.create(user1);
				
		UserEntity user2= this.factory.createNewUser("2", "2", "2", null, null); 
		dao.create(user2);
		
		assertThat(dao.readAll())
			.isNotEmpty()
			.hasSize(2)
			.containsExactlyInAnyOrder(user1,user2);
			    
		// AND No exception is thrown
		// AND there is nothing in the h2 Database.
		dao.deleteAll();
				
		assertThat(dao.readAll()==null);
		
	}
	@Test
	public void UpdateUserTest() {
		// GIVEN a clean database
		
		// WHEN I Update User
		String smartSpace = "Test";
		UserEntity user= this.factory.createNewUser("1", "1", "1", null, null); 
		UserEntity insertedUser = dao.create(user);
				
		assertThat(dao.readAll())
			.hasSize(1);
		
		UserEntity update = new UserEntity();
		
		update.setKey(user.getKey());
		update.setUserName(smartSpace);
		update.setKey(user.getKey());
		
		this.dao.update(update);
				
		//THEN the specific user has been updated.
		insertedUser = this.dao.readByKey(insertedUser.getKey());
		if(insertedUser==null)
			throw new RuntimeException("User is not available after update");
		assertThat(dao.readAll())
			.hasSize(1);
		
	}
	@Test(expected=Exception.class)
	public void UpdateNotExistingUserTest() {
		// Given a clean database
		
		// WHEN I Update not existing User
		String smartSpace = "Test";
		UserEntity user= this.factory.createNewUser("1", "1", "1", null, null);

		UserEntity update = new UserEntity();
		
		update.setKey(user.getKey());
		update.setUserEmail("number2@gmail.com");
		update.setUserName(smartSpace);
		update.setKey(user.getKey());
				
		//THEN exception is thrown
		this.dao.update(update);
		
	}

}
