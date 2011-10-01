package org.jboss.seam.spring.utils;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.util.Collection;

/**
 * @author Marius Bogoevici
 */
public class Dependencies {

    public static final String SPRING_VERSION;

    public static final String COMMONS_LOGGING_VERSION;

    public static final String SLF4J_VERSION;

    private static final String SOLDER_VERSION;

    private static final String SPRING_VERSION_FALLBACK = "3.0.5.RELEASE";

    private static final String COMMONS_LOGGING_VERSION_FALLBACK = "1.1.1";

    private static final String SLF4J_CALLBACK = "1.6.1";

    private static final String SOLDER_VERSION_CALLBACK = "3.0.0.Final";

    static {
        // use system properties when running the test - use fallback values for IDE testing
        SPRING_VERSION = System.getProperty("spring.version", SPRING_VERSION_FALLBACK);
        COMMONS_LOGGING_VERSION = System.getProperty("commons.logging.version", COMMONS_LOGGING_VERSION_FALLBACK);
        SLF4J_VERSION = System.getProperty("slf4j.version", SLF4J_CALLBACK);
        SOLDER_VERSION = System.getProperty("solder.version", SOLDER_VERSION_CALLBACK);
    }

    public static Collection<JavaArchive> springWebApplicationDependencies() {
        return DependencyResolvers.use(MavenDependencyResolver.class)
                .artifact("org.springframework:spring-context-support:" + SPRING_VERSION)
                .artifact("org.springframework:spring-beans:" + SPRING_VERSION)
                .artifact("org.springframework:spring-context:" + SPRING_VERSION)
                .artifact("org.springframework:spring-core:" + SPRING_VERSION)
                .artifact("org.springframework:spring-web:" + SPRING_VERSION)
                .artifact("org.jboss.seam.solder:seam-solder" + ":" + SOLDER_VERSION)
                .artifact("commons-logging:commons-logging:" + COMMONS_LOGGING_VERSION)
                .artifact("org.slf4j:slf4j-simple:" + SLF4J_VERSION)
                .goOffline()
                .resolveAs(JavaArchive.class);
    }
}
