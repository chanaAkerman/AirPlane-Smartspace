package smartspace.dao.memory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import smartspace.data.ActionEntity;
//@Repository
public class MemoryActionDao implements smartspace.dao.ActionDao{
	private Map<String, ActionEntity> actions;
	
	public MemoryActionDao() {
		super();
		this.setActions(Collections.synchronizedMap(new HashMap<>()));
	}

	@Override
	public ActionEntity create(ActionEntity actionEntity) {
		this.actions.put(actionEntity.getKey(), actionEntity);
		return actionEntity;
	}
	@Override
	public List<ActionEntity> readAll() {
		return new ArrayList<ActionEntity>(this.actions.values());
	}

	@Override
	public void deleteAll() {
		this.actions.clear();
	}

	public Map<String, ActionEntity> getActions() {
		return actions;
	}

	public void setActions(Map<String, ActionEntity> actions) {
		this.actions = actions;
	}

}
