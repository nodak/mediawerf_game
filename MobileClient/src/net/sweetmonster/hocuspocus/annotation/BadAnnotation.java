package net.sweetmonster.hocuspocus.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented  
@Target(ElementType.FIELD) 
@Retention(RetentionPolicy.RUNTIME) 
@Inherited 
public @interface BadAnnotation { 
	
	public String name() default ""; 
	public float min() default 0; 
	public float max() default 1;  
	

} 
