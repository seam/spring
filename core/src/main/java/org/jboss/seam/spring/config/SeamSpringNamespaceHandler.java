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

package org.jboss.seam.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * A {@link org.springframework.beans.factory.xml.NamespaceHandler} for Spring/CDI integration.
 *
 * @author: Marius Bogoevici
 */
public class SeamSpringNamespaceHandler extends NamespaceHandlerSupport{

    public static final String BEAN_MANAGER_ELEMENT_NAME = "bean-manager";
    private static final String BEAN_REFERENCE = "bean-reference";

    @Override
    public void init() {
        registerBeanDefinitionParser(BEAN_MANAGER_ELEMENT_NAME, new BeanManagerBeanDefinitionParser());
        registerBeanDefinitionParser(BEAN_REFERENCE, new CdiBeanImportBeanDefinitionParser());
    }
}
