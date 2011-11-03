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

package org.jboss.seam.spring.extension;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessProducer;

import org.jboss.seam.spring.context.SpringContext;
import org.jboss.seam.spring.inject.SpringBean;
import org.jboss.seam.spring.utils.Locations;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: Marius Bogoevici
 */
public class SpringContextBootstrapExtension implements Extension {

   
    public Map<String, ConfigurableApplicationContext> contextDefinitions = new HashMap<String, ConfigurableApplicationContext>();
    
    private Set<String> vetoedTypes = new HashSet<String>();

    public void handleSpringExtensionProducers(final @Observes ProcessProducer<?, Object> processProducer, final BeanManager beanManager) {
        if (processProducer.getAnnotatedMember().isAnnotationPresent(SpringBean.class)) {
            processProducer.setProducer(new SpringBeanProducer(processProducer, beanManager));
        } else if (processProducer.getAnnotatedMember().isAnnotationPresent(SpringContext.class)) {
            processProducer.setProducer(new SpringContextProducer(processProducer, processProducer.getProducer()));
        }
    }


    public void loadSpringContext(@Observes BeforeBeanDiscovery beforeBeanDiscovery, BeanManager beanManager) {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(Locations.SEAM_SPRING_CONTEXTS_LOCATION);
        if (inputStream != null) {
            Properties contextLocations = new Properties();
            try {
                contextLocations.load(inputStream);
                for (String contextName : contextLocations.stringPropertyNames()) {
                    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(contextLocations.getProperty(contextName));
                    contextDefinitions.put(contextName, context);
                    for (String beanDefinitionName : context.getBeanDefinitionNames()) {
                        vetoedTypes.add(context.getBeanFactory().getBeanDefinition(beanDefinitionName).getBeanClassName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
    
    public void registerSpringContextBeans(@Observes AfterBeanDiscovery afterBeanDiscovery) {
        for (String contextName : contextDefinitions.keySet()) {
            afterBeanDiscovery.addBean(new SpringContextBean(contextDefinitions.get(contextName), new SpringContextLiteral(contextName)));
        }
    }

    public void autoVeto(@Observes ProcessAnnotatedType<?> processAnnotatedType) {
        String name = processAnnotatedType.getAnnotatedType().getJavaClass().getName();
        if (vetoedTypes.contains(name)) {
            processAnnotatedType.veto();
        }
    }

}
