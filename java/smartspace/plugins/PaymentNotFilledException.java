package smartspace.plugins;

public class PaymentNotFilledException extends Exception
{
    public PaymentNotFilledException(String msg){
        super(msg);
    }
    
    public PaymentNotFilledException(){
        super();
    }
}
