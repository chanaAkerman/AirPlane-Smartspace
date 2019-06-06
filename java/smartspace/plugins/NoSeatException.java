package smartspace.plugins;

public class NoSeatException extends Exception
{
    public NoSeatException(String msg){
        super(msg);
    }
    
    public NoSeatException(){
        super();
    }
}
