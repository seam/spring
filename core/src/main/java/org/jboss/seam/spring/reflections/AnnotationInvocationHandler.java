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

package org.jboss.seam.spring.reflections;

import org.springframework.core.convert.ConversionService;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link InvocationHandler} implementation for dynamically instantiating annotations
 *
 * @author: Marius Bogoevici
 */
public class AnnotationInvocationHandler implements InvocationHandler, Annotation {

    private Map<String, Object> registeredValues = new HashMap<String, Object>();

    private ConversionService conversionService = null;

    private Class<? extends Annotation> annotationType;

    public AnnotationInvocationHandler(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    public static AnnotationInvocationHandler ofType(Class<Annotation> annotationType) {
        return new AnnotationInvocationHandler(annotationType);
    }

    public AnnotationInvocationHandler withConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
        return this;
    }

    public AnnotationInvocationHandler withAttributes(Map<String, Object> registeredValues) {
        this.registeredValues = registeredValues;
        return this;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // delegate methods to
        if (method.getDeclaringClass().equals(AnnotationInvocationHandler.class)
                || method.getDeclaringClass().equals(Object.class)
                || method.getDeclaringClass().equals(Annotation.class)) {
            return method.invoke(this, args);
        }
        if (registeredValues.containsKey(method.getName())) {
            if (conversionService != null) {
                return conversionService.convert(registeredValues.get(method.getName()), method.getReturnType());
            }
            else {
                return registeredValues.get(method.getName());
            }
        } else {
            return method.getDefaultValue();
        }
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return this.annotationType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnnotationInvocationHandler)) return false;

        AnnotationInvocationHandler that = (AnnotationInvocationHandler) o;

        if (annotationType != null ? !annotationType.equals(that.annotationType) : that.annotationType != null)
            return false;
        if (registeredValues != null ? !registeredValues.equals(that.registeredValues) : that.registeredValues != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = registeredValues != null ? registeredValues.hashCode() : 0;
        result = 31 * result + (annotationType != null ? annotationType.hashCode() : 0);
        return result;
    }
}
