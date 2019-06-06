package smartspace.layout;

import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import smartspace.data.ElementEntity;
import smartspace.data.Location;

public class ElementBoundary {
	private String key;
	private String elementSmartspace;
	private String elementId;
	private String location;
	private String name;
	private String type;
	public Date creationTimestamp;
	private String Expired;
	private String creatorSmartspace;
	private String creatorEmail;
	private Map<String,Object> moreAttributes;
	
	public ElementBoundary() {
		super();
	}

	public ElementBoundary(String key,String location, String name, String type,
			Date creationTimestamp, String expired, String creatorSmartspace, String creatorEmail,
			Map<String, Object> moreAttributes) {
		super();
		this.key=key;
		if (this.key != null && this.key.contains("#")) {
			String[] args = this.key.split("#");
			if (args.length == 2) {
				this.elementId=args[0];
				this.elementSmartspace=args[1];
			}
		}
		this.location = location;
		this.name = name;
		this.type = type;
		this.creationTimestamp = creationTimestamp;
		this.Expired = expired;
		this.creatorSmartspace = creatorSmartspace;
		this.creatorEmail = creatorEmail;
		this.moreAttributes = moreAttributes;
	}
	// from entity to boundary
	public ElementBoundary(ElementEntity entity) {
		String key = entity.getKey();
		if (key != null) {
			this.key=key;
			if (this.key.contains("#")) {
				String[] args = this.key.split("#");
				if (args.length == 2) {
					this.elementId=args[0];
					this.elementSmartspace=args[1];
				}
			}
		}else {
			this.key=entity.getElementId()+"#"+entity.getElementSmartspace();
			this.elementId=entity.getElementId();
			this.elementSmartspace=entity.getElementSmartspace();
		}
		if (entity.getLocation() != null) {
			double x = entity.getLocation().getX();
			double y = entity.getLocation().getY();
			this.location=x+"#"+y;
		}
		
		this.name = entity.getName();
		this.type = entity.getType();
		
		this.creationTimestamp=entity.getCreationTimestamp();
		this.Expired=entity.getExpired().toString();
		this.creatorSmartspace=entity.getCreatorSmartspace();
		this.creatorEmail=entity.getCreatorEmail();
		this.moreAttributes=entity.getMoreAttributes();
	}
	// from  boundary to entity
	public ElementEntity toEntity() {
		ElementEntity entity = new ElementEntity();
		if (this.key != null && this.key.contains("#")) {
			String[] args = this.key.split("#");
			if (args.length == 2) {
				entity.setElementId(args[0]);
				entity.setElementSmartspace(args[1]);
				entity.setKey(key);
			}
		}else {
			throw new RuntimeException("Invalid key");
		}
		if(this.location!=null && this.location.contains("#")) {
			double[] arr = Stream.of(this.location.split("#"))
                    .mapToDouble (Double::parseDouble)
                    .toArray();
			if (arr.length == 2) {
			    Location location =new Location(arr[0],arr[1]);
				entity.setLocation(location);
			}
		}
		entity.setName(name);
		entity.setType(type);
		entity.setCreationTimestamp(creationTimestamp);
		if(this.Expired!=null && this.Expired.equals("false")) {
			entity.setExpired(false);
		}
		if(this.Expired!=null && this.Expired.equals("true")) {
			entity.setExpired(true);
		}
		entity.setCreatorEmail(creatorEmail);
		entity.setCreatorSmartspace(creatorSmartspace);
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public String getExpired() {
		return Expired;
	}

	public void setExpired(String expired) {
		Expired = expired;
	}

	public String getCreatorSmartspace() {
		return creatorSmartspace;
	}

	public void setCreatorSmartspace(String creatorSmartspace) {
		this.creatorSmartspace = creatorSmartspace;
	}

	public String getCreatorEmail() {
		return creatorEmail;
	}

	public void setCreatorEmail(String creatorEmail) {
		this.creatorEmail = creatorEmail;
	}

	public Map<String, Object> getMoreAttributes() {
		return moreAttributes;
	}

	public void setMoreAttributes(Map<String, Object> moreAttributes) {
		this.moreAttributes = moreAttributes;
	}

	@Override
	public String toString() {
		return "ElementBoundary [key=" + key + ", elementSmartspace=" + elementSmartspace + ", elementId=" + elementId
				+ ", location=" + location + ", name=" + name + ", type=" + type + ", creationTimestamp="
				+ creationTimestamp + ", Expired=" + Expired + ", creatorSmartspace=" + creatorSmartspace
				+ ", creatorEmail=" + creatorEmail + ", moreAttributes=" + moreAttributes + "]";
	}


	

}
