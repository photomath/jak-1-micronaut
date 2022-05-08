package com.photomath.roundtable;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import reactor.core.publisher.Mono;

@Client("sorting")
interface SortingClient {

    @Post("sorting")
    Mono<SortingResponse> sorting(@Body Sorting request);
}
