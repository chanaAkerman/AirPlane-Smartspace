package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.UserEntity;

public interface AnhancedUserDao<UserKey> extends UserDao<UserKey>{
	public List<UserEntity> readAll(
			int size, int page);

	public List<UserEntity> readAll(
			String sortingAttr,
			int size, int page);

	public List<UserEntity> readUserContainingText (
			String text, 
			int size, int page);

	public List<UserEntity> readUserContainingText (
			String text, 
			String sortAttr, int size, int page);

	public void deleteByKey(String key);

	public UserEntity findUserByKey(String key);
	
	public UserEntity readByKey (UserKey key);
	
	void delete(UserEntity userEntity);
}
