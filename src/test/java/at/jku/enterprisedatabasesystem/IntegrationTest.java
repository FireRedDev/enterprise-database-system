package at.jku.enterprisedatabasesystem;

import at.jku.enterprisedatabasesystem.EnterprisedatabasesystemApp;
import at.jku.enterprisedatabasesystem.config.TestSecurityConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { EnterprisedatabasesystemApp.class, TestSecurityConfiguration.class })
public @interface IntegrationTest {
}
