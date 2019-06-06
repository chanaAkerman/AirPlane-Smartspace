package smartspace.data;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.springframework.data.annotation.Id;

/*import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;*/

import org.springframework.data.mongodb.core.mapping.Document;

import smartspace.plugins.AirplaneSeat;

//@Entity
//@Table(name="ELEMENTS")
@Document(collection="Elements")
public class ElementEntity implements SmartspaceEntity<String>{
	private String identifier;
	private String key;
	private String elementSmartspace;
	private String elementId;
	private Location location;
	private String name; // ticket
	private String type; // flight Ticket
	public Date creationTimestamp;
	private Boolean Expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Map<String,Object> moreAttributes;

	public ElementEntity() {
		super();
	}
	public ElementEntity(String elementSmartspace, String elementId, Location location, String name, String type, Date creationTimestamp,
			Boolean Expired, String creatorSmartspace, String creatorEmail, Map<String, Object> moreAttributes) {
		setElementSmartspace(elementSmartspace);
		setElementId(elementId);
		setKey(getKey());
		setLocation(location);
		setName(name);
		setType(type);
		setCreationTimestamp(creationTimestamp);
		setExpired(Expired);
		setCreatorSmartspace(creatorSmartspace);
		setCreatorEmail(creatorEmail);
		setMoreAttributes(moreAttributes);
	}
	@Override
	public String getKey() {
		return this.elementId+"#"+this.elementSmartspace;
	}
	@Override
	public void setKey(String key) {
		String []str=key.split("#");
		setElementId(str[0]);
		setElementSmartspace(str[1]);
		this.key=key;
	}
	public void addAttribute(AirplaneSeat res) {
		HashMap<String,Object>map = new HashMap<>();
		map.put("userKey", res.getUserKey());
		map.put("category", res.getCategory());
		map.put("meal", res.getMeal().toString());
		map.put("confrimed", res.isConfirmed());
		map.put("reserved", res.isReserved());
		this.setMoreAttributes(map);
	}
	public void cleanAttribute() {
		this.moreAttributes=new HashMap<>();
	}
	//@Transient
	public String getElementSmartspace() {
		return this.elementSmartspace;
		
	}
	public void setElementSmartspace(String elementSmartspace) {
		if(!(elementSmartspace.equals(null)||elementSmartspace.trim().equals(""))) {
			this.elementSmartspace = elementSmartspace;
		}
		else {
			throw new RuntimeException("ElementEntity class: Element smartSpace is empty or null ");
		}
	}
	//@Transient
	public String getElementId() {
		return elementId;
	}
	public void setElementId(String elementId) {
		if(!(elementId.equals(null)||elementId.trim().equals(""))) {
			this.elementId = elementId;
		}
		else {
			throw new RuntimeException("ElementEntity class: ElementId is empty or null ");
		}
	}
	//@Embedded
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	//@Column(name="Name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(!(name.equals(null)||name.trim().equals(""))) {
			this.name = name;
		}
		else {
			throw new RuntimeException("ElementEntity class: Element name is empty or null ");
		}
	}
	//@Column(name="Type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		if(!(type.equals(null)||type.trim().equals(""))) {
			this.type = type;
		}
		else {
			throw new RuntimeException("ElementEntity class: Element type is empty or null ");
		}
	}
	//@Column(name="Expired")
	public Boolean getExpired() {
		return Expired;
	}
	public void setExpired(Boolean expired) {
		Expired = expired;
	}
	//@Column(name="CreatorSmartSpace")
	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}
	public void setCreatorSmartspace(String creatorSmartspace) {
		if(!(creatorSmartspace.equals(null)||creatorSmartspace.trim().equals(""))) {
			this.creatorSmartspace = creatorSmartspace;
		}
		else {
			throw new RuntimeException("ElementEntity class: CreatorSmartSpace is empty or null ");
		}
	}
	//@Column(name="Creator_Email")
	public String getCreatorEmail() {
		return creatorEmail;
	}
	public void setCreatorEmail(String creatorEmail) {
		if(!(creatorEmail.equals(null)||creatorEmail.trim().equals(""))) {
			this.creatorEmail = creatorEmail;
		}
		else {
			throw new RuntimeException("ElementEntity class: CreatorEmail is empty or null ");
		}
	}
	//@Column(name="Creation_Time_Stamp")
	public Date getCreationTimestamp() {
		return creationTimestamp;
	}
	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}
	//@Convert(converter=MapToJsonConverter.class)
	//@Lob
	public Map<String,Object> getMoreAttributes() {
		return moreAttributes;
	}
	public void setMoreAttributes(Map<String,Object> moreAttributes) {
			this.moreAttributes = moreAttributes;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ElementEntity other = (ElementEntity) obj;
		if (Expired == null) {
			if (other.Expired != null)
				return false;
		} else if (!Expired.equals(other.Expired))
			return false;
		if (creationTimestamp == null) {
			if (other.creationTimestamp != null)
				return false;
		} else if (!(creationTimestamp.compareTo(other.creationTimestamp) == 0)) 
			return false;
		if (creatorEmail == null) {
			if (other.creatorEmail != null)
				return false;
		} else if (!creatorEmail.equals(other.creatorEmail))
			return false;
		if (creatorSmartspace == null) {
			if (other.creatorSmartspace != null)
				return false;
		} else if (!creatorSmartspace.equals(other.creatorSmartspace))
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
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (moreAttributes == null) {
			if (other.moreAttributes != null)
				return false;
		} else if (!moreAttributes.equals(other.moreAttributes))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ElementEntity [elementSmartspace=" + elementSmartspace + ", elementId=" + elementId + ", location="
				+ location + ", name=" + name + ", type=" + type + ", creationTimestamp=" + creationTimestamp
				+ ", Expired=" + Expired + ", creatorSmartspace=" + creatorSmartspace + ", creatorEmail=" + creatorEmail
				+ ", moreAttributes=" + printAttributes() + "]";
	}
	public String getIdentifier() {
		return this.identifier;
	}
	@Id
	public void setIdentifier(String key) {
		this.identifier=key;
	}
	public String printAttributes() {
		if(moreAttributes!=null) {
			return Arrays.toString(moreAttributes.entrySet().toArray());
		}
		return "{}";
	}
}
