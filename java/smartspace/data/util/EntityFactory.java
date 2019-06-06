package smartspace.data.util;


import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Component;

import smartspace.data.ActionEntity;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.UserRole;

public interface EntityFactory {
	
	public ElementEntity createNewElement(
			Location location,
			String name,
			String type,
			Date creationTimestamp,
			Boolean Expired,
			String creatorSmartspace,
			String creatorEmail,
			Map<String,Object> moreAttributes
			) ;

	
	public ActionEntity createNewAction(
			String elementSmartspace, 
			String elementId,
			String playerSmartspace,
			String playerId,
			String actionType,
			Date creationTimestamp,
			Map<String, Object> moreAttributes
			) ;
	
	public UserEntity createNewUser(
			 String userEmail,
			 String userName,
			 String avatar,
			 UserRole role,
			 Long points
			) ;
}
	
