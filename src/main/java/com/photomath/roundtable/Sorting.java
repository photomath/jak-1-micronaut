package com.photomath.roundtable;

import io.micronaut.core.annotation.Introspected;

import javax.validation.constraints.Positive;

@Introspected
public record Sorting(@Positive long arraySize) {
}
