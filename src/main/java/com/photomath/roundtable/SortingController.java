package com.photomath.roundtable;

import io.micrometer.core.annotation.Counted;
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
    @Counted(value = "demo_request_counter_total", description = "Number of sorting requests processed")
    public Mono<SortingResponse> sorting(@Valid @Body Sorting request) {
        return Mono.just(request.arraySize())
                .doOnSubscribe(subscription -> log.info("Request received"))
                .publishOn(Schedulers.parallel())
                .map(SortingServiceImpl::sorting)
                .doOnNext(time -> log.info("Sorting performed in {} ms", time))
                .map(SortingResponse::new)
                .doOnNext(response -> log.info("Response sent"));
    }

    @Post(uri = "delegated/sorting")
    public void sortingDelegated(@Valid @Body Sorting request) {
        sortingClient.sorting(request)
                .subscribe();
    }
}
