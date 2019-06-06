package smartspace.dao;

import java.util.Date;
import java.util.List;

import smartspace.data.ActionEntity;


public interface AnhancedActionDao<ActionKey> extends ActionDao{
	public List<ActionEntity> readAll(
			int size, int page);

	public List<ActionEntity> readAll(
			String sortingAttr,
			int size, int page);

	public List<ActionEntity> readActionContainingText (
			String text, 
			int size, int page);

	public List<ActionEntity> readActionContainingText (
			String text, 
			String sortAttr, int size, int page);
	
	public List<ActionEntity> readAllCreationTimestamp(
			Date availableFrom, 
			Date availableTo,
			int size, int page);

	public void deleteByKey(String key);
	void delete(ActionEntity actionEntity);
}
