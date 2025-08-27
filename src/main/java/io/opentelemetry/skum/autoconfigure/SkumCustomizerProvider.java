package io.opentelemetry.skum.autoconfigure;

import com.google.auto.service.AutoService;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import io.opentelemetry.sdk.trace.SpanProcessor;
import io.opentelemetry.skum.TheSwamp;

@AutoService(AutoConfigurationCustomizerProvider.class)
public class SkumCustomizerProvider implements AutoConfigurationCustomizerProvider {

  @Override
  public void customize(AutoConfigurationCustomizer autoConfiguration) {

    SpanProcessor skumSpanProcessor = TheSwamp.instance.getSpanProcessor();
    autoConfiguration.addSpanProcessorCustomizer(
        (existing, config) -> SpanProcessor.composite(existing, skumSpanProcessor));
  }
}
