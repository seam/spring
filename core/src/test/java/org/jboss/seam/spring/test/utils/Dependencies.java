package org.jboss.seam.spring.test.utils;

import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

import java.util.Collection;

/**
 * @author Marius Bogoevici
 */
public class Dependencies {


    // Core Seam Spring packages (all will be initialized on demand)

    private static final String ORG_JBOSS_SEAM_SPRING = "org.jboss.seam.spring";

    private static final String[] CORE_PACKAGE_NAMES = new String[]{
            ORG_JBOSS_SEAM_SPRING + ".bootstrap",
            ORG_JBOSS_SEAM_SPRING + ".injection",
            ORG_JBOSS_SEAM_SPRING + ".reflections",
            ORG_JBOSS_SEAM_SPRING + ".utils"
    };


    // Default dependency versions

    private static final String SPRING_VERSION_FALLBACK = "3.0.5.RELEASE";

    private static final String COMMONS_LOGGING_VERSION_FALLBACK = "1.1.1";

    private static final String SLF4J_CALLBACK = "1.6.1";

    private static final String SOLDER_VERSION_CALLBACK = "3.0.0.Final";


    public static final String SPRING_VERSION;

    public static final String COMMONS_LOGGING_VERSION;

    public static final String SLF4J_VERSION;

    private static final String SOLDER_VERSION;

    private static volatile Collection<JavaArchive> SPRING_DEPENDENCIES = null;
    public static final String SEAM_SPRING_EXTENSION_LOCATION = "META-INF/services/javax.enterprise.inject.spi.Extension";

    static {
        // use system properties when running the test - use fallback values for IDE testing
        SPRING_VERSION = System.getProperty("spring.version", SPRING_VERSION_FALLBACK);
        COMMONS_LOGGING_VERSION = System.getProperty("commons.logging.version", COMMONS_LOGGING_VERSION_FALLBACK);
        SLF4J_VERSION = System.getProperty("slf4j.version", SLF4J_CALLBACK);
        SOLDER_VERSION = System.getProperty("solder.version", SOLDER_VERSION_CALLBACK);

        // resolve Spring dependencies upfront

    }

    public static Collection<JavaArchive> springDependencies() {
        if (SPRING_DEPENDENCIES == null) {
            SPRING_DEPENDENCIES = DependencyResolvers.use(MavenDependencyResolver.class)
                    .artifact("org.springframework:spring-asm:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-context-support:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-beans:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-context:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-core:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-web:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-tx:" + SPRING_VERSION)
                    .artifact("org.jboss.seam.solder:seam-solder" + ":" + SOLDER_VERSION)
                    .artifact("commons-logging:commons-logging:" + COMMONS_LOGGING_VERSION)
                    .artifact("org.slf4j:slf4j-simple:" + SLF4J_VERSION)
                    .goOffline()
                    .resolveAs(JavaArchive.class);
        }
        return SPRING_DEPENDENCIES;
    }

    public static String[] corePackages() {
        return CORE_PACKAGE_NAMES;
    }


}
