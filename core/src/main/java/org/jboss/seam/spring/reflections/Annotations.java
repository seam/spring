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

import org.springframework.core.convert.support.GenericConversionService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;

/**
 * Helper class for working with annotations
 *
 * @author: Marius Bogoevici
 */
public class Annotations {

    /**
     * Create an {@link Annotation} instance from its given class
     * @param clazz - the annotation class
     * @param attributes - atrt
     * @param <T> the annotation type
     * @return an annotation instance
     */
     public static <T extends Annotation> T instanceOf(Class<T> clazz, Map<String, Object> attributes) {
         AnnotationInvocationHandler annotationInvocationHandler = new AnnotationInvocationHandler(clazz)
                 .withAttributes(attributes);
         return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, annotationInvocationHandler);
     }

      /**
     * Create an {@link Annotation} instance from its given class
     * @param clazz - the annotation class
     * @param <T> the annotation type
     * @return an annotation instance
     */
     public static <T extends Annotation> T instanceOf(Class<T> clazz) {
         return instanceOf(clazz, Collections.<String, Object>emptyMap());
     }
}
