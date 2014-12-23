package gh.funthomas424242.junitsupport.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
 

@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface TestSuite {
    
	public String className() default "GeneratedTestSuite";
	public String packageName() default "tests";
	public String[] categories() default {"Integration","Modul"};
    public String superClass() default "org.junit.runners.Suite.class";
    		
    

}
