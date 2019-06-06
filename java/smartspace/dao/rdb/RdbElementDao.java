package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AnhancedElementDao;
import smartspace.data.ElementEntity;
import smartspace.data.Location;

@Repository
public class RdbElementDao implements AnhancedElementDao<String> {
	private ElementCrud elementCrud;
	//private GeneratedNumber generatedNumber; 
	
	@Autowired
	public RdbElementDao(ElementCrud elementCrud) {
		super();
		this.elementCrud = elementCrud;
	}

	@Override
	@Transactional
	public ElementEntity create(ElementEntity element) {
		// SQL INSERT
		if (!this.elementCrud.existsById(element.getKey())) {
			ElementEntity rv = this.elementCrud.save(element);
			return rv;
		}else {
			throw new RuntimeException("Element already exists with id: " + element.getKey());
		}
	}

	@Override
	@Transactional(readOnly=true)
	public Optional<ElementEntity> readById(String key) {
		// SQL SELECT 
		return this.elementCrud.findById(key);
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll() {
		List<ElementEntity> list = new ArrayList<>();
		// SQL SELECT
		this.elementCrud.findAll()
			.forEach(list::add);
		return list;
	}

	@Override
	@Transactional
	public void update(ElementEntity element) {
		ElementEntity existing = this.readByKey(element.getKey());
		if(existing==null) {
			throw new RuntimeException("could not find any element with id: " + element.getKey());
		}
		
		if (element.getElementSmartspace() != null) {
			existing.setElementSmartspace(element.getElementSmartspace());
		}
		if (element.getElementId() != null) {
			existing.setElementId(element.getElementId());
		}
		if (element.getLocation() != null) {
			existing.setLocation(element.getLocation());
		}
		if (element.getName() != null) {
			existing.setName(element.getName());
		}
		if (element.getType() != null) {
			existing.setType(element.getType());
		}
		if (element.getCreationTimestamp() != null) {
			existing.setCreationTimestamp(element.getCreationTimestamp());
		}
		if (element.getExpired() != null) {
			existing.setExpired(element.getExpired());
		}
		if (element.getCreatorSmartspace() != null) {
			existing.setCreatorSmartspace(element.getCreatorSmartspace());
		}
		if (element.getCreatorEmail() != null) {
			existing.setCreatorEmail(element.getCreatorEmail());
		}
		if (element.getMoreAttributes() != null) {
			existing.setMoreAttributes(element.getMoreAttributes());
		}

		// SQL UPDATE
		this.elementCrud.save(existing);
	}

	@Override
	@Transactional
	public void deleteAll() {
		this.elementCrud.deleteAll();		
	}

	@Override
	public void deleteByKey(String key) {
		if (this.elementCrud.existsByKey(key)) {
			elementCrud.deleteByKey(key);
		}else { // Optional
			new RuntimeException("could not delete not existing element: "+key);
		}
	}
	

	@Override
	public void delete(ElementEntity elementEntity) {
		if(elementCrud.existsByKey(elementEntity.getKey())) {
			elementCrud.deleteByKey(elementEntity.getKey());
		}else { // Optional
			new RuntimeException("could not delete not existing element: "+elementEntity.toString());
		}
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAll(int size, int page) {
		return
			this.elementCrud
			.findAll(PageRequest.of(page, size))
			.getContent();
	}

	@Override
	public List<ElementEntity> readAll(String sortingAttr, int size, int page) {
		return this.elementCrud.findAll(PageRequest.of(page, size, Direction.ASC, sortingAttr))
				.getContent();
	}

	@Override
	public List<ElementEntity> readElementContainingText(String text, int size, int page) {
		return this.elementCrud.findAllByNameLike (text,PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readElementContainingText(String text, String sortAttr, int size, int page) {
		return this.elementCrud.findAllByNameLike (text,PageRequest.of(page, size, Direction.ASC, sortAttr));
	}

	@Override
	@Transactional(readOnly=true)
	public List<ElementEntity> readAllCreationTimestamp(Date availableFrom, Date availableTo, int size, int page) {
		return this.elementCrud.findAllByCreationTimestampBetween(availableFrom,availableTo,PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllByLocationBetween(Location location1, Location location2, int size, int page) {
		return this.elementCrud.findAllByLocationBetween(location1, location2, PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllByName(String name, int size, int page) {
		return this.elementCrud.findAllByNameLike(name,PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readAllByTypeLike(String type, int size, int page) {
		return this.elementCrud.findAllByTypeLike(type,PageRequest.of(page, size));
	}

	@Override
	public List<ElementEntity> readByElementElementId(String elementId, int size, int page) {
		return this.elementCrud.findByElementIdLike(elementId,PageRequest.of(page, size));
	}

	@Override
	public ElementEntity readByElementId(String elementId) {
		return this.elementCrud.findByElementId(elementId);
	}

	@Override
	public ElementEntity readByKey(String key) {
		return this.elementCrud.findByKey(key);
	}
}
