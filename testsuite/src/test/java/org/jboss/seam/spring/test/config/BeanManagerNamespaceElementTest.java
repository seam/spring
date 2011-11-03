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

package org.jboss.seam.spring.test.config;

import static org.jboss.seam.spring.test.utils.Dependencies.dependencies;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.spring.context.SpringContext;
import org.jboss.seam.spring.test.bootstrap.WebContextProducer;
import org.jboss.seam.spring.test.utils.ContextInjected;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;

/**
 * @author: Marius Bogoevici
 */
@RunWith(Arquillian.class)
public class BeanManagerNamespaceElementTest {


        @Deployment
        public static Archive<?> deployment() {
            return ShrinkWrap.create(WebArchive.class, "test.war")
                    .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                    .setWebXML("org/jboss/seam/spring/test/common/web.xml")
                    .addAsWebInfResource("org/jboss/seam/spring/test/config/webinf/applicationContext.xml", "applicationContext.xml")
                    .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                    .addAsLibraries(dependencies())
                    .addClasses(WebContextProducer.class, ContextInjected.class);
        }

        @Test
        public void testSimpleBean(@SpringContext ApplicationContext applicationContext) {
            Assert.assertNotNull(applicationContext);
            BeanManager beanManager = applicationContext.getBean(BeanManager.class);
            Assert.assertNotNull(beanManager);
        }

}
