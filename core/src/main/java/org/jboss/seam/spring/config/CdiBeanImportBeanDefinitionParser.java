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

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.jboss.seam.spring.config.BeanManagerBeanDefinitionParser.DEFAULT_BEAN_MANAGER_ID;
import static org.jboss.seam.spring.injection.TypeSafeCdiBeanLookup.Qualifier;

/**
 * @author: Marius Bogoevici
 */
public class CdiBeanImportBeanDefinitionParser extends AbstractBeanDefinitionParser {

    public static final String FACTORY_BEAN_CLASS_NAME = "org.jboss.seam.spring.injection.CdiBeanFactoryBean";

    public static final String BEAN_MANAGER_REFERENCE = "bean-manager";
    public static final String TYPE_SAFE_BEAN_LOOKUP_CLASS_NAME = "org.jboss.seam.spring.injection.TypeSafeCdiBeanLookup";

    @Override
    public AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder cdiBeanFactoryBuilder = BeanDefinitionBuilder.rootBeanDefinition(FACTORY_BEAN_CLASS_NAME);
        String beanManagerReference = element.getAttribute(BEAN_MANAGER_REFERENCE);
        if (beanManagerReference != null) {
            cdiBeanFactoryBuilder.addPropertyReference("beanManager", beanManagerReference);
        } else {
            cdiBeanFactoryBuilder.addPropertyReference("beanManager", DEFAULT_BEAN_MANAGER_ID);
        }

        String name = element.getAttribute(NAME_ATTRIBUTE);

        String type = element.getAttribute("type");

        if (!StringUtils.hasText(name) && !StringUtils.hasText(type)) {
            parserContext.getReaderContext().error("A CDI bean reference must specify at least a name or the bean type", element);
        }



        ArrayList<Qualifier> qualifiers = new ArrayList<Qualifier>();

        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node childNode = children.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && "qualifier".equals(childNode.getLocalName())) {
                Element qualifierElement = (Element) childNode;
                Qualifier qualifier = new Qualifier();
                Map<String, Object> attributes = new HashMap<String, Object>();
                qualifier.setClassName(qualifierElement.getAttribute("type"));

                if (qualifierElement.hasChildNodes()) {
                    NodeList attributeNodes = qualifierElement.getChildNodes();
                    for (int j = 0; j < attributeNodes.getLength(); j++) {
                        Node attributeNode = attributeNodes.item(j);
                        if (attributeNode.getNodeType() == Node.ELEMENT_NODE && "attribute".equals(attributeNode.getLocalName())) {
                            Element attributeElement = (Element) attributeNode;
                            String attributeName = attributeElement.getAttribute("name");
                            String attributeValue = attributeElement.getAttribute("value");
                            if (!StringUtils.hasText(attributeName) || !StringUtils.hasText(attributeValue)) {
                                parserContext.getReaderContext().error("Qualifier attributes must have both a name and a value", attributeElement);
                            }
                            attributes.put(attributeName, attributeValue);
                        }
                    }
                }
                qualifier.setAttributes(attributes);
                qualifiers.add(qualifier);
            }
        }
        if (StringUtils.hasText(name) && !qualifiers.isEmpty()) {
            parserContext.getReaderContext().error("A bean reference must either specify a name or qualifiers but not both", element);
        }

        BeanDefinitionBuilder lookupBuilder = BeanDefinitionBuilder.rootBeanDefinition(TYPE_SAFE_BEAN_LOOKUP_CLASS_NAME);
        lookupBuilder.addConstructorArgValue(type);
        lookupBuilder.addPropertyValue("qualifiers", qualifiers);
        AbstractBeanDefinition lookupBeanDefinition = lookupBuilder.getBeanDefinition();
        String lookupBeanName = parserContext.getReaderContext().generateBeanName(lookupBeanDefinition);
        BeanDefinitionReaderUtils.registerBeanDefinition(new BeanDefinitionHolder(lookupBeanDefinition, lookupBeanName), parserContext.getRegistry());
        cdiBeanFactoryBuilder.addPropertyReference("cdiBeanLookup", lookupBeanName);

        return cdiBeanFactoryBuilder.getBeanDefinition();
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
        String id = element.getAttribute(ID_ATTRIBUTE);
        if (!StringUtils.hasText(id)) {
            if (!parserContext.isNested()) {
                parserContext.getReaderContext().error("A root level CDI bean reference must specify an id", element);
            }
            id = parserContext.getReaderContext().generateBeanName(definition);
        }
        return id;
    }
}
