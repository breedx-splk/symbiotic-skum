package io.opentelemetry.skum.autoconfigure;

import com.google.auto.service.AutoService;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigProperties;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.skum.TheSwamp;
import java.util.function.BiFunction;

@AutoService(AutoConfigurationCustomizerProvider.class)
public class SkumCustomizerProvider implements AutoConfigurationCustomizerProvider {

  @Override
  public void customize(AutoConfigurationCustomizer autoConfiguration) {

    SpanProcessor skumSpanProcessor = TheSwamp.instance.getSpanProcessor();
    autoConfiguration.addSpanProcessorCustomizer(
        (existing, config) -> SpanProcessor.composite(existing, skumSpanProcessor)
    );

    autoConfiguration.addSamplerCustomizer(
        (existing,config) -> TheSwamp.instance.buildSkumSampler(existing)
    );

    autoConfiguration.addPropagatorCustomizer(
        new BiFunction<TextMapPropagator, ConfigProperties, TextMapPropagator>() {
          @Override
          public TextMapPropagator apply(TextMapPropagator propagator, ConfigProperties config) {
            TextMapPropagator skumPropagator = TheSwamp.instance.getPropagator();
            return TextMapPropagator.composite(propagator, skumPropagator);
          }
        });

  }
}
