package org.jboss.seam.spring.bootstrap;

import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;
import java.util.Set;

/**
 * @author Marius Bogoevici
 */
class SpringContextProducer implements Producer {

    private final ProcessProducer<?, ?> processProducer;

    private final Producer<?> originalProducer;

    private boolean producedLocally = true;

    public SpringContextProducer(ProcessProducer<?, ?> processProducer, Producer<?> originalProducer) {
        this.processProducer = processProducer;
        this.originalProducer = originalProducer;
    }

    @Override
    public Object produce(CreationalContext ctx) {
        Configuration configuration = processProducer.getAnnotatedMember().getAnnotation(Configuration.class);
        if (configuration != null) {
            this.producedLocally = true;
            final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configuration.locations());
            return context;
        }
        Web web = processProducer.getAnnotatedMember().getAnnotation(Web.class);
        if (web != null) {
            final WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
            return context;
        }
        throw new IllegalStateException("Cannot produce a Spring ApplicationContext: the producer must be annotated" +
                "with one of: " + Web.class.getName() + ", " + Configuration.class.getName());
    }

    @Override
    public void dispose(Object instance) {
        if (this.producedLocally && instance instanceof ConfigurableApplicationContext) {
            ((ConfigurableApplicationContext) instance).close();
        }
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return originalProducer.getInjectionPoints();
    }
}
