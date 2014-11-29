package gh.funthomas424242.junitsupport.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value = { ElementType.TYPE, ElementType.METHOD })
public @interface TestCategories {

	public String[] names(); 
	
}
