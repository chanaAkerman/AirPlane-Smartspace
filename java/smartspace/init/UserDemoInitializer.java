package smartspace.init;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;


@Component
@Profile("production")
public class UserDemoInitializer implements CommandLineRunner{
	private UserDao<String> userDao;
	
	@Autowired
	public UserDemoInitializer(UserDao<String> userDao) {
		this.userDao = userDao;
	}


	@Override
	public void run(String... args) throws Exception {
		UserEntity user = new UserEntity("2019B.chana", "demo@gmail.com", "demo", ":)" ,UserRole.ADMIN, new Long(0));
	}
}
