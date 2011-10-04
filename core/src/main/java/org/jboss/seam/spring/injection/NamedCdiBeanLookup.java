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

import org.springframework.beans.factory.BeanCreationException;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

/**
 * @author: Marius Bogoevici
 */
public class NamedCdiBeanLookup implements CdiBeanLookup<Object> {

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object lookupBean(BeanManager beanManager) {
        Bean<Object> resolvedBean = (Bean<Object>) beanManager.resolve(beanManager.getBeans(name));
        if (resolvedBean != null) {
            return resolvedBean.create(beanManager.createCreationalContext(resolvedBean));
        }
        else {
            throw new BeanCreationException("No bean named " + name + " can be found");
        }
    }

    @Override
    public Class<?> getExpectedType() {
        return null;
    }
}
