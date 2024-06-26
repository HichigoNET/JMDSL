///usr/bin/env jbang "$0" "$@" ; exit $?
/*
These commented lines make the class executable if you have jbang installed by making the file
executable (eg: chmod +x ./PerformanceTest.java) and just executing it with ./PerformanceTest.java
*/
//DEPS org.assertj:assertj-core:3.23.1
//DEPS org.junit.jupiter:junit-jupiter-engine:5.9.1
//DEPS org.junit.platform:junit-platform-launcher:1.9.1
//DEPS us.abstracta.jmeter:jmeter-java-dsl-wrapper:1.11
//DEPS us.abstracta.jmeter:jmeter-java-dsl:1.11

import static org.assertj.core.api.Assertions.assertThat;
import static us.abstracta.jmeter.javadsl.JmeterDsl.*;
import static us.abstracta.jmeter.javadsl.wrapper.WrapperJmeterDsl.*;

import java.io.IOException;
import java.io.PrintWriter;
import org.apache.jmeter.assertions.JSR223Assertion;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;
import us.abstracta.jmeter.javadsl.core.TestPlanStats;

public class PerformanceTest {

  @Test
  public void test() throws IOException {
    TestPlanStats stats = testPlan(
        setupThreadGroup(
          transaction("Transaction setup",
            jsr223Sampler("JSR223 setup", "log.error(\"This is jsr-setup\")")
              .children(
                testElement("JSR223 Assertion setup", new JSR223Assertion())
                  .prop("cacheKey", "true")
                  .prop("script", "log.error(\"this is jsr assertion from setup\")")
                  .prop("scriptLanguage", "groovy"),
                jsr223PreProcessor("JSR223 PreProcessor setup", "log.error(\"this is jsr preprocessor from setup\")"),
                jsr223PostProcessor("JSR223 PostProcessor setup", "log.error(\"this is jsr postprocessor from setup\")")
              )
          )
        ),
        threadGroup(1, 1,
          transaction("Transaction thread",
            jsr223Sampler("JSR223 thread", "log.error(\"This is jsr-thread\")")
              .children(
                testElement("JSR223 Assertion thread", new JSR223Assertion())
                  .prop("cacheKey", "true")
                  .prop("script", "log.error(\"this is jsr assertion from thread\")")
                  .prop("scriptLanguage", "groovy"),
                jsr223PreProcessor("JSR223 PreProcessor thread", "log.error(\"this is jsr preprocessor from thread\")"),
                jsr223PostProcessor("JSR223 PostProcessor thread", "log.error(\"this is jsr postprocessor from thread\")")
              )
          )
        ),
        teardownThreadGroup(
          transaction("Transaction teardown",
            jsr223Sampler("JSR223 teardown", "log.error(\"This is jsr-teardown\")")
              .children(
                testElement("JSR223 Assertion teardown", new JSR223Assertion())
                  .prop("cacheKey", "true")
                  .prop("script", "log.error(\"this is jsr assertion from teardown\")")
                  .prop("scriptLanguage", "groovy"),
                jsr223PreProcessor("JSR223 PreProcessor teardown", "log.error(\"this is jsr preprocessor from teardown\")"),
                jsr223PostProcessor("JSR223 PostProcessor teardown", "log.error(\"this is jsr postprocessor from teardown\")")
              )
          )
        )
    ).run();
    assertThat(stats.overall().errorsCount()).isEqualTo(0);
  }

  /*
   This method is only included to make the test class self-executable. You can remove it when
   executing tests with maven, gradle, or some other tool.
   */
  public static void main(String[] args) {
    SummaryGeneratingListener summaryListener = new SummaryGeneratingListener();
    LauncherFactory.create()
        .execute(LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectClass(PerformanceTest.class))
                .build(),
            summaryListener);
    TestExecutionSummary summary = summaryListener.getSummary();
    summary.printFailuresTo(new PrintWriter(System.err));
    System.exit(summary.getTotalFailureCount() > 0 ? 1 : 0);
  }

}
