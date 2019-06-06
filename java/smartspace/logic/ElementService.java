package smartspace.logic;

import java.util.List;

import smartspace.data.ElementEntity;

public interface ElementService {
	public ElementEntity insert (ElementEntity elements);
	public List<ElementEntity> getElements (int size, int page);
	public List<ElementEntity> getElementsByContent (String pattern, int size, int page);
	public void update(String key, ElementEntity entity) throws ElementNotFoundException;
	public void deleteOneElementByKey(String key);
	public List<ElementEntity> getElementsByLocation(double x, double y,double distance, int size, int page);
	public List<ElementEntity> getElementsByName(String name, int size, int page);
	public List<ElementEntity> getElementsByType(String type, int size, int page);
	public List<ElementEntity> getElementByElementId(String elementId, int size, int page);
	ElementEntity getElementById(String elementId);

}
