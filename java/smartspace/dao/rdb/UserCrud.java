package smartspace.dao.rdb;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
@Repository
public interface UserCrud extends PagingAndSortingRepository<UserEntity, String>{
	public List<UserEntity> findAllByUserNameLike(
			@Param("userNamePattern") 
			String userNamePattern,
			Pageable pageable);

	public boolean existsByKey(
			@Param("key") String key);

	public void deleteByKey(
			@Param("key") String key);

	public UserEntity findByKeyLike(
			@Param("key")String key);
}