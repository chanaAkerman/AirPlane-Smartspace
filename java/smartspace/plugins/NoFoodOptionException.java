package smartspace.plugins;

public class NoFoodOptionException extends Exception
{
    public NoFoodOptionException(String message){
        super(message);
    }
    public NoFoodOptionException(){
        super();
    }
}
