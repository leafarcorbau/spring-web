package com.dh.sp.web.mvc;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.lifecycle.Startables;

import java.util.Map;
import java.util.stream.Stream;

import static java.lang.String.format;

@ContextConfiguration(
        initializers = IntegrationTest.Initializer.class)
public abstract class IntegrationTest {

    static class Initializer implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        static GenericContainer mockServer = new GenericContainer("jamesdbloom/mockserver:mockserver-5.5.4")
                .withExposedPorts(1080);

        private static void startContainers() {
            Startables.deepStart(Stream.of(mockServer)).join();
        }

        private static Map<String, String> createConnectionConfiguration() {
            return Map.of(
                    "dh.credit.service.url", format("http://%s:%d", mockServer.getHost(), mockServer.getMappedPort(1080)),
                    "dh.mock.server.host", mockServer.getHost(),
                    "dh.mock.server.port", mockServer.getMappedPort(1080).toString()
            );
        }

        @Override
        public void initialize(
                ConfigurableApplicationContext applicationContext) {

            startContainers();

            ConfigurableEnvironment environment =
                    applicationContext.getEnvironment();

            MapPropertySource testcontainers = new MapPropertySource(
                    "testcontainers",
                    (Map) createConnectionConfiguration()
            );

            environment.getPropertySources().addFirst(testcontainers);
        }
    }
}
