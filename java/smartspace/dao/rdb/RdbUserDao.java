package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.data.UserEntity;
import smartspace.dao.AnhancedUserDao;
import smartspace.dao.rdb.UserCrud;
@Repository
public class RdbUserDao implements AnhancedUserDao<String> {
	private UserCrud userCrud;
	
	@Autowired
	public RdbUserDao(UserCrud userCrud) {
		super();
		this.userCrud = userCrud;
	}
	@Override
	@Transactional
	public UserEntity create(UserEntity userEntity) {
		// SQL INSERT
				if (!this.userCrud.existsById(userEntity.getKey())) {
					UserEntity rv = this.userCrud.save(userEntity);
					return rv;
				}else {
					throw new RuntimeException("user already exists with id: " + userEntity.getKey());
				}
	}


	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll() {
		// TODO Auto-generated method stub
		List<UserEntity> list = new ArrayList<>();
		// SQL SELECT
		this.userCrud.findAll()
			.forEach(list::add);
		return list;
	}

	@Override
	@Transactional
	public void update(UserEntity userEntity) {
		UserEntity existing = this.readByKey(userEntity.getKey());
		if(existing==null)
				{
					throw new RuntimeException("could not find any user with id: " + userEntity.getKey());
				}
		    if (userEntity.getUserSmartspace() != null) {
			    existing.setUserSmartspace(userEntity.getUserSmartspace());
		    }
		    if (userEntity.getUserEmail() != null) {
			    existing.setUserEmail(userEntity.getUserEmail());
		    }
		    if (userEntity.getUserName() != null) {
			    existing.setUserName(userEntity.getUserName());
		    }
		    if (userEntity.getAvatar() != null) {
			    existing.setAvatar(userEntity.getAvatar());
		    }
		    if (userEntity.getRole() != null) {
			    existing.setRole(userEntity.getRole());
		    }
		    if (userEntity.getPoints() != null) {
			    existing.setPoints(userEntity.getPoints());
		    }

			// SQL UPDATE
			this.userCrud.save(existing);
		
	}
	@Override
	public UserEntity readByKey(String key) {
		return this.userCrud.findByKeyLike(key);
	}
	@Override
	@Transactional
	public void deleteAll() {
		this.userCrud.deleteAll();
		
	}
	@Override
	@Transactional(readOnly=true)
	public Optional<UserEntity> readById(String key) {
		return this.userCrud.findById(key);
	}
	@Override
	@Transactional(readOnly=true)
	public List<UserEntity> readAll(int size, int page) {
		return
			this.userCrud
			.findAll(PageRequest.of(page, size))
			.getContent();
	}
	@Override
	public List<UserEntity> readAll(String sortingAttr, int size, int page) {
			return this.userCrud.findAll(PageRequest.of(page, size,Direction.ASC, sortingAttr)).getContent();
		}
	@Override
	public List<UserEntity> readUserContainingText(String text, int size, int page) {
		return this.userCrud.findAllByUserNameLike(text,PageRequest.of(page, size));
	}
	@Override
	public List<UserEntity> readUserContainingText(String text, String sortAttr, int size, int page) {
		return this.userCrud.findAllByUserNameLike (text,PageRequest.of(page, size, Direction.ASC, sortAttr));
	}
	@Override
	public void deleteByKey(String key) {
		if (this.userCrud.existsByKey(key)) {
			this.userCrud.deleteByKey(key);		
		}
	}
	@Override
	public void delete(UserEntity userEntity) {
		if(userCrud.existsByKey(userEntity.getKey())) {
			userCrud.deleteByKey(userEntity.getKey());
		}else { // Optional
			new RuntimeException("could not delete not existing element: "+userEntity.toString());
		}
	}
	@Override
	public UserEntity findUserByKey(String key) {
		return this.userCrud.findByKeyLike(key);
	}
}

