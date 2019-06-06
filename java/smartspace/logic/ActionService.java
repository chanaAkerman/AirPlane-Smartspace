package smartspace.logic;

import java.util.List;

import smartspace.data.ActionEntity;

public interface ActionService {
	public ActionEntity insert (ActionEntity elements);
	public List<ActionEntity> getActions (int size, int page);
	public List<ActionEntity> getActionsByContent (String pattern, int size, int page);
	public void deleteOneActionByKey(String key);

}
