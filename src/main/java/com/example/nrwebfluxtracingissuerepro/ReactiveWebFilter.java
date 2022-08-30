package com.example.nrwebfluxtracingissuerepro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.io.Closeable;

@Component
@Order(ReactiveWebFilter.ORDER)
public class ReactiveWebFilter implements WebFilter, Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactiveWebFilter.class);

    public static final int ORDER = -1;

    private Scheduler scheduler;

    @Value("${reactivewebfilter.subscribe-on-parallel-scheduler}")
    private boolean subscribeOnParallelScheduler;

    @PostConstruct
    private void init() {
        if (subscribeOnParallelScheduler) {
            LOGGER.info("Creating parallel scheduler for ReactiveWebFilter");
            this.scheduler = Schedulers.newParallel("reactive-web-filter");
        } else {
            LOGGER.info("Creating immediate scheduler for ReactiveWebFilter");
            this.scheduler = Schedulers.immediate();
        }
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.defer(() -> runFilter(exchange, chain)).subscribeOn(scheduler);
    }

    private Mono<Void> runFilter(ServerWebExchange exchange, WebFilterChain chain) {
        return Mono.just(exchange).flatMap(chain::filter);
    }

    @Override
    public void close() {
        scheduler.dispose();
    }
}
