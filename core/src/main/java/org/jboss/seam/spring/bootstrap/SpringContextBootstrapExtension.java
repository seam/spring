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

import org.jboss.arquillian.core.impl.ExtensionImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessProducer;
import javax.enterprise.inject.spi.Producer;
import java.util.Set;

/**
 * @author: Marius Bogoevici
 */
public class SpringContextBootstrapExtension implements Extension {

    public void bootstrapContext(final @Observes ProcessProducer<?,ApplicationContext> springContextProducer) {

        final Producer<ApplicationContext> originalProducer = springContextProducer.getProducer();
        springContextProducer.setProducer(new Producer<ApplicationContext>() {

            @Override
            public ApplicationContext produce(CreationalContext<ApplicationContext> ctx) {
                Configuration annotation = springContextProducer.getAnnotatedMember().getAnnotation(Configuration.class);
                if (annotation != null) {
                   return new ClassPathXmlApplicationContext(annotation.locations());
                }
                throw new IllegalStateException("Cannot find locations");
            }

            @Override
            public void dispose(ApplicationContext instance) {
                if (instance instanceof ConfigurableApplicationContext) {
                    ((ConfigurableApplicationContext)instance).close();
                }
            }

            @Override
            public Set<InjectionPoint> getInjectionPoints() {
                return originalProducer.getInjectionPoints();
            }
        });
    }
}
