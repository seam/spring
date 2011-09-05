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

package org.jboss.seam.spring.injection;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.spring.bootstrap.*;
import org.jboss.seam.spring.reflections.AnnotationInvocationHandler;
import org.jboss.seam.spring.reflections.Annotations;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

/**
 * @author: Marius Bogoevici
 */
@RunWith(Arquillian.class)
public class SpringBeanInjectionTest {

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("org/jboss/seam/spring/bootstrap/applicationContext.xml")
                .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                .addAsResource("META-INF/services/org.jboss.seam.spring.contexts")
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
                        .artifact("org.springframework:spring-context-support:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-beans:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-context:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-core:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-web:3.0.5.RELEASE")
                        .artifact("org.jboss.seam.solder:seam-solder:3.0.0.Final")
                        .artifact("commons-logging:commons-logging:1.1.1")
                        .artifact("org.slf4j:slf4j-simple:1.6.1")
                        .resolveAs(JavaArchive.class))
                .addClasses(ContextInjected.class, SimpleBean.class,
                        SpringContext.class, Annotations.class, AnnotationInvocationHandler.class,
                        SpringBean.class, SpringInjected.class, SimpleBeanProducer.class, ComplicatedBean.class,
                        SpringContextBootstrapExtension.class,
                        Web.class, Configuration.class, SpringContextLiteral.class);
    }

    @Inject SpringInjected mySpringInjected;

    @Test
    public void testCdiBeanInjectedWithSpringBean(SpringInjected springInjected) {
        Assert.assertNotNull(springInjected);
        Assert.assertNotNull(springInjected.simpleBean);
        Assert.assertEquals(springInjected.simpleBean.getMessage(), "Hello");
        Assert.assertNotNull(springInjected.complicatedBean);
        Assert.assertNotNull(springInjected.complicatedBean.simpleBean);
        Assert.assertEquals(springInjected.complicatedBean.simpleBean.getMessage(), "Hello");
    }

}
