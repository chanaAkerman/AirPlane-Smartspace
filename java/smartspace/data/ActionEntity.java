package smartspace.data;

import java.util.Date;

import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

/*import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;*/


//@Entity
//@Table(name="ACTIONS")
@Document(collection="Actions")
public class ActionEntity implements SmartspaceEntity<String> {

	private String identifier;
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
	
	public ActionEntity() {
		super();
	}
	public ActionEntity(String actionSmartspace, String actionId, String elementSmartspace, String elementId,
			String playerSmartspace, String playerId, String actionType, Date creationTimestamp,
			Map<String, Object> moreAttributes) {
		super();
		setActionSmartspace(actionSmartspace);
		setActionId(actionId);
		setElementSmartspace(elementSmartspace);
		setElementId(elementId);
		setPlayerSmartspace(playerSmartspace);
		setPlayerId(playerId);
		setKey(getKey());
		setActionType(actionType);
		setCreationTimestamp(creationTimestamp);
		setMoreAttributes(moreAttributes);
	}
	@Override
	public String getKey() {
		return this.actionId+"#"+this.actionSmartspace;
	}
	@Override
	public void setKey(String key) {
		String []str=key.split("#");
		setActionId(str[0]);
		setActionSmartspace(str[1]);
		this.key=key;
	}
	//@Transient
	public String getActionSmartspace() {
		return actionSmartspace;
	}
	public void setActionSmartspace(String actionSmartspace) {
		if(!(actionSmartspace.equals(null)||actionSmartspace.trim().equals(""))) {
			this.actionSmartspace = actionSmartspace;
		}
		else {
			throw new RuntimeException("ActionEntity class: Action Smarts pace is empty or null ");
		}
	}
	//@Transient
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		if(!(actionId.equals(null)||actionId.trim().equals(""))) {
			this.actionId = actionId;
			
		}
		else {
			throw new RuntimeException("ActionEntity class: Action Id is empty or null ");
		}
	}
	//@Column(name="Element_SmartSpace")
	public String getElementSmartspace() {
		return elementSmartspace;
	}
	public void setElementSmartspace(String elementSmartspace) {
		if(!(elementSmartspace.equals(null)||elementSmartspace.trim().equals(""))) {
			this.elementSmartspace = elementSmartspace;
		}
		else {
			throw new RuntimeException("ActionEntity class: Element Smart space is empty or null ");
		}
	}
	@Override
	public String toString() {
		return "ActionEntity [actionSmartspace=" + actionSmartspace + ", actionId=" + actionId + ", elementSmartspace="
				+ elementSmartspace + ", elementId=" + elementId + ", playerSmartspace=" + playerSmartspace
				+ ", playerId=" + playerId + ", actionType=" + actionType + ", creationTimestamp=" + creationTimestamp
				+ ", moreAttributes=" + moreAttributes + "]";
	}
	//@Column(name="Element_Id")
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		if(!(elementId.equals(null)||elementId.trim().equals(""))) {
			this.elementId = elementId;
		}
		else {
			throw new RuntimeException("ActionEntity class: Element Id is empty or null ");
		}
	}
	//@Column(name="Player_Id")
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		if(!(playerId.equals(null)||playerId.trim().equals(""))) {
			this.playerId = playerId;
		}
		else {
			throw new RuntimeException("ActionEntity class: player Id is empty or null ");
		}
	}
	//@Column(name="Player_SmartSpace")
	public String getPlayerSmartspace() {
		return playerSmartspace;
	}
	public void setPlayerSmartspace(String playerSmartspace) {
		if(!(playerSmartspace.equals(null)||playerSmartspace.trim().equals(""))) {
			this.playerSmartspace = playerSmartspace;
		}
		else {
			throw new RuntimeException("ActionEntity class: player Smart space is empty or null ");
		}
	}
	//@Column(name="Action_Type")
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		if(!(actionType.equals(null)||actionType.trim().equals(""))) {
			this.actionType = actionType;
		}
		else {
			throw new RuntimeException("ActionEntity class: Action Type is empty or null ");
		}
	}
	//@Column(name="CreationTimeStamp")
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}
	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp; // Allow null
	}
	//@Convert(converter=MapToJsonConverter.class)
	//@Lob
	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}
	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes; // Allows null
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ActionEntity other = (ActionEntity) obj;
		if (actionId == null) {
			if (other.actionId != null)
				return false;
		} else if (!actionId.equals(other.actionId))
			return false;
		if (actionSmartspace == null) {
			if (other.actionSmartspace != null)
				return false;
		} else if (!actionSmartspace.equals(other.actionSmartspace))
			return false;
		if (actionType == null) {
			if (other.actionType != null)
				return false;
		} else if (!actionType.equals(other.actionType))
			return false;
		if (creationTimestamp == null) {
			if (other.creationTimestamp != null)
				return false;
		} else if (!(creationTimestamp.compareTo(other.creationTimestamp) == 0))
			return false;
		if (elementId == null) {
			if (other.elementId != null)
				return false;
		} else if (!elementId.equals(other.elementId))
			return false;
		if (elementSmartspace == null) {
			if (other.elementSmartspace != null)
				return false;
		} else if (!elementSmartspace.equals(other.elementSmartspace))
			return false;
		if (moreAttributes == null) {
			if (other.moreAttributes != null)
				return false;
		} else if (!moreAttributes.equals(other.moreAttributes))
			return false;
		if (playerId == null) {
			if (other.playerId != null)
				return false;
		} else if (!playerId.equals(other.playerId))
			return false;
		if (playerSmartspace == null) {
			if (other.playerSmartspace != null)
				return false;
		} else if (!playerSmartspace.equals(other.playerSmartspace))
			return false;
		return true;
	}
	public String getIdentifier() {
		return this.identifier;
	}
	@Id
	public void setIdentifier(String key) {
		this.identifier=key;
	}
}

