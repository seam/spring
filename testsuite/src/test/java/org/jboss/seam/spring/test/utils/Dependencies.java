package org.jboss.seam.spring.test.utils;

import java.io.File;
import java.util.Collection;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;

/**
 * @author Marius Bogoevici
 */
public class Dependencies {


    // Default dependency versions

    private static final String SPRING_VERSION_FALLBACK = "3.0.5.RELEASE";
    private static final String COMMONS_LOGGING_VERSION_FALLBACK = "1.1.1";
    private static final String SLF4J_FALLBACK = "1.6.1";
    private static final String SOLDER_VERSION_FALLBACK = "3.1.0.Beta3";

    public static final String SPRING_VERSION;
    public static final String COMMONS_LOGGING_VERSION;
    public static final String SLF4J_VERSION;
    private static final String SOLDER_VERSION;
    private static final String SEAM_SPRING_VERSION;

    private static volatile Collection<JavaArchive> SPRING_DEPENDENCIES = null;

    public static final String SEAM_SPRING_EXTENSION_LOCATION = "META-INF/services/javax.enterprise.inject.spi.Extension";

    private static final String SEAM_SPRING_JAR = "../core/target/seam-spring-core.jar";


    static {
        // use system properties when running the test - use fallback values for IDE testing
        SPRING_VERSION = System.getProperty("spring.version", SPRING_VERSION_FALLBACK);
        COMMONS_LOGGING_VERSION = System.getProperty("commons.logging.version", COMMONS_LOGGING_VERSION_FALLBACK);
        SLF4J_VERSION = System.getProperty("slf4j.version", SLF4J_FALLBACK);
        SOLDER_VERSION = System.getProperty("solder.version", SOLDER_VERSION_FALLBACK);
        SEAM_SPRING_VERSION = System.getProperty("seam.spring.version");
    }

    public static Collection<JavaArchive> dependencies() {
        if (SPRING_DEPENDENCIES == null) {
            SPRING_DEPENDENCIES = DependencyResolvers.use(MavenDependencyResolver.class)
                    .artifact("org.springframework:spring-asm:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-context-support:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-beans:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-context:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-core:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-web:" + SPRING_VERSION)
                    .artifact("org.springframework:spring-tx:" + SPRING_VERSION)
                    .artifact("org.jboss.solder:solder-api" + ":" + SOLDER_VERSION)
                    .artifact("org.jboss.solder:solder-impl" + ":" + SOLDER_VERSION)
                    .artifact("commons-logging:commons-logging:" + COMMONS_LOGGING_VERSION)
                    .artifact("org.slf4j:slf4j-simple:" + SLF4J_VERSION)
                    .goOffline()
                    .resolveAs(JavaArchive.class);
            SPRING_DEPENDENCIES.add(ShrinkWrap.create(
                    ZipImporter.class, "seam-spring-core.jar")
                    .importFrom(new File(SEAM_SPRING_JAR))
                    .as(JavaArchive.class));
        }
        return SPRING_DEPENDENCIES;
    }
    
}
