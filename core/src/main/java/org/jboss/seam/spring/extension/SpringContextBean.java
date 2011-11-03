package org.jboss.seam.spring.extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.jboss.seam.spring.context.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Marius Bogoevici
 */
public class SpringContextBean implements Bean<ApplicationContext> {

    private ConfigurableApplicationContext context;

    private SpringContext qualifier;

    public SpringContextBean(ConfigurableApplicationContext context, SpringContext qualifier) {
        this.context = context;
        this.qualifier = qualifier;
    }

    @Override
    public Set<Type> getTypes() {
        return Collections.<Type>singleton(ApplicationContext.class);
    }

    @Override
    public Set<Annotation> getQualifiers() {
        return Collections.<Annotation>singleton(qualifier);
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return ApplicationScoped.class;
    }

    @Override
    public String getName() {

        return null;
    }

    @Override
    public Set<Class<? extends Annotation>> getStereotypes() {
        return Collections.emptySet();
    }

    @Override
    public Class<?> getBeanClass() {
        return ClassPathXmlApplicationContext.class;
    }

    @Override
    public boolean isAlternative() {
        return false;
    }

    @Override
    public boolean isNullable() {
        return false;
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }

    @Override
    public ApplicationContext create(CreationalContext<ApplicationContext> applicationContextCreationalContext) {
        return context;
    }

    @Override
    public void destroy(ApplicationContext instance, CreationalContext<ApplicationContext> applicationContextCreationalContext) {
       context.stop();
    }
}
