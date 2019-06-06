package smartspace.data.util;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;
@Component
public class EntityFactoryImpl implements EntityFactory {
	public static String Smartspace;
	public static AtomicLong nextElementId;
	public static AtomicLong nextActiontId;
	public EntityFactoryImpl() {
		super();
		Smartspace="2019B.chana";
		nextElementId=new AtomicLong(1);
		nextActiontId=new AtomicLong(1);
	}
	@Override
	public ElementEntity createNewElement(Location location, String name, String type, Date creationTimestamp,
			Boolean Expired, String creatorSmartspace, String creatorEmail, Map<String, Object> moreAttributes) {
		return new ElementEntity(Smartspace, nextElementId.getAndIncrement()+"", location, name, type, creationTimestamp, Expired, creatorSmartspace, creatorEmail, moreAttributes);
	}
	@Override
	public ActionEntity createNewAction(String elementSmartspace, String elementId,String playerSmartspace, String playerId, String actionType,
			Date creationTimestamp, Map<String, Object> moreAttributes) {
		return new ActionEntity(Smartspace, nextActiontId.getAndIncrement()+"", elementSmartspace, elementId, playerSmartspace, playerId, actionType, creationTimestamp, moreAttributes);
		}
	@Override
	public UserEntity createNewUser(String userEmail, String userName, String avatar, UserRole role,
			Long points) {
		return new UserEntity(Smartspace, userEmail, userName, avatar, role, points);
	}
	public static String getSmartspace() {
		return Smartspace;
	}
	public static void setSmartspace(String smartspace) {
		Smartspace = smartspace;
	}
	public static AtomicLong getNextElementId() {
		return nextElementId;
	}
	public static AtomicLong getNextActiontId() {
		return nextActiontId;
	}
}
