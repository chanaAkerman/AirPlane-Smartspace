package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import smartspace.dao.UserDao;
import smartspace.data.UserEntity;
//@Repository
public class MemoryUserDao implements UserDao<String>{
	private Map<String,UserEntity> users;
	public MemoryUserDao() {
		super();
		this.users=Collections.synchronizedMap(new HashMap<>());
	}
	@Override
	public UserEntity create(UserEntity userEntity) {
		this.users.put(userEntity.getKey(), userEntity);
		return userEntity;
	}

	@Override
	public Optional<UserEntity> readById(String key) {
		UserEntity output = this.users.get(key);
		if (output != null) {
			return Optional.of(output);
		}else {
			return Optional.empty();
		}
	}
	@Override
	public List<UserEntity> readAll() {
		return new ArrayList<UserEntity>(this.users.values());
	}
	@Override
	public void update(UserEntity userEntity) {
		boolean dirtyFlag = false; 
		UserEntity existing = 
			this.readById(userEntity.getKey()).orElseThrow(
					()->new RuntimeException("could not find any user with id: " + userEntity.getKey())
			);
					/*new Supplier<RuntimeException>() {
						@Override
						public RuntimeException get() {
							return new RuntimeException("could not find any message with id: " + message.getKey());
						}
					});*/
	    if (userEntity.getUserSmartspace() != null) {
		    existing.setUserSmartspace(userEntity.getUserSmartspace());
		    dirtyFlag = true;
	    }
	    if (userEntity.getUserEmail() != null) {
		    existing.setUserEmail(userEntity.getUserEmail());
		    dirtyFlag = true;
	    }
	    if (userEntity.getUserName() != null) {
		    existing.setUserName(userEntity.getUserName());
		    dirtyFlag = true;
	    }
	    if (userEntity.getAvatar() != null) {
		    existing.setAvatar(userEntity.getAvatar());
		    dirtyFlag = true;
	    }
	    if (userEntity.getRole() != null) {
		    existing.setRole(userEntity.getRole());
		    dirtyFlag = true;
	    }
	    if (userEntity.getPoints() != null) {
		    existing.setPoints(userEntity.getPoints());
		    dirtyFlag = true;
	    }
		if (dirtyFlag) {
			this.users.put(existing.getKey(), existing);
		}
		
	}
	@Override
	public void deleteAll() {
		this.users.clear();
	}
	public Map<String,UserEntity> getUsers() {
		return users;
	}
	public void setUsers(Map<String,UserEntity> users) {
		this.users = users;
	}
}
