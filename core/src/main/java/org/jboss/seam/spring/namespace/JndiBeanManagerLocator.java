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

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.NamingException;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jndi.JndiLocatorSupport;
import org.springframework.util.Assert;

/**
 * @author: Marius Bogoevici
 */
public class JndiBeanManagerLocator extends JndiLocatorSupport implements FactoryBean<BeanManager>, InitializingBean {

    
    private final static String[] DEFAULT_BEAN_MANAGER_NAMES = new String[] {"java:comp/BeanManager", "java:comp/env/BeanManager" };

    private String beanManagerLocationName;
    
    private boolean locateEagerly = false;

    private volatile BeanManager beanManagerInstance;

    private Object initializationLock = new Object();

    public void setBeanManagerLocationName(String beanManagerLocationName) {
        Assert.notNull(beanManagerLocationName, "The BeanManager location name must not be null");
        Assert.hasText(beanManagerLocationName, "The BeanManager location name must not be empty");        
        this.beanManagerLocationName = beanManagerLocationName;
    }

    public void setLocateEagerly(boolean locateEagerly) {
        this.locateEagerly = locateEagerly;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.locateEagerly) {
            initInstanceIfNotSet();
        }
    }

    private void initInstanceIfNotSet() throws NamingException {
        if (beanManagerInstance == null) {
            synchronized (initializationLock) {
                if (beanManagerInstance == null) {
                    if (this.beanManagerLocationName == null) {
                        BeanManager beanManagerInstance = null;
                        for (String beanManagerName : DEFAULT_BEAN_MANAGER_NAMES) {
                            try {
                                beanManagerInstance = lookup(beanManagerName, BeanManager.class);
                            } catch (NamingException e) {
                                // ignore individual failures
                            }
                        }
                        if (beanManagerInstance == null) {
                            throw new IllegalStateException("Cannot locate a BeanManager at any of the default JNDI locations " +
                                    "please provide an explicit BeanManager location");
                        } else {
                            this.beanManagerInstance = beanManagerInstance;
                        }
                    } else {
                        this.beanManagerInstance = lookup(this.beanManagerLocationName, BeanManager.class);
                    }
                }
            }
        }
    }

    @Override
    public BeanManager getObject() throws Exception {
        initInstanceIfNotSet();
        return beanManagerInstance;
    }

    @Override
    public Class<?> getObjectType() {
        return BeanManager.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
