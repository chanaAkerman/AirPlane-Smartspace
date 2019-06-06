package smartspace;

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
import smartspace.dao.AnhancedUserDao;
import smartspace.data.UserEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties={"spring.profiles.active=default"})
public class UserPaginationTests {
	private AnhancedUserDao<String> userDao;
	private EntityFactory factory;
	
	@Autowired
	public void setMessageDao(AnhancedUserDao<String> userDao) {
		this.userDao = userDao;
	}
	
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@After
	public void setup() {
		this.userDao.deleteAll();
	}
	
	@Test
	public void readAllUsersWithPaginationTest() throws Exception{
		// GIVEN the database contains 20 users
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewUser(
				"" + i, ""+i, "" +i,null,new Long (0L)))
		.forEach(this.userDao::create);

			
			
		// WHEN I get a page of 3 after skipping first two pages 
		int size = 3;
		int page = 2;
		List<UserEntity> page2Results = 
			this.userDao.readAll(size, page);
		
		// THEN the result contains 3 results
		assertThat(page2Results)
			.hasSize(3);
	}
	@Test
	public void testGetAllUsersWithPaginationAndSorting() throws Exception{
		// GIVEN the database contains 10 messages
		IntStream.range(0, 9)
		.mapToObj(i->this.factory.createNewUser(
				"" + i, ""+i, "" +i,null,new Long (0L)))
		.forEach(this.userDao::create);

		// WHEN I get a page of 3 after skipping first two pages 
		int size = 3;
		int page = 2;
		List<UserEntity> page2Results = 
			this.userDao.readAll("userName", size, page);
		
		// THEN the result contains specific 3 results
		assertThat(page2Results
				.stream()
				.map(UserEntity::getUserName)
				.collect(Collectors.toList()))
			.containsExactly("6", "7", "8");
	}
	@Test
	public void testGetByNamePatternWithPagination() throws Exception{
		// GIVEN the database contains 20 users with the text "abc"
		// AND the database contains 3 users with the text "xyz"
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewUser(
				"" + i, "abc", "" +i,null,new Long (0L)))
		.forEach(this.userDao::create);

		IntStream.range(20, 23)
		.mapToObj(i->this.factory.createNewUser(
				"" + i, "xyz", "" +i,null,new Long (0L)))
		.forEach(this.userDao::create);

			
		// WHEN I get a page of 3 with no skipping with the text "xyz"
		int size = 3;
		int page = 0;
		String text = "xyz";
		List<UserEntity> results = 
			this.userDao.readUserContainingText(
					text, size, page);
		// THEN the result contains 3 results
		assertThat(results)
			.hasSize(3);
	}
	@Test
	public void testGetByNamePatternWithPaginationAndSkipping() throws Exception{
		// GIVEN the database contains 20 users with the text "abc"
		// AND the database contains 4 users with the text "xyz"
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewUser(
				"" + i, "abc", "" +i,null,new Long (0L)))
		.forEach(this.userDao::create);

		IntStream.range(20, 24)
		.mapToObj(i->this.factory.createNewUser(
				"" + i, "xyz", "" +i,null,new Long (0L)))
		.forEach(this.userDao::create);

			
		// WHEN I get a page of 3 with skipping first page with the text "xyz"
		int size = 3;
		int page = 1;
		String text = "xyz";
		List<UserEntity> results = 
			this.userDao.readUserContainingText(
					text, "userEmail", size, page);
		
		// THEN the result contains 1 results
		assertThat(results)
			.hasSize(1);
	}
}