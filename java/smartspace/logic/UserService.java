package smartspace.logic;

import java.util.List;

import smartspace.data.UserEntity;
public interface UserService {
	public UserEntity insert (UserEntity message);
	public List<UserEntity> getUsers (int size, int page);
	public List<UserEntity> getUsersByContent (String pattern, int size, int page);
	public void update(String key, UserEntity entity) throws UserNotFoundException;
	public void deleteOneUserByKey(String key);
	public UserEntity getUserByKey(String managerKey);

}
