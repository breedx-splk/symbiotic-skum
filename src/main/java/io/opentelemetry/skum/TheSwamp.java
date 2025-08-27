package io.opentelemetry.skum;

import io.opentelemetry.skum.trace.SkumSpanProcessor;

// maybe this becomes some public api or something? Might need to be structured/tiered?
public class TheSwamp {

  public final static TheSwamp instance = buildInstance();

  private final SkumSpanProcessor skumSpanProcessor;

  public SkumSpanProcessor getSpanProcessor() {
    return skumSpanProcessor;
  }

  private TheSwamp(SkumSpanProcessor skumSpanProcessor){
    this.skumSpanProcessor = skumSpanProcessor;
  }

  // Might later come from factory or builder or something nifty
  private static TheSwamp buildInstance() {
    return new TheSwamp(new SkumSpanProcessor());
  }

}
