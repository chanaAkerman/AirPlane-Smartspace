package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;

public interface AnhancedElementDao<ElementKey> extends ElementDao<ElementKey>{
	public List<ElementEntity> readAll(
			int size, int page);

	public List<ElementEntity> readAll(
			String sortingAttr,
			int size, int page);

	public List<ElementEntity> readElementContainingText (
			String text, 
			int size, int page);

	public List<ElementEntity> readElementContainingText (
			String text, 
			String sortAttr, int size, int page);
	
	public List<ElementEntity> readAllCreationTimestamp(
			Date availableFrom, 
			Date availableTo,
			int size, int page);

	public List<ElementEntity> readAllByLocationBetween(
			Location location1, 
			Location location2,
			int size, int page);

	public List<ElementEntity> readAllByName(
			String name, 
			int size, int page);

	public List<ElementEntity> readAllByTypeLike(
			String type, 
			int size, int page);

	public List<ElementEntity> readByElementElementId(
			String elementId, 
			int size, int page);

	public ElementEntity readByElementId(
			String elementId);
	
	public ElementEntity readByKey (String key);
}
