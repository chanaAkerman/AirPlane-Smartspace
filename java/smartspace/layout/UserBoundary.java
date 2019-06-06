package smartspace.layout;

import smartspace.data.UserEntity;
import smartspace.data.UserRole;
public class UserBoundary {
	private String key;
	private String userSmartspace;
	private String userEmail;
	private String userName;
	private String avatar;
	private String role;
	private Long points;
	
	public UserBoundary() {
		super();
	}

	public UserBoundary(String key, String userName, String avatar,
			String role, Long points) {
		super();
		this.key=key;
		if (this.key != null && this.key.contains("#")) {
			String[] args = this.key.split("#");
			if (args.length == 2) {
				this.setUserSmartspace(args[0]);
				this.setUserEmail(args[1]);
			}
		}
		this.userName = userName;
		this.avatar = avatar;
		this.role = role;
		this.points = points;
	}
	// from entity to boundary
	public UserBoundary(UserEntity entity) {
		String key = entity.getKey();
		if (key != null) {
			this.key=key;
			if (this.key.contains("#")) {
				String[] args = this.key.split("#");
				if (args.length == 2) {
					this.setUserSmartspace(args[0]);
					this.setUserEmail(args[1]);
				}
			}
		}else {
			this.key=entity.getUserSmartspace()+"#"+entity.getUserEmail();
			this.userSmartspace=entity.getUserSmartspace();
			this.userEmail=entity.getUserEmail();
		}
		this.userName=entity.getUserName();
		this.avatar=entity.getAvatar();
		this.role=entity.getRole().toString();

		this.points=entity.getPoints();
		
	}
	// from boundary to entity
	public UserEntity toEntity() {
		UserEntity entity = new UserEntity();
		if (this.key != null && this.key.contains("#"))
				{
			String[] args = this.key.split("#");
			if (args.length == 2) {
				entity.setUserSmartspace(args[0]);
				entity.setUserEmail(args[1]);
				entity.setKey(key);
			}
		}
		else {
			throw new RuntimeException("Invalid key");
		}
		
		entity.setUserName(userName);
		entity.setAvatar(avatar);
		if(this.getRole().equals(UserRole.ADMIN+"")) {
			entity.setRole(UserRole.ADMIN);
		}
		if(this.getRole().equals(UserRole.MANAGER+"")) {
			entity.setRole(UserRole.MANAGER);
		}
		if(this.getRole().equals(UserRole.PLAYER+"")) {
			entity.setRole(UserRole.PLAYER);
		}
		entity.setPoints(points);
		return entity;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getPoints() {
		return points;
	}

	public void setPoints(Long points) {
		this.points = points;
	}
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		this.userSmartspace = userSmartspace;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Override
	public String toString() {
		return "UserBoundary [key=" + key + ", userSmartspace=" + userSmartspace + ", userEmail=" + userEmail
				+ ", userName=" + userName + ", avatar=" + avatar + ", role=" + role + ", points=" + points + "]";
	}
	


	
	
	

}
