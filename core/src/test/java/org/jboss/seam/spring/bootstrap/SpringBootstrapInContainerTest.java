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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.spring.support.ContextInjected;
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


/**
 * @author: Marius Bogoevici
 */

@RunWith(Arquillian.class)

public class SpringBootstrapInContainerTest {

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("org/jboss/seam/spring/bootstrap/applicationContext.xml")
                .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                .addAsLibraries(DependencyResolvers.use(MavenDependencyResolver.class)
                        .artifact("org.springframework:spring-context-support:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-beans:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-context:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-core:3.0.5.RELEASE")
                        .artifact("org.springframework:spring-web:3.0.5.RELEASE")
                        .artifact("commons-logging:commons-logging:1.1.1")
                        .artifact("org.slf4j:slf4j-simple:1.6.1")
                        .resolveAs(JavaArchive.class))
                .addClasses(ConfigurationContextProducer.class, ContextInjected.class);
    }

    @Test
    public void testSimpleBean(ContextInjected contextInjected) {
        Assert.assertNotNull(contextInjected);
    }


}
