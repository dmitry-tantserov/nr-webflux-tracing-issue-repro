# nr-webflux-tracing-issue-repro

The current version of New Relic Java agent (7.9.0) has a gap in instrumenting some aspects
of Spring WebFlux reactive applications. This app reproduces the issue.

### Usage
Add the following flags to the `NrWebfluxTracingIssueReproApplication` run config:
 * `-javaagent:/path/to/newrelic-agent-7.9.0.jar`
 * `-Dnewrelic.config.app_name=<NewRelic-Application-Name>`
 * `-Dnewrelic.config.license_key=<NewRelic-License-Key>`

### Reproduction
Make a GET call to the [/api/reproduce](http://localhost:8080/api/reproduce) endpoint.

__Expected result__: the transaction is traced under the `WebTransaction/SpringController/api/reproduce (GET)` name.\
__Actual result__: the transaction is traced under the `WebTransaction/NettyDispatcher/NettyDispatcher` name.

#### Notes:
The issue depends on the type of Netty scheduler used as an argument in `subscribeOn` operator
in `ReactiveWebFilter`. It is reproducible only when the parallel scheduler is used in the web filter.
In the case of the immediate scheduler (no thread switching occurs), the call is traced as expected.
You can switch the type of scheduler by changing the `reactivewebfilter.subscribe-on-parallel-scheduler` property.

However, calls to [/api/ok](http://localhost:8080/api/ok) are traced as expected regardless of the selected scheduler.
The only difference between `/api/reproduce` and `/api/ok` handlers is the presence
of the `String value` parameter annotated with `@RequestBody`.
