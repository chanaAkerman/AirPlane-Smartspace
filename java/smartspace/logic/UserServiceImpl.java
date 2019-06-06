package smartspace.logic;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.AnhancedUserDao;
import smartspace.dao.UserDao;
import smartspace.data.UserEntity;

@Service
public class UserServiceImpl implements UserService {
	private AnhancedUserDao<String> users;
	@Autowired
	public UserServiceImpl(AnhancedUserDao<String> users) {
		super();
		this.users=users;
	}
	@Override
	@Transactional
	public UserEntity insert(UserEntity user) {
		if (validate(user)) {
			return this.users.create(user);
		}else {
			throw new RuntimeException("invalid user");
		}
	}
	private boolean validate(UserEntity user) {
		return user != null &&
				user.getUserSmartspace()!=null&&
				!user.getUserSmartspace().trim().isEmpty()&&
				user.getUserEmail()!=null&&
				!user.getUserEmail().trim().isEmpty()&&
				user.getUserName()!=null&&
				!user.getUserName().trim().isEmpty()&&
				user.getAvatar()!=null&&
				!user.getAvatar().trim().isEmpty()&&
				user.getRole()!=null&&
				user.getPoints()!=null;

	}

	@Override
	public List<UserEntity> getUsers(int size, int page) {
		return this.users.readAll(size,page);
	}


	@Override
	public List<UserEntity> getUsersByContent(String pattern, int size, int page) {
		return this.users.readUserContainingText(pattern, "availableFrom", size, page);
	}
	@Override
	public void update(String key, UserEntity entity) throws UserNotFoundException {
		entity.setKey(key);
		this.users.update(entity);
		
	}
	@Override
	public void deleteOneUserByKey(String key) {
		UserEntity user = this.users.findUserByKey(key);
		this.users.deleteByKey(user.getIdentifier());		
		
	}
	@Override
	public UserEntity getUserByKey(String key) {
		return this.users.findUserByKey(key);
	}

}
