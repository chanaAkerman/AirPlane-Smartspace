package smartspace.init;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;


@Component
@Profile("production")
public class ElementDemoInitializer implements CommandLineRunner{
	private ElementDao<String> elementDao;
	
	@Autowired
	public ElementDemoInitializer(ElementDao<String> elementDao) {
		this.elementDao = elementDao;
	}
	@Override
	public void run(String... args) throws Exception {
		ElementEntity element = new ElementEntity(
				"2019B.chana", "1", new Location(0,0), "demo", "new", new Date(), false, "creatorDemo", "demo@gmail.com", new HashMap<>());
	}
}