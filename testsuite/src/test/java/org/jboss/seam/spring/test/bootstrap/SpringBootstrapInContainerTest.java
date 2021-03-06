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

package org.jboss.seam.spring.test.bootstrap;

import static org.jboss.seam.spring.test.utils.Dependencies.dependencies;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.spring.test.injection.ComplicatedBean;
import org.jboss.seam.spring.test.injection.SimpleBean;
import org.jboss.seam.spring.test.utils.ContextInjected;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
                .addAsResource("org/jboss/seam/spring/test/bootstrap/applicationContext.xml")
                .addAsLibraries(dependencies())
                .addClasses(ContextInjected.class, ConfigurationContextProducer.class,
                        SimpleBean.class, ComplicatedBean.class);
    }

    @Test
    public void testSimpleBean(ContextInjected contextInjected) {
        Assert.assertNotNull(contextInjected);
    }


}
