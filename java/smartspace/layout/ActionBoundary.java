package smartspace.layout;

import java.util.Date;
import java.util.Map;

import smartspace.data.ActionEntity;

public class ActionBoundary {
	private String key;
	private String actionSmartspace;
	private String actionId;
	private String elementSmartspace;
	private String elementId;
	private String playerSmartspace;
	private String playerId;
	private String actionType;
	private Date creationTimestamp;
	private Map<String, Object> moreAttributes;
	
	public ActionBoundary() {
		super();
	}

	public ActionBoundary(String key,String elementSmartspace, String elementId, String playerSmartspace,
			String playerId, String actionType, Date creationTimestamp, Map<String, Object> moreAttributes) {
		super();
		this.key=key;
		if (this.key != null && this.key.contains("#")) {
			String[] args = this.key.split("#");
			if (args.length == 2) {
				this.actionSmartspace=args[0];
				this.actionId=args[1];
			}
		}
		this.elementSmartspace = elementSmartspace;
		this.elementId = elementId;
		this.playerSmartspace = playerSmartspace;
		this.playerId = playerId;
		this.actionType = actionType;
		this.creationTimestamp = creationTimestamp;
		this.moreAttributes = moreAttributes;
	}
	public ActionBoundary(ActionEntity entity) {
		String key = entity.getKey();
		if (key != null) {
			this.key=key;
		}else {
			this.key=entity.getActionSmartspace()+"#"+entity.getActionId();
		}
		
		this.elementSmartspace=entity.getElementSmartspace();
		this.elementId=entity.getElementId();
		this.playerSmartspace=entity.getPlayerSmartspace();
		this.playerId=entity.getPlayerId();
		this.actionType=entity.getActionType();
		this.creationTimestamp=entity.getCreationTimestamp();
		this.moreAttributes=entity.getMoreAttributes();
	}


	public ActionEntity toEntity() {
		ActionEntity entity = new ActionEntity();
		if (this.key != null && this.key.contains("#")) {
			String[] args = this.key.split("#");
			if (args.length == 2) {
				entity.setActionSmartspace(args[0]);
				entity.setActionId(args[1]);
				entity.setKey(key);
			}
		}else {
			throw new RuntimeException("Invalid key");
		}
		entity.setElementSmartspace(elementSmartspace);
		entity.setElementId(elementId);
		entity.setPlayerSmartspace(playerSmartspace);
		entity.setPlayerId(playerId);
		entity.setActionType(actionType);
		entity.setCreationTimestamp(creationTimestamp);
		entity.setMoreAttributes(moreAttributes);
		return entity;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getElementSmartspace() {
		return elementSmartspace;
	}

	public void setElementSmartspace(String elementSmartspace) {
		this.elementSmartspace = elementSmartspace;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getPlayerSmartspace() {
		return playerSmartspace;
	}

	public void setPlayerSmartspace(String playerSmartspace) {
		this.playerSmartspace = playerSmartspace;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	public String getActionSmartspace() {
		return actionSmartspace;
	}

	public void setActionSmartspace(String actionSmartspace) {
		this.actionSmartspace = actionSmartspace;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	@Override
	public String toString() {
		return "ActionBoundary [key=" + key + ", actionSmartspace=" + actionSmartspace + ", actionId=" + actionId
				+ ", elementSmartspace=" + elementSmartspace + ", elementId=" + elementId + ", playerSmartspace="
				+ playerSmartspace + ", playerId=" + playerId + ", actionType=" + actionType + ", creationTimestamp="
				+ creationTimestamp + ", moreAttributes=" + moreAttributes + "]";
	}
	
	

}
