package org.jboss.seam.spring.bootstrap;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;

import org.jboss.seam.spring.injection.SpringBean;
import org.springframework.context.ApplicationContext;

/**
 * Producer class for Spring bean instances that are injected into CDI injection points.
 *
 * @author Marius Bogoevici
 */
class SpringBeanProducer implements Producer<Object> {
    private final ProcessProducer<?, Object> processProducer;
    private final BeanManager beanManager;

    public SpringBeanProducer(ProcessProducer<?, Object> processProducer, BeanManager beanManager) {
        this.processProducer = processProducer;
        this.beanManager = beanManager;
    }

    @Override
    public Object produce(CreationalContext<Object> ctx) {
        String contextName = processProducer.getAnnotatedMember().getAnnotation(SpringBean.class).fromContext();
        Map<String, Object> attributeValues = new HashMap<String, Object>();
        attributeValues.put("name", contextName);
        Bean<?> resolve = beanManager.resolve(beanManager.getBeans(ApplicationContext.class, new SpringContextLiteral(contextName)));
        ApplicationContext applicationContextInstance = (ApplicationContext) beanManager.getReference(resolve, ApplicationContext.class, beanManager.createCreationalContext(resolve));
        Field producerField = (Field) processProducer.getAnnotatedMember().getJavaMember();
        return applicationContextInstance.getBean(producerField.getType());
    }

    @Override
    public void dispose(Object instance) {
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints() {
        return Collections.emptySet();
    }
}
