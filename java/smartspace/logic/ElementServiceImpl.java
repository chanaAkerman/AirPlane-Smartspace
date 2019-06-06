package smartspace.logic;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.AnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
@Service
public class ElementServiceImpl implements ElementService {
	private AnhancedElementDao<String> elements;

	@Autowired
   	public ElementServiceImpl(AnhancedElementDao<String> elements) {
   		super();
   		this.elements=elements;
   	}

	@Override
	@Transactional
	public ElementEntity insert(ElementEntity element) {
		element.setCreationTimestamp(new Date());
		if (validate(element)) {
			return this.elements.create(element);
		}else {
			throw new RuntimeException("invalid element");
		}
	}
	private boolean validate(ElementEntity element) {
		return element != null &&
				element.getElementSmartspace()!=null&&
				!element.getElementSmartspace().trim().isEmpty()&&
				element.getElementId()!=null&&
				!element.getElementId().trim().isEmpty()&&
				element.getLocation()!=null&&
				element.getName()!=null&&
				!element.getName().trim().isEmpty()&&
				element.getType()!=null&&
				!element.getType().trim().isEmpty()&&
				element.getCreationTimestamp()!=null&&
				element.getExpired()!=null&&
				element.getCreatorSmartspace()!=null&&
				!element.getCreatorSmartspace().trim().isEmpty()&&
				element.getCreatorEmail()!=null&&
				!element.getCreatorEmail().trim().isEmpty()&&
				element.getMoreAttributes()!=null;
	}

	@Override
	public List<ElementEntity> getElements(int size, int page) {
		return this.elements.readAll(size,page);
	}

	@Override
	public List<ElementEntity> getElementsByContent(String pattern, int size, int page) {
		return this.elements.readElementContainingText(pattern, "availableFrom", size, page);
	}

	@Override
	public void update(String key, ElementEntity entity) throws ElementNotFoundException {
		//entity.setKey(key);
		this.elements.update(entity);
	}

	@Override
	public void deleteOneElementByKey(String key) {
		this.elements.deleteByKey(key);
		
	}

	@Override
	public List<ElementEntity> getElementsByLocation(double x, double y,double distance, int size, int page) {
		Location location1=new Location(x-distance, y-distance);
		Location location2=new Location(x+distance, y+distance);
		return this.elements.readAllByLocationBetween(location1,location2,size,page);
	}

	@Override
	public List<ElementEntity> getElementsByName(String name, int size, int page) {
		return this.elements.readAllByName(name,size,page);
	}

	@Override
	public List<ElementEntity> getElementsByType(String type, int size, int page) {
		return this.elements.readAllByTypeLike(type,size,page);
	}

	@Override
	public List<ElementEntity> getElementByElementId(String elementId, int size, int page) {
			return this.elements.readByElementElementId(elementId,size,page);
	}

	@Override
	public ElementEntity getElementById(String elementId) {
		return this.elements.readByElementId(elementId);
	}



}
