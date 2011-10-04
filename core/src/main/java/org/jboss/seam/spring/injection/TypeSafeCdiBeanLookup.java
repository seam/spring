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

import org.jboss.seam.spring.reflections.AnnotationInvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: Marius Bogoevici
 */
public class TypeSafeCdiBeanLookup<T> implements CdiBeanLookup<T>, ApplicationContextAware {

    private final Class<T> expectedType;

    private Qualifier[] qualifiers = new Qualifier[]{};

    private ConversionService conversionService;

    public TypeSafeCdiBeanLookup(Class<T> expectedType) {
        Assert.notNull(expectedType, "Expected type cannot be null");
        this.expectedType = expectedType;
    }

    public void setQualifiers(Qualifier[] qualifiers) {
        this.qualifiers = qualifiers;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            Object conversionServiceBeanInstance = applicationContext.getBean("conversionService");
            if (conversionServiceBeanInstance instanceof ConversionService) {
                this.conversionService = (ConversionService) conversionServiceBeanInstance;
            }
        } catch (BeansException e) {
          // do nothing
        }
        this.conversionService = new GenericConversionService();
    }

    @Override
    public T lookupBean(BeanManager beanManager) {
        try {
            List<Annotation> qualifierAnnotations = new ArrayList<Annotation>();
            for (Qualifier qualifier : qualifiers) {
                Class<? extends Annotation> qualifierClass = (Class<Annotation>) ClassUtils.getDefaultClassLoader().loadClass(qualifier.getClassName());
                if (!beanManager.isQualifier(qualifierClass)) {
                    throw new BeanCreationException(qualifierClass + " is not a valid JSR-299 qualifier");
                }
                AnnotationInvocationHandler annotationInvocationHandler = new AnnotationInvocationHandler(qualifierClass)
                        .withConversionService(conversionService)
                        .withAttributes(qualifier.getAttributes());
                qualifierAnnotations.add((Annotation) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class[]{qualifierClass}, annotationInvocationHandler));
            }
            Bean<?> resolvedBean = beanManager.resolve(beanManager.getBeans(expectedType, qualifierAnnotations.toArray(new Annotation[]{})));
            Assert.notNull(resolvedBean, "Cannot find a CDI bean");
            return (T) beanManager.getReference(resolvedBean, expectedType, beanManager.createCreationalContext(resolvedBean));
        } catch (Exception e) {
            throw new BeanCreationException("Cannot look up bean: ", e);
        }
    }


    @Override
    public Class<?> getExpectedType() {
        return expectedType;
    }

    public static class Qualifier {

        private String className;

        private Map<String, Object> attributes;


        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, Object> attributes) {
            this.attributes = attributes;
        }
    }
}
