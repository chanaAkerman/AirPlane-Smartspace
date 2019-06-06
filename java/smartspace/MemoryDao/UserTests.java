package smartspace.MemoryDao;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import smartspace.dao.memory.MemoryUserDao;
import smartspace.data.UserEntity;

public class UserTests {
	private MemoryUserDao dao;
	
	@Before
	public void setup() {
		dao=new MemoryUserDao();	
	}
	@After
	public void teardown() {
		this.dao=null;
	}
	
	@Test
	public void CreateNewUserTest() throws Exception {
		/// GIVEN a new MemoryElementDao
		
		/// WHEN I create a ElementEntity with all of the required qualities
		UserEntity user= new UserEntity("1", "1", "1", "1", null, null); 
		UserEntity storedUser = dao.create(user);
				
				
		// Then No exception is thrown
		// And the UserEntity was stored to the Dao
		// And the Dao size was increased by 1
		// And the returned UserEntity has an id
		assertThat(dao.getUsers().values())
			.usingElementComparator((u1, u2)->u1.getKey().compareTo(u2.getKey()))
			.contains(user);
			
		assertThat(dao.getUsers())
			.hasSize(1);
			
		assertThat(storedUser.getKey())
			.isNotNull();
	}
	@Test
	public void DeleteAllUsersReadAllTest() { 
		/// GIVEN a new MemoryUserDao
		
		/// WHEN I delete all objects
		UserEntity user1= new UserEntity("1", "1", "1", "1", null, null); 
		dao.create(user1);
				
		UserEntity user2= new UserEntity("2", "2", "2", "2", null, null); 
		dao.create(user2);

		// THEN You can read all the objects
		assertThat(this.dao.readAll())
			.containsExactlyInAnyOrder(user1,user2)
			.isNotEmpty();
			    
		// AND No exception is thrown
		// AND the Dao size dropped to 0
		dao.deleteAll();
				
		assertThat(dao.getUsers())
			.hasSize(0);
		
	}
	@Test(expected=Exception.class)
	public void testCreateWithNullUser() throws Exception{
		// Given the dao is initialized
		
		// When I create a null message
		this.dao.create(null);
	}
	@Test
	public void UpdateUserTest() {
		// GIVEN we have a MemoryUserDao
		
		// WHEN I Update User
		String smartSpace = "Test";
		UserEntity user= new UserEntity( "1","1", "1", "1", null, null); 
		UserEntity insertedUser = dao.create(user);
				
		assertThat(dao.getUsers())
			.hasSize(1);
		
		UserEntity update = new UserEntity();
		
		update.setKey(user.getKey());
		update.setUserEmail("number2@gmail.com");
		update.setUserName(smartSpace);
		update.setKey(user.getKey());

		
		this.dao.update(update);
				
		//THEN the specific user has been updated.
		// AND the Dao size has not changed
		insertedUser = this.dao.readById(insertedUser.getKey())
				.orElseThrow(()->new RuntimeException("User is not available after update"));
		assertThat(dao.getUsers())
			.hasSize(1);
		
	}

}
