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
import smartspace.dao.AnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties={"spring.profiles.active=default"})
public class ElementPaginationTests {
	private AnhancedElementDao<String> elementDao;
	private EntityFactory factory;
	
	@Autowired
	public void setMessageDao(AnhancedElementDao<String> userDao) {
		this.elementDao = userDao;
	}
	
	@Autowired
	public void setFactory(EntityFactory factory) {
		this.factory = factory;
	}
	
	@After
	public void setup() {
		this.elementDao.deleteAll();
	}
	
	@Test
	public void readAllElemetsWithPaginationTest() throws Exception{
		// GIVEN the database contains 20 elements
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), ""+i, ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);
		
		// WHEN I get a page of 3 after skipping first two pages 
		int size = 3;
		int page = 2;
		List<ElementEntity> page2Results = 
			this.elementDao.readAll(size, page);
		
		// THEN the result contains 3 results
		assertThat(page2Results)
			.hasSize(3);
	}
	@Test
	public void testGetAllElementsWithPaginationAndSorting() throws Exception{
		// GIVEN the database contains 10 elements
		IntStream.range(0, 9)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), ""+i, ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);

		// WHEN I get a page of 3 after skipping first two pages 
		int size = 3;
		int page = 2;
		List<ElementEntity> page2Results = 
			this.elementDao.readAll("name", size, page);
		
		// THEN the result contains specific 3 results
		assertThat(page2Results
				.stream()
				.map(ElementEntity::getName)
				.collect(Collectors.toList()))
			.containsExactly("6", "7", "8");
	}
	@Test
	public void testGetByNamePatternWithPagination() throws Exception{
		// GIVEN the database contains 20 elements with the text "abc"
		// AND the database contains 3 elements with the text "xyz"
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), "abc", ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);

		IntStream.range(20, 23)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), "xyz", ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);

			
		// WHEN I get a page of 3 with no skipping with the text "xyz"
		int size = 3;
		int page = 0;
		String text = "xyz";
		List<ElementEntity> results = 
			this.elementDao.readElementContainingText(
					text, size, page);
		// THEN the result contains 3 results
		assertThat(results)
			.hasSize(3);
	}
	@Test
	public void testGetByNamePatternWithPaginationAndSkipping() throws Exception{
		// GIVEN the database contains 20 elements with the text "abc"
		// AND the database contains 4 elements with the text "xyz"
		IntStream.range(0, 20)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), "abc", ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);

		IntStream.range(20, 24)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), "xyz", ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);

			
		// WHEN I get a page of 3 with skipping first page with the text "xyz"
		int size = 3;
		int page = 1;
		String text = "xyz";
		List<ElementEntity> results = 
			this.elementDao.readElementContainingText(
					text, "name", size, page);
		
		// THEN the result contains 1 results
		assertThat(results)
			.hasSize(1);
	}
	@Test
	public void testGetDateWithPagination() throws Exception{
		// GIVEN the database contains 17 elements from today
		// AND the database contains 3 are in the date range of two days ago to yesterday
		IntStream.range(0, 17)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), "abc", ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
        .forEach(this.elementDao::create);
		
		Date twentyEightHoursAgo = new Date(System.currentTimeMillis() - 28*3600*1000); 
		
		IntStream.range(17, 20)
		.mapToObj(i->this.factory.createNewElement(
				new Location(0, 0), "xyz", ""+i, new Date(), false, ""+i, ""+i, new HashMap<>()))
		.peek(el->el.setCreationTimestamp(twentyEightHoursAgo))
		.forEach(this.elementDao::create);

			
		// WHEN I get a page of 5 with skipping no pages with elements from two days ago and to last day
		int size = 5;
		int page = 0;
		Date twoDaysAgo = new Date(System.currentTimeMillis() - 1000*3600*48);
		Date yesterday = new Date(System.currentTimeMillis() - 1000*3600*24);
		List<ElementEntity> results = this.elementDao.readAllCreationTimestamp(twoDaysAgo, yesterday, size, page);
		
		// THEN the result contains 3 results
		assertThat(results)
			.hasSize(3);
	}
	
}
