package gh.funthomas424242.junitsupport.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(value = {ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface TestSuite {
    
    public String parent() default "org.junit.runners.Suite";
    		
    public String[] categories() default {"Integration","Modul"};

}
