package smartspace.data;


import org.springframework.data.annotation.Id;

/*import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;*/

import org.springframework.data.mongodb.core.mapping.Document;

//@Entity
//@Table(name="USERS")
@Document(collection="Users")
public class UserEntity implements SmartspaceEntity<String> {
	private String key;
	private String identifier;
	private String userSmartspace;
	private String userEmail;
	private String userName;
	private String avatar;
	private UserRole role;
	private Long points;
	
	public UserEntity() {
		super();
	}
	public UserEntity(String userSmartspace, String userEmail, String userName, String avatar, UserRole role,
			Long points) {
		super();
		setUserSmartspace(userSmartspace);
		setUserEmail(userEmail);
		setKey(getKey());
		setUserName(userName);
		setAvatar(avatar);
		setRole(role);
		setPoints(points);
	}
	@Override
	//@Column(name="ID")
	public String getKey() {
		return this.userSmartspace+"#"+this.userEmail;
	}
	@Override
	public void setKey(String key) {
		String []str=key.split("#");
		setUserSmartspace(str[0]);
		setUserEmail(str[1]);
		this.key = key;
	}
	//@Transient
	public String getUserSmartspace() {
		return userSmartspace;
	}

	public void setUserSmartspace(String userSmartspace) {
		if(!(userSmartspace.equals(null) || userSmartspace.trim().equals(""))) {
			this.userSmartspace = userSmartspace;
		}
		else {
			throw new RuntimeException("UserEntity class: smartSpace name is empty or null ");
		}
	}
	//@Column(name="Email")
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		if(!(userEmail.equals(null) || userEmail.trim().equals(""))) {
			this.userEmail = userEmail;
		}
		else {
			throw new RuntimeException("UserEntity class: user email is empty or null ");
		}
	}
	//@Column(name="Name")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		if(!(userName.equals(null) || userName.trim().equals(""))) {
			this.userName = userName;
		}
		else {
			throw new RuntimeException("UserEntity class: user name is empty or null ");
		}
	}
	//@Column(name="Avatar")
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	//@Column(name="Role")
	public UserRole getRole() {
		return role;
	}
	public void setRole(UserRole role) {
		this.role=role;
	}
	//@Column(name="Points")
	public Long getPoints() {
		return points;
	}
	public void setPoints(Long points) {
		this.points = points;
	}
	public void AddPoints(int point){
		this.points+=point;
	}
	public void LowerPoints(int point){
		this.points-=point;
	}
	public void resetPoints(){
		this.points=new Long(0);
	}
	@Override
	public String toString() {
		return "UserEntity [key = "+ key + ", userSmartspace=" + userSmartspace + ", userEmail=" + userEmail + ", userName=" + userName
				+ ", avatar=" + avatar + ", role=" + role + ", points=" + points + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserEntity other = (UserEntity) obj;
		if (avatar == null) {
			if (other.avatar != null)
				return false;
		} else if (!avatar.equals(other.avatar))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (role != other.role)
			return false;
		if (userEmail == null) {
			if (other.userEmail != null)
				return false;
		} else if (!userEmail.equals(other.userEmail))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (userSmartspace == null) {
			if (other.userSmartspace != null)
				return false;
		} else if (!userSmartspace.equals(other.userSmartspace))
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

