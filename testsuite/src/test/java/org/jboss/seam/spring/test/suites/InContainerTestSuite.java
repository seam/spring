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

package org.jboss.seam.spring.test.suites;

import org.jboss.seam.spring.test.bootstrap.SpringBootstrapInContainerTest;
import org.jboss.seam.spring.test.bootstrap.SpringWebContextAccessTest;
import org.jboss.seam.spring.test.config.BeanImportNamespaceTest;
import org.jboss.seam.spring.test.config.BeanManagerNamespaceElementTest;
import org.jboss.seam.spring.test.injection.InjectionOfCdiBeanWithWebContextTest;
import org.jboss.seam.spring.test.injection.SpringBootstrapByExtensionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Marius Bogoevici
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
        { SpringBootstrapInContainerTest.class,
                SpringBootstrapByExtensionTest.class,
                SpringWebContextAccessTest.class,
                InjectionOfCdiBeanWithWebContextTest.class,
                BeanImportNamespaceTest.class,
                BeanManagerNamespaceElementTest.class})
public class InContainerTestSuite {
}