package com.example.nrwebfluxtracingissuerepro;

import com.newrelic.api.agent.Trace;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/")
public class WebFluxTracingIssueReproController {

    @GetMapping("/ok")
    @ResponseBody
    @Trace
    public Mono<String> tracingWorks() {
        return Mono.just("ok");
    }

    @GetMapping("/reproduce")
    @ResponseBody
    @Trace
    public Mono<String> tracingIsBroken(@RequestBody String value) {
        return Mono.just("ko");
    }
}
