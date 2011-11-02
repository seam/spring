package org.jboss.seam.spring.test.config;

import static org.jboss.seam.spring.test.utils.Dependencies.corePackages;
import static org.jboss.seam.spring.test.utils.Dependencies.springDependencies;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.seam.spring.bootstrap.SpringContext;
import org.jboss.seam.spring.test.bootstrap.WebContextProducer;
import org.jboss.seam.spring.test.injection.CdiBean;
import org.jboss.seam.spring.test.injection.CdiDependency;
import org.jboss.seam.spring.test.injection.CdiQualifier;
import org.jboss.seam.spring.test.injection.CdiQualifierWithAttributes;
import org.jboss.seam.spring.test.injection.SecondCdiBean;
import org.jboss.seam.spring.test.injection.SpringBeanInjectedWithCdiBean;
import org.jboss.seam.spring.test.injection.ThirdCdiBean;
import org.jboss.seam.spring.test.utils.ContextInjected;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;

/**
 * @author Marius Bogoevici
 */

@RunWith(Arquillian.class)
public class BeanImportNamespaceTest {

    @Deployment
    public static Archive<?> deployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML("org/jboss/seam/spring/test/common/web.xml")
                .addAsWebInfResource("org/jboss/seam/spring/test/config/webinf/applicationContextWithImport.xml", "applicationContext.xml")
                .addAsResource("META-INF/services/javax.enterprise.inject.spi.Extension")
                .addAsLibraries(springDependencies())
                .addPackages(true, corePackages())
                .addAsResource("META-INF/spring.schemas")
                .addAsResource("META-INF/spring.handlers")
                .addAsResource("org/jboss/seam/spring/config/seam-spring-3.1.xsd")
                .addClasses(WebContextProducer.class, ContextInjected.class, CdiBean.class, CdiDependency.class,
                        CdiQualifier.class, SecondCdiBean.class, SpringBeanInjectedWithCdiBean.class, ThirdCdiBean.class, CdiQualifierWithAttributes.class);
    }


    @Test
    public void testSimpleBean(@SpringContext ApplicationContext applicationContext) {
        Assert.assertNotNull(applicationContext);
        BeanManager beanManager = applicationContext.getBean(BeanManager.class);
        Assert.assertNotNull(beanManager);
        CdiBean cdiBean = applicationContext.getBean(CdiBean.class);
        Assert.assertNotNull(cdiBean);
        SpringBeanInjectedWithCdiBean springBean = applicationContext.getBean(SpringBeanInjectedWithCdiBean.class);
        Assert.assertNotNull(springBean);
        Assert.assertNotNull(springBean.getCdiBean());
        Assert.assertNotNull(springBean.getSecondCdiBean());
        ThirdCdiBean thirdCdiBean = applicationContext.getBean(ThirdCdiBean.class);
        Assert.assertNotNull(thirdCdiBean);
    }

}
