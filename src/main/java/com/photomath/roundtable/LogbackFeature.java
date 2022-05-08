package com.photomath.roundtable;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.pattern.LineSeparatorConverter;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import com.oracle.svm.core.annotate.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;
import org.graalvm.nativeimage.hosted.RuntimeReflection;

import java.util.stream.Stream;

@AutomaticFeature
public class LogbackFeature implements Feature {

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        Stream.of(
                "ch.qos.logback.classic.Level",
                "ch.qos.logback.classic.Logger",
                "ch.qos.logback.classic.PatternLayout",
                "ch.qos.logback.core.CoreConstants",
                "ch.qos.logback.core.spi.AppenderAttachableImpl",
                "ch.qos.logback.core.status.InfoStatus",
                "ch.qos.logback.core.status.StatusBase",
                "org.slf4j.impl.StaticLoggerBinder",
                "org.slf4j.LoggerFactory"
        ).forEach(RuntimeClassInitialization::initializeAtBuildTime);

        Stream.of(
                AsyncAppender.class,
                ConsoleAppender.class,
                FileAppender.class,
                LineSeparatorConverter.class,
                MessageConverter.class,
                PatternLayoutEncoder.class
        ).forEach(clazz -> {
            RuntimeReflection.register(clazz);
            RuntimeReflection.register(clazz.getDeclaredConstructors());
            RuntimeReflection.register(clazz.getMethods());
        });
    }
}
