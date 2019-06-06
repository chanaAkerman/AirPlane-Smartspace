package smartspace.data;

public class Location implements Comparable<Location>{
	private double x;
	private double y;
	public Location() {
		super();
	}
	public Location(double x, double y) {
		super();
		setX(x);
		setY(y);
	}
	@Override
	public String toString() {
		return "Location [x=" + x + ", y=" + y + "]";
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		if(Double.toString(x)==null) {
			this.x=0.0;
		}
		else {
			this.x = x;
		}
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		if(Double.toString(y)==null) {
			this.y=0.0;
		}
		else {
			this.y = y;
		}
	}
	@Override
	public int compareTo(Location l) {
		return 0;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

}
