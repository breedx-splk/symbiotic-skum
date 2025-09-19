package io.opentelemetry.skum;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.skum.resource.ResourceMangler;

public class TestMain {

  public static void main(String[] args) throws Exception {
    System.setProperty("otel.exporter.otlp.protocol", "http/protobuf");

    AutoConfiguredOpenTelemetrySdk sdk = AutoConfiguredOpenTelemetrySdk.builder()
        .addResourceCustomizer((resource, config) -> resource.merge(Resource.builder()
            .put("deployment.environment.name", "skum-env")
            .put("service.name", "skum-test")
            .build()))
        .build();

    OpenTelemetrySdk otel = sdk.getOpenTelemetrySdk();

    startAndStopSpan(otel);
    Thread.sleep(5000); // This is to allow the BSP to send the first span before we mangle the resource
    new ResourceMangler(otel).setAttribute("mangled", "with skum!");
    startAndStopSpan(otel);
  }

  private static void startAndStopSpan(OpenTelemetrySdk otel) throws InterruptedException {
    Span span = otel.getTracer("skum")
        .spanBuilder("test.span")
        .setAttribute("foo", "bar")
        .startSpan();

    span.makeCurrent();
    Thread.sleep(500);
    span.end();
  }

}
