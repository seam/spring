/*
 * JBoss, Home of Professional Open Source
 * Copyright [2011], Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.seam.spring.bootstrap;

import org.jboss.seam.spring.injection.SpringBean;
import org.jboss.seam.spring.reflections.Annotations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ContextLoader;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.spi.*;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author: Marius Bogoevici
 */
public class SpringContextBootstrapExtension implements Extension {

    public final Map<String, ApplicationContext> contexts = new HashMap<String, ApplicationContext>();
    private Set<String> vetoedTypes = new HashSet<String>();

    public void importBean(final @Observes ProcessProducer<?, Object> processProducer, final BeanManager beanManager) {
        if (processProducer.getAnnotatedMember().isAnnotationPresent(SpringBean.class)) {
            overrideProducedBean(processProducer, beanManager);
        } else if (processProducer.getAnnotatedMember().isAnnotationPresent(SpringContext.class)) {
            overrideProduceContext(processProducer);
        }
    }

    private void overrideProducedBean(final ProcessProducer<?, Object> processProducer, final BeanManager beanManager) {
        processProducer.setProducer(new Producer<Object>() {
            @Override
            public Object produce(CreationalContext<Object> ctx) {
                String value = processProducer.getAnnotatedMember().getAnnotation(SpringBean.class).fromContext();
                Bean<? extends Object> resolve = beanManager.resolve(beanManager.getBeans(ApplicationContext.class, new SpringContextLiteral(value) ));
                ApplicationContext applicationContextInstance = (ApplicationContext) beanManager.getReference(resolve, ApplicationContext.class, beanManager.createCreationalContext(resolve));
                Field producerField = (Field) processProducer.getAnnotatedMember().getJavaMember();
                return  applicationContextInstance.getBean(producerField.getType());
            }
            @Override
            public void dispose(Object instance) {
            }
            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return Collections.emptySet();
            }
        });
    }


    private void overrideProduceContext(final ProcessProducer<?, ?> processProducer) {
        final Producer<?> originalProducer = processProducer.getProducer();
        processProducer.setProducer(new Producer() {
            @Override
            public Object produce(CreationalContext ctx) {
                Configuration configuration = processProducer.getAnnotatedMember().getAnnotation(Configuration.class);
                if (configuration != null) {
                    return new ClassPathXmlApplicationContext(configuration.locations());
                }
                Web web = processProducer.getAnnotatedMember().getAnnotation(Web.class);
                if (web != null) {
                    return ContextLoader.getCurrentWebApplicationContext();
                }
                throw new IllegalStateException("Cannot find locations");
            }
            @Override
            public void dispose(Object instance) {
                if (instance instanceof ConfigurableApplicationContext) {
                    ((ConfigurableApplicationContext) instance).close();
                }
            }
            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return originalProducer.getInjectionPoints();
            }
        });
    }

    public void loadSpringContext(@Observes BeforeBeanDiscovery beforeBeanDiscovery) {
        InputStream inputStream = getClass().getResourceAsStream("/META-INF/services/org.jboss.seam.spring.contexts");
        if (inputStream != null) {
            Properties contextLocations = new Properties();
            try {
                contextLocations.load(inputStream);
                for (String contextName : contextLocations.stringPropertyNames()) {
                    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextLocations.getProperty(contextName));
                    this.contexts.put(contextName, context);
                    for (String beanDefinitionName : context.getBeanDefinitionNames()) {
                        vetoedTypes.add(context.getBeanFactory().getBeanDefinition(beanDefinitionName).getBeanClassName());
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void autoVeto(@Observes ProcessAnnotatedType<?> processAnnotatedType) {
        String name = processAnnotatedType.getAnnotatedType().getJavaClass().getName();
        if (vetoedTypes.contains(name)) {
            processAnnotatedType.veto();
        }
    }
}
