package com.photomath.roundtable;

import io.micronaut.core.annotation.Introspected;

@Introspected
public record SortingResponse(long timeSpentInMs) {
}
