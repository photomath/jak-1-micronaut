package com.photomath.roundtable;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.validation.Valid;

@Controller("/v1")
public class SortingController {

    private final SortingClient sortingClient;
    private final Logger log = LoggerFactory.getLogger(Sorting.class);

    public SortingController(@Client("sorting") SortingClient sortingClient) {
        this.sortingClient = sortingClient;
    }

    @Post(uri = "sorting")
    public Mono<SortingResponse> sorting(@Valid @Body Sorting request) {
        return Mono.just(request.arraySize())
                .doOnSubscribe(subscription -> log.info("Request received"))
                .publishOn(Schedulers.parallel()) // (1)
                .map(SortingServiceImpl::sorting) // (2)
                .doOnNext(time -> log.info("Sorting performed in {} ms", time))
                .map(SortingResponse::new) // (3)
                .doOnNext(response -> log.info("Response sent"));
    }

    @Post(uri = "delegated/sorting")
    public Mono<SortingResponse> sortingDelegated(@Valid @Body Sorting request) {
        return sortingClient.sorting(request);
    }
}
