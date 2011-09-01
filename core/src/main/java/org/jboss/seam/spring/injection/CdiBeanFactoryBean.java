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

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import javax.enterprise.inject.spi.BeanManager;

/**
 * @author: Marius Bogoevici
 */
public class CdiBeanFactoryBean<T> implements FactoryBean<T>, InitializingBean {

    private BeanManager beanManager;

    private CdiBeanLookup<T> cdiBeanLookup;

    /**
     * Allows the injection of a bean manager instance
     *
     * @param beanManager
     */
    public void setBeanManager(BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    /**
     * Allows the injection of a bean lookup strategy
     * @param cdiBeanLookup
     */
    public void setCdiBeanLookup(CdiBeanLookup<T> cdiBeanLookup) {
        this.cdiBeanLookup = cdiBeanLookup;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(beanManager, "A BeanManager instance must be provided");
    }

    @Override
    public T getObject() throws Exception {
        return cdiBeanLookup.lookupBean(beanManager);
    }

    @Override
    public Class<?> getObjectType() {
       return cdiBeanLookup.getExpectedType();
    }

    @Override
    public boolean isSingleton() {
        // for now, always delegate to the BeanManager for retrieving the instance
        return false;
    }
}
