package smartspace.logic;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import smartspace.dao.ActionDao;
import smartspace.dao.AnhancedActionDao;
import smartspace.data.ActionEntity;
@Service
public class ActionServiceImpl implements ActionService{
	private AnhancedActionDao<String> actions;
	@Autowired
	public ActionServiceImpl(AnhancedActionDao<String> actions) {
		super();
		this.actions = actions;
	}
	@Override
	@Transactional
	public ActionEntity insert(ActionEntity action) {
		if (validate(action)) {
			action.setCreationTimestamp(new Date());
			return this.actions.create(action);
		}else {
			throw new RuntimeException("invalid action");
		}
	}

	private boolean validate(ActionEntity action) {
		return action != null &&
				action.getActionSmartspace()!=null&&
				!action.getActionSmartspace().trim().isEmpty()&&
				action.getActionId()!=null&&
				!action.getActionId().trim().isEmpty()&&
				action.getElementSmartspace()!=null&&
				!action.getElementSmartspace().trim().isEmpty()&&
				action.getElementId()!=null&&
				!action.getElementId().trim().isEmpty()&&
				action.getPlayerSmartspace()!=null&&
				!action.getPlayerSmartspace().trim().isEmpty()&&
				action.getPlayerId()!=null&&
				!action.getPlayerId().trim().isEmpty()&&
				action.getActionType()!=null&&
				!action.getActionType().trim().isEmpty()&&
				action.getCreationTimestamp()!=null&&
				action.getMoreAttributes()!=null;
	}

	@Override
	public List<ActionEntity> getActions(int size, int page) {
		return this.actions.readAll(size,page);
	}

	@Override
	public List<ActionEntity> getActionsByContent(String pattern, int size, int page) {
		return this.actions.readActionContainingText(pattern, "availableFrom", size, page);
	}
	@Override
	public void deleteOneActionByKey(String key) {
		this.actions.deleteByKey(key);	
		
	}

}
