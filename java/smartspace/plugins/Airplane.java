package smartspace.plugins;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import org.springframework.web.client.RestTemplate;
import smartspace.data.ElementEntity;
import smartspace.data.Location;
import smartspace.data.UserEntity;
import smartspace.data.util.EntityFactory;
import smartspace.data.util.EntityFactoryImpl;
import smartspace.layout.ElementBoundary;
import smartspace.layout.UserBoundary;

public class Airplane
{
	private RestTemplate restTemplate = new RestTemplate();
	private String elementsUrl = ClientApplication.url+"/elements";
	private String usersUrl = ClientApplication.url+"/users";
    public int numSeatsPerRow, numRows, numRowsBusiness, numRowsFirst;
    private double avgTicketPrice;
    private String[] rowLetter = {null, "A", "B", "C", "D", "E", "F", "G", "H"};
    public ArrayList<AirplaneSeat> seatList;
    public AirplaneSeat[][] seatListArray;
    public ElementEntity[][] elements;
    public UserEntity[][] userListArray;
    public AirplaneSeat[][] preReservation;
    public UserEntity creatorAdmin;
    public EntityFactory factory = new EntityFactoryImpl();
   
    @Override
	public String toString() {
		return "Airplane [numSeatsPerRow=" + numSeatsPerRow + ", numRows=" + numRows + ", numRowsBusiness="
				+ numRowsBusiness + ", numRowsFirst=" + numRowsFirst + ", avgTicketPrice=" + avgTicketPrice
				+ ", rowLetter=" + Arrays.toString(rowLetter) + ", seatList=" + seatList.toString() + ", seatListArray="
				+ Arrays.toString(seatListArray) + ", userListArray=" + Arrays.toString(userListArray) + ", factory="
				+ factory + "]";
	}
    
    public Airplane(UserEntity admin,int cols, int rowsTotal, int first, int business, double ticketPrice){
        numSeatsPerRow = cols;
        numRows = rowsTotal;
        numRowsBusiness = business;
        numRowsFirst = first;
        avgTicketPrice = ticketPrice;
        seatList = new ArrayList<AirplaneSeat>(cols * rowsTotal);
        seatListArray = new AirplaneSeat[rowsTotal][cols];
        userListArray = new UserEntity[rowsTotal][cols];
        elements = new ElementEntity[rowsTotal][cols];
        //fillAirplane();
        creatorAdmin = admin;
        createAirplane();
    }
    
    public void Confirm(int x, int y,UserEntity user) {	
    	seatListArray[x][y].setReserved(true);
		seatListArray[x][y].setConfirmed(true);
		seatListArray[x][y].setUserKey(user.getKey());
		userListArray[x][y]=user;
		
		ElementBoundary elementBoundary = new ElementBoundary(elements[x][y]);
    	
    	HashMap<String,Object>map = new HashMap<>();
		map.put("userKey", user.getKey());
		map.put("category",seatListArray[x][y].getCategory() );
		map.put("meal", seatListArray[x][y].getMeal());
		map.put("confrimed", seatListArray[x][y].isConfirmed());
		map.put("reserved", seatListArray[x][y].isReserved());
		
		elementBoundary.setMoreAttributes(map);
		this.restTemplate
		.put(this.elementsUrl+"/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
				elementBoundary,user.getUserSmartspace(),user.getUserEmail(),elementBoundary.getElementSmartspace(),elementBoundary.getElementId());
    }
	
    public void Unconfirm(int x, int y,UserEntity user) {		
		seatListArray[x][y].setReserved(false);
		seatListArray[x][y].setConfirmed(false);
		seatListArray[x][y].setUserKey(null);
		userListArray[x][y]=null;
		elements[x][y].setMoreAttributes(null);
		ElementBoundary elementBoundary = new ElementBoundary(elements[x][y]);
		
		HashMap<String,Object>map = new HashMap<>();
		map.put("userKey", null);
		map.put("category",seatListArray[x][y].getCategory() );
		map.put("meal", null);
		map.put("confrimed", seatListArray[x][y].isConfirmed());
		map.put("reserved", seatListArray[x][y].isReserved());

		elementBoundary.setMoreAttributes(map);

		this.restTemplate
		.put(this.elementsUrl+"/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
				elementBoundary,user.getUserSmartspace(),user.getUserEmail(),elementBoundary.getElementSmartspace(),elementBoundary.getElementId());
		
		
	}
    
	private void createAirplane() {
    	for(int r = 1; r <= numRows; r++){
            for(int c = 1; c <= numSeatsPerRow; c++){
            	if(r <= numRowsFirst){
                    boolean reserve = false;
                    AirplaneSeat firstSeat = new AirplaneSeat(reserve, 3, avgTicketPrice, r + rowLetter[c]);
                    seatList.add(firstSeat);
                    seatListArray[r-1][c-1] = firstSeat;
                    elements[r-1][c-1] =this.factory.createNewElement(new Location(r-1, c-1),firstSeat.getSeatName() ,
                    		"FIRST", new Date(), false, "2019B.chana", creatorAdmin.getUserEmail(), new HashMap<>());
                    ElementBoundary elementBoundary = new ElementBoundary(elements[r-1][c-1]);
                    ElementBoundary result =  this.restTemplate
        			.postForObject(
        					this.elementsUrl+"/{managerSmartspace}/{managerEmail}", 
        					elementBoundary, 
        					ElementBoundary.class,creatorAdmin.getUserSmartspace(),creatorAdmin.getUserEmail());
                    
                    
                    
                    
                    
                    
                }else if(r > numRowsFirst && r <= numRowsBusiness + numRowsFirst){
                    boolean reserve = false;
                    AirplaneSeat businessSeat = new AirplaneSeat(reserve, 2, avgTicketPrice, r + rowLetter[c]);
                    seatList.add(businessSeat);
                    seatListArray[r-1][c-1] = businessSeat;
                    elements[r-1][c-1] =this.factory.createNewElement(new Location(r-1, c-1), businessSeat.getSeatName() ,
                    		"BUSINESS", new Date(), false, "2019B.chana", creatorAdmin.getUserEmail(), new HashMap<>());
                    ElementBoundary elementBoundary = new ElementBoundary(elements[r-1][c-1]);
                    ElementBoundary result =  this.restTemplate
        			.postForObject(
        					this.elementsUrl+"/{managerSmartspace}/{managerEmail}", 
        					elementBoundary, 
        					ElementBoundary.class,creatorAdmin.getUserSmartspace(),creatorAdmin.getUserEmail());
                    
            		
            
                }else{
                    boolean reserve = false;
                    AirplaneSeat economySeat = new AirplaneSeat(reserve,1, avgTicketPrice, r + rowLetter[c]);
                    seatList.add(economySeat);
                    seatListArray[r-1][c-1] = economySeat;
                    elements[r-1][c-1] =this.factory.createNewElement(new Location(r-1, c-1),economySeat.getSeatName(), 
                    		"ECONOMY", new Date(), false, "2019B.chana", creatorAdmin.getUserEmail(), new HashMap<>());
                    ElementBoundary elementBoundary = new ElementBoundary(elements[r-1][c-1]);
                    ElementBoundary result =  this.restTemplate
        			.postForObject(
        					this.elementsUrl+"/{managerSmartspace}/{managerEmail}", 
        					elementBoundary, 
        					ElementBoundary.class,creatorAdmin.getUserSmartspace(),creatorAdmin.getUserEmail());
                    
                    
                }
            	
            }
    	}
    	new AlertBox("Create","The Airplane has Successfully created");
    }
    
    public void saveAirplane() {
    	for(int r = 1; r <= numRows; r++){
            for(int c = 1; c <= numSeatsPerRow; c++){
                    	if(r <= numRowsFirst && seatListArray[r-1][c-1].isReserved()){
                                boolean reserve =true;
                                AirplaneSeat firstSeat = new AirplaneSeat(reserve, 3, avgTicketPrice, r + rowLetter[c]);
                                seatList.add(firstSeat);
                                seatListArray[r-1][c-1] = firstSeat;
                        }else if(r <= numRowsFirst && !seatListArray[r-1][c-1].isReserved()) {
                        		boolean reserve =false;
                                AirplaneSeat firstSeat = new AirplaneSeat(reserve, 3, avgTicketPrice, r + rowLetter[c]);
                                seatList.add(firstSeat);
                                seatListArray[r-1][c-1] = firstSeat;
                        }
                    	if(r > numRowsFirst && r <= numRowsBusiness + numRowsFirst && seatListArray[r-1][c-1].isReserved()){
                            	boolean reserve =true;
                            	AirplaneSeat businessSeat = new AirplaneSeat(reserve, 2, avgTicketPrice, r + rowLetter[c]);
                            	seatList.add(businessSeat);
                            	seatListArray[r-1][c-1] = businessSeat;
                    	}else if (r > numRowsFirst && r <= numRowsBusiness + numRowsFirst && !seatListArray[r-1][c-1].isReserved()) {
                    			boolean reserve =false;
                    			AirplaneSeat businessSeat = new AirplaneSeat(reserve, 2, avgTicketPrice, r + rowLetter[c]);
                    			seatList.add(businessSeat);
                    			seatListArray[r-1][c-1] = businessSeat;
                    	}
                        if(r > numRowsBusiness + numRowsFirst && r<=numRows && seatListArray[r-1][c-1].isReserved()) {
                        	boolean reserve =true;
                        	AirplaneSeat economySeat = new AirplaneSeat(reserve,1, avgTicketPrice, r + rowLetter[c]);
                            seatList.add(economySeat);
                            seatListArray[r-1][c-1] = economySeat;
                        }
                        else if (r > numRowsBusiness + numRowsFirst && r<=numRows && !seatListArray[r-1][c-1].isReserved()){
                        	boolean reserve =false;
                        	AirplaneSeat economySeat = new AirplaneSeat(reserve,1, avgTicketPrice, r + rowLetter[c]);
                            seatList.add(economySeat);
                            seatListArray[r-1][c-1] = economySeat;
                        }
                    	
                    }
            	}
    	}
    
    public void cancleReservation(int x,int y,UserEntity user) {
    	seatListArray[x][y].setReserved(false);
		seatListArray[x][y].setConfirmed(false);
		seatListArray[x][y].setUserKey(null);
		userListArray[x][y]=null;
		elements[x][y].setMoreAttributes(null);
		ElementBoundary elementBoundary = new ElementBoundary(elements[x][y]);
		
		HashMap<String,Object>map = new HashMap<>();
		map.put("userKey", null);
		map.put("category",seatListArray[x][y].getCategory() );
		map.put("meal", null);
		map.put("confrimed", seatListArray[x][y].isConfirmed());
		map.put("reserved", seatListArray[x][y].isReserved());

		elementBoundary.setMoreAttributes(map);

		this.restTemplate
		.put(this.elementsUrl+"/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
				elementBoundary,user.getUserSmartspace(),user.getUserEmail(),elementBoundary.getElementSmartspace(),elementBoundary.getElementId());
    }
  
	
    public void saveReservation(int x, int y,UserEntity user) {
    	userListArray[x][y] = user;
    	seatListArray[x][y].setReserved(true);
    	seatListArray[x][y].setConfirmed(false);
    	seatListArray[x][y].setUserKey(user.getKey());
    	
    	UserBoundary response= this.restTemplate.getForObject(
				this.usersUrl+"/login/{userSmartspace}/{userEmail}",
				UserBoundary.class,user.getUserSmartspace(),user.getUserEmail());
		// add 10 points for reservation
		response.setPoints(response.getPoints()+10);
		
		this.restTemplate.put(
				this.usersUrl+"/login/{userSmartspace}/{userEmail}", 
				response,response.getUserSmartspace(),response.getUserEmail());
    	
    	ElementBoundary elementBoundary = new ElementBoundary(elements[x][y]);
    	
    	HashMap<String,Object>map = new HashMap<>();
		map.put("userKey", user.getKey());
		map.put("category",seatListArray[x][y].getCategory() );
		map.put("meal", seatListArray[x][y].getMeal());
		map.put("confrimed", seatListArray[x][y].isConfirmed());
		map.put("reserved", seatListArray[x][y].isReserved());
		
		elementBoundary.setMoreAttributes(map);
		this.restTemplate
		.put(this.elementsUrl+"/{managerSmartspace}/{managerEmail}/{elementSmartspace}/{elementId}", 
				elementBoundary,
				user.getUserSmartspace(),user.getUserEmail(),
				elementBoundary.getElementSmartspace(),elementBoundary.getElementId());
		
    }
}
