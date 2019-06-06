package smartspace.plugins;
public class AirplaneSeat// Airplane HAS-A(some) airplaneSeats
{
	private String userKey;
    private boolean reserved;
    private boolean confirmed;
    private int category;//1== economy, 2 == business, 3 == first
    private double price;
    private Meal meal;
	private String seatName;
    public static boolean isConfirmed(AirplaneSeat seat) {
		return seat.confirmed;
	}
    public boolean isConfirmed() {
		return confirmed;
	}
	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

    //precondition - type is in range (1, 3) inclusive, price is posotive
    public AirplaneSeat(boolean res, int type, double cost, String name){
        reserved = res;
        category = type;
        price = cost;
        seatName = name;
        meal= Meal.Standard;
        if(type == 2){
            price *= 1.4;
        }else if(type == 3){
            price *= 1.7;
        }
    }
    //accessor for reserved
    public static boolean isReserved(AirplaneSeat seat){
        return seat.reserved;
    }
    //accessor for seat price
    public static double getPrice(AirplaneSeat seat){
        return seat.price;
    }
    //accessor for seat name
    public static String getName(AirplaneSeat seat){
        return seat.seatName;
    }
    //accessor for seat category as int value
    public static int getCategoryInt(AirplaneSeat seat){
        return seat.category;
    }
    //accessor for seat category as String representation
    public static String getCategory(AirplaneSeat seat){
        if(seat.category == 1){
            return "Economy Class";
        }else if(seat.category == 2){
            return "Business Class";
        }else {
            return "First Class";
        }
    }
    public static void changeMeal(AirplaneSeat seat,Meal meal){
        seat.setMeal(meal);
    }


	public String getUserKey() {
		return userKey;
	}


	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}


	public boolean isReserved() {
		return reserved;
	}


	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}


	public int getCategory() {
		return category;
	}


	public void setCategory(int category) {
		this.category = category;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getSeatName() {
		return seatName;
	}


	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	 public Meal getMeal() {
			return meal;
		}


		public void setMeal(Meal meal) {
			this.meal = meal;
		}


		@Override
		public String toString() {
			return "AirplaneSeat [userKey=" + userKey + ", reserved=" + reserved + ", confirmed=" + confirmed
					+ ", category=" + category + ", price=" + price + ", meal=" + meal + ", seatName=" + seatName + "]";
		}
		
    
    
    
}