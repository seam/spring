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

package org.jboss.seam.spring.namespace;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * A {@link BeanDefinitionParser} for the <code>&lt;bean-manager&gt;</code> element.
 *
 * @author: Marius Bogoevici
 */
public class BeanManagerBeanDefinitionParser extends AbstractBeanDefinitionParser {
    
    private static final String JNDI_BEAN_MANAGER_LOCATOR_CLASS = "org.jboss.seam.spring.namespace.JndiBeanManagerLocator";

    public static final String DEFAULT_BEAN_MANAGER_ID = "cdiBeanManager";

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(JNDI_BEAN_MANAGER_LOCATOR_CLASS);
        return builder.getBeanDefinition();
    }

    @Override
    protected String resolveId(org.w3c.dom.Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
        String idAttributeValue = element.getAttribute(ID_ATTRIBUTE);
        if (StringUtils.hasText(idAttributeValue)) {
            return idAttributeValue;
        }
        return DEFAULT_BEAN_MANAGER_ID;
    }
}
