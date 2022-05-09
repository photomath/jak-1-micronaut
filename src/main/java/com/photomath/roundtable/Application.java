package com.photomath.roundtable;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;
import io.dekorate.kubernetes.annotation.Label;
import io.dekorate.kubernetes.annotation.Port;
import io.dekorate.kubernetes.annotation.Probe;
import io.dekorate.openshift.annotation.OpenshiftApplication;
import io.dekorate.prometheus.annotation.EnableServiceMonitor;
import io.micronaut.context.env.PropertySource;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@OpenAPIDefinition(
        info = @Info(
                title = "Roundtable Micronaut"
        )
)
@OpenshiftApplication(
        name = "roundtable-micronaut",
        labels = {
                @Label(key = "app", value = "roundtable-micronaut"),
        },
        ports = @Port(name = "http", containerPort = 8080),
        livenessProbe = @Probe(httpActionPath = "/health/liveness", initialDelaySeconds = 5, timeoutSeconds = 3, failureThreshold = 10),
        readinessProbe = @Probe(httpActionPath = "/health/readiness", initialDelaySeconds = 5, timeoutSeconds = 3, failureThreshold = 10)

)
@EnableServiceMonitor(path = "/prometheus")
public class Application {

    public static void main(String[] args) {
        initializeLogback();
        final var log = LoggerFactory.getLogger(Application.class);

        // Read service URLs from environment variable
        final var urls = Optional.ofNullable(System.getenv("SORTING_URLS"))
                .orElse("http://localhost:8080/v1/");
        final var urlList = Arrays.asList(urls.split(","));
        log.info("Using sorting services {}", urlList);

        Micronaut.build(args)
                .propertySources(PropertySource.of(
                        "sortingEndpoints",
                        Collections.singletonMap("micronaut.http.services.sorting.urls", urlList)
                ))
                .banner(false)
                .classes(Application.class)
                .start();
    }

    private static void initializeLogback() {
        var context = (LoggerContext) LoggerFactory.getILoggerFactory();
        try {
            var configurator = new JoranConfigurator();
            configurator.setContext(context);
            context.reset();
            configurator.doConfigure(Application.class.getResourceAsStream("/_logback.xml"));
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        StatusPrinter.printInCaseOfErrorsOrWarnings(context);
    }
}
