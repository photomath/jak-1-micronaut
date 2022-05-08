package com.photomath.roundtable;

import com.oracle.svm.core.annotate.AutomaticFeature;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

import java.util.stream.Stream;

@AutomaticFeature
public class MicronautFeature implements Feature {

    @Override
    public void beforeAnalysis(BeforeAnalysisAccess access) {
        Stream.of(
                "io.micronaut.inject.provider.JavaxProviderBeanDefinition"
        ).forEach(RuntimeClassInitialization::initializeAtRunTime);
    }
}
