package smartspace.dao.rdb;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
@Repository
public interface ElementCrud extends PagingAndSortingRepository<ElementEntity, String>{
	public List<ElementEntity> findAllByNameLike(
			@Param("NamePattern") 
			String name,
			Pageable pageable);

	public List<ElementEntity> 
	findAllByCreationTimestampBetween(
				@Param("creationTimestampFrom")Date creationTimestampFrom, 
				@Param("creationTimestampTo")Date creationTimestampTo, 
				Pageable pageable);
	
	public List<ElementEntity> 
	findAllByLocationBetween(
				@Param("locationFrom") Location locationForm, 
				@Param("locationTo") Location locationTo, 
				Pageable pageable);

	public List<ElementEntity> 
	findAllByTypeLike(
			@Param("type") String type, 
			Pageable pageable);

	public List<ElementEntity>
	findByElementIdLike(
			@Param("elementId") String elementId,
			Pageable pageable);
	
	public ElementEntity findByKey(
			@Param("key")String key);
	
	public boolean existsByKey(
			@Param("key") String key);
	
	public void deleteByKey(
			@Param("key") String key);

	public ElementEntity findByElementId(
			@Param("elementId") String elementId);
}

