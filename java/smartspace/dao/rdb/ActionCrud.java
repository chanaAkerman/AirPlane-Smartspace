package smartspace.dao.rdb;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import smartspace.data.ActionEntity;

public interface ActionCrud extends PagingAndSortingRepository<ActionEntity, String>{
	public List<ActionEntity> findAllByActionTypeLike(
			@Param("ActionTypePattern") 
			String actionType,
			Pageable pageable);

	public List<ActionEntity> 
		findAllByCreationTimestampBetween(
				@Param("creationTimestampFrom")Date creationTimestampFrom, 
				@Param("creationTimestampTo")Date creationTimestampTo,  
				Pageable pageable);
	
	public boolean existsByKey(
			@Param("key") String key);

	public void deleteByKey(
			@Param("key") String key);
	
	public ActionEntity findByKeyLike(
			@Param("key") String key);

}
