package smartspace.init;
import org.springframework.stereotype.Component;

@Component
public class Demo {
	public Demo(){
		System.err.println("Demo created!");
	}

	public Demo(String value){
		System.err.println("Demo created with value: "  + value + " !");
	}

	public String toString() {
		return "I am a generic demo";
	}
}