package smartspace.init;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.ActionDao;
import smartspace.data.ActionEntity;


@Component
@Profile("production")
public class ActionDemoInitializer implements CommandLineRunner{
	private ActionDao actionDao;
	
	@Autowired
	public ActionDemoInitializer(ActionDao actionDao) {
		this.actionDao = actionDao;
	}


	@Override
	public void run(String... args) throws Exception {
		ActionEntity action = new ActionEntity(
				"2019B.chana", "1", "2019B.demo", "4", "2018A.shir", "309", "::)", new Date(), new HashMap<>());
	}
}