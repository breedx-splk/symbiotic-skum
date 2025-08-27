package io.opentelemetry.skum;

import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.skum.trace.SkumSampler;
import io.opentelemetry.skum.trace.SkumSpanProcessor;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;

// maybe this becomes some public api or something? Might need to be structured/tiered?
public class TheSwamp {

  public final static TheSwamp instance = buildInstance();

  private final SkumSpanProcessor skumSpanProcessor;

  private final AtomicReference<SkumSampler> skumSampler = new AtomicReference<>();

  public SkumSpanProcessor getSpanProcessor() {
    return skumSpanProcessor;
  }

  // shouldn't be nullable, but could be
  @Nullable
  public SkumSampler getSkumSampler(){
    return skumSampler.get();
  }

  // internal method
  public Sampler buildSkumSampler(Sampler existing) {
    skumSampler.set(new SkumSampler(existing));
    return skumSampler.get();
  }

  private TheSwamp(SkumSpanProcessor skumSpanProcessor){
    this.skumSpanProcessor = skumSpanProcessor;
  }
  // Might later come from factory or builder or something nifty

  private static TheSwamp buildInstance() {
    return new TheSwamp(new SkumSpanProcessor());
  }
}
