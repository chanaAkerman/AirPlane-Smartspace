package smartspace.dao.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import smartspace.dao.AnhancedActionDao;
import smartspace.data.ActionEntity;
import smartspace.data.UserEntity;
import smartspace.data.util.EntityFactory;

@Repository
public class RdbActionDao implements AnhancedActionDao<String> {
	private ActionCrud actionCrud;
	//private GeneratedNumber generatedUmber;
	//private IdentitySeedCrud identitySeedCrud;
	
	@Autowired
	public RdbActionDao(ActionCrud actionCrud) {
		super();
		this.actionCrud = actionCrud;
	}

	@Override
	@Transactional
	public ActionEntity create(ActionEntity action) {		
		//
		// creating only with factory, factory give a unique number
		//
		
		// SQL INSERT
				if (!this.actionCrud.existsById(action.getKey())) {
					ActionEntity rv = this.actionCrud.save(action);
					return rv;
				}else {
					throw new RuntimeException("Action already exists with id: " + action.getKey());
				}
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll() {
		List<ActionEntity> list = new ArrayList<>();
		// SQL SELECT
		this.actionCrud.findAll()
			.forEach(list::add);
		return list;
	}

	@Override
	public void deleteAll() {
		this.actionCrud.deleteAll();
		
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(int size, int page) {
		return
			this.actionCrud
			.findAll(PageRequest.of(page, size))
			.getContent();
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAll(
			String sortingAttr, 
			int size, int page) {
		return
				this.actionCrud
				.findAll(
					PageRequest.of(
							page, size, 
							Direction.ASC, sortingAttr))
				.getContent();
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readActionContainingText(
			String text, int size, int page) {
		return this.actionCrud
				.findAllByActionTypeLike(
					text,
					PageRequest.of(page, size));
	}

	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readActionContainingText(
			String text, 
			String sortAttr, 
			int size, int page) {
		return this.actionCrud
				.findAllByActionTypeLike (
					text,
					PageRequest.of(page, size, Direction.ASC, sortAttr));
	}
	@Override
	@Transactional(readOnly=true)
	public List<ActionEntity> readAllCreationTimestamp(Date availableFrom, Date availableTo, int size, int page) {
		return this.actionCrud.findAllByCreationTimestampBetween(availableFrom,availableTo,PageRequest.of(page, size));
	}

	public ActionCrud getActionCrud() {
		return actionCrud;
	}

	public void setActionCrud(ActionCrud actionCrud) {
		this.actionCrud = actionCrud;
	}
	@Override
	public void delete(ActionEntity actionEntity) {
			if(actionCrud.existsByKey(actionEntity.getKey())) {
				actionCrud.deleteByKey(actionEntity.getKey());
			}else { // Optional
				new RuntimeException("could not delete not existing element: "+actionEntity.toString());
			}
		
	}

	@Override
	public void deleteByKey(String key) {
		ActionEntity actionEntity = this.actionCrud.findByKeyLike(key);
		if(actionEntity!=null) {
			actionCrud.deleteByKey(actionEntity.getKey());
		}else { // Optional
			new RuntimeException("could not delete not existing element: "+actionEntity.toString());
		}
		
	}

	

}

