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
import org.springframework.jndi.JndiTemplate;

import javax.enterprise.inject.spi.BeanManager;

/**
 * @author: Marius Bogoevici
 */
public class JndiBeanManagerFactoryBean implements FactoryBean<BeanManager>, InitializingBean {

    public static String DEFAULT_BEAN_MANAGER_JNDI_LOCATION =  "java:comp/BeanManager";

    private JndiTemplate jndiTemplate;

    private String beanManagerLocation;

    private BeanManager beanManager;

    /**
     * Allow the injection of a {@link JndiTemplate} for customizing JNDI lookups
     *
     * @param jndiTemplate
     */
    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = jndiTemplate;
    }

    /**
     * Allows to configure the location of {@link BeanManager} wherever it is not bound to <code>java:comp/BeanManager</code>,
     * e.g. Tomcat
     *
     * @param beanManagerLocation
     */
    public void setBeanManagerLocation(String beanManagerLocation) {
        this.beanManagerLocation = beanManagerLocation;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.beanManagerLocation == null) {
            beanManagerLocation = DEFAULT_BEAN_MANAGER_JNDI_LOCATION;
        }
        if (this.jndiTemplate == null) {
            jndiTemplate = new JndiTemplate();
        }
        this.beanManager = (BeanManager)jndiTemplate.lookup(beanManagerLocation);
    }

    @Override
    public BeanManager getObject() throws Exception {
        return beanManager;
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
