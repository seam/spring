package org.jboss.seam.spring.test.reflections;

/**
 * @author Marius Bogoevici
 */
public @interface Team {
    
    String name();
    
    int size() default 15;
}
