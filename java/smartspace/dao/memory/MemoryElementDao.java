package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import smartspace.dao.ElementDao;
import smartspace.data.ElementEntity;
//@Repository
public class MemoryElementDao implements ElementDao<String> {
	private Map<String, ElementEntity> elements;
	public MemoryElementDao() {
		super();
		this.elements=Collections.synchronizedMap(new HashMap<>());
	}
	@Override
	public ElementEntity create(ElementEntity elementEntity) {
		this.elements.put(elementEntity.getKey(), elementEntity);
		return elementEntity;
	}
	@Override
	public Optional<ElementEntity> readById(String key) {
		ElementEntity output = this.elements.get(key);
		if (output != null) {
			return Optional.of(output);
		}else {
			return Optional.empty();
		}
	}
	@Override
	public List<ElementEntity> readAll() {
		return new ArrayList<ElementEntity>(this.elements.values());
	}
	@Override
	public void update(ElementEntity elementEntity) {
		boolean dirtyFlag = false; 
		ElementEntity existing = 
			this.readById(elementEntity.getKey()).orElseThrow(
					()->new RuntimeException("could not find any element with id: " + elementEntity.getKey())
			);
					/*new Supplier<RuntimeException>() {
						@Override
						public RuntimeException get() {
							return new RuntimeException("could not find any element with id: " + message.getKey());
						}
					});*/
	    if (elementEntity.getElementSmartspace() != null) {
		    existing.setElementSmartspace(elementEntity.getElementSmartspace());
		    dirtyFlag = true;
	    }
		if (elementEntity.getElementId() != null) {
			existing.setElementId(elementEntity.getElementId());
			dirtyFlag = true;
		}
		if (elementEntity.getLocation() != null) {
			existing.setLocation(elementEntity.getLocation());
			dirtyFlag = true;
		}
		if (elementEntity.getName() != null) {
			existing.setName(elementEntity.getName());
			dirtyFlag = true;
		}
		if (elementEntity.getType() != null) {
			existing.setType(elementEntity.getType());
			dirtyFlag = true;
		}
		if (elementEntity.getCreationTimestamp() != null) {
			existing.setCreationTimestamp(elementEntity.getCreationTimestamp());
			dirtyFlag = true;
		}
		if (elementEntity.getExpired() != null) {
			existing.setExpired(elementEntity.getExpired());
			dirtyFlag = true;
		}
		if (elementEntity.getCreatorSmartspace() != null) {
			existing.setCreatorSmartspace(elementEntity.getCreatorSmartspace());
			dirtyFlag = true;
		}
		if (elementEntity.getCreatorEmail() != null) {
			existing.setCreatorEmail(elementEntity.getCreatorEmail());
			dirtyFlag = true;
		}
		if (elementEntity.getMoreAttributes() != null) {
			existing.setMoreAttributes(elementEntity.getMoreAttributes());
			dirtyFlag = true;
		}
		if (dirtyFlag) {
			this.elements.put(existing.getKey(), existing);
		}
	}
	@Override
	public void deleteByKey(String key) {
		if(elements.containsKey(key)) {
			elements.remove(key);
		}else { // Optional
			new RuntimeException("could not delete not existing element: "+key.toString());
		}
	}
	@Override
	public void delete(ElementEntity elementEntity) {
		if(elements.containsValue(elementEntity)) {
			for (Entry<String, ElementEntity> set : elements.entrySet()) {
		        if (set.getValue().equals(elementEntity)) {
		            elements.remove(set.getKey(), elementEntity);
		            break;
		        }
		    }
		}
		else { // Optional
			new RuntimeException("could not delete not existing element: "+elementEntity.toString());
		}
	}
	@Override
	public void deleteAll() {
		this.elements.clear();
	}
	public Map<String, ElementEntity> getElements() {
		return elements;
	}
	public void setElements(Map<String, ElementEntity> elements) {
		this.elements = elements;
	}
}
