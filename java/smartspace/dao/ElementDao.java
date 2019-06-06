package smartspace.dao;

import java.util.List;
import java.util.Optional;

import smartspace.data.ElementEntity;

public interface ElementDao<ElementKey> {
	public ElementEntity create(ElementEntity elementEntity);
	public Optional<ElementEntity> readById (ElementKey key);
	public List<ElementEntity> readAll();
	public void update (ElementEntity elementEntity);
	public void deleteByKey(ElementKey key);
	public void delete(ElementEntity elementEntity);
	public void deleteAll();

}
