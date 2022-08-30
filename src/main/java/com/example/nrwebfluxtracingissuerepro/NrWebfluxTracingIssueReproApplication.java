package com.example.nrwebfluxtracingissuerepro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class NrWebfluxTracingIssueReproApplication {

    public static void main(String[] args) {
        ReactorDebugAgent.init();
        ReactorDebugAgent.processExistingClasses();
        SpringApplication.run(NrWebfluxTracingIssueReproApplication.class, args);
    }
}
