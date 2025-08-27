package io.opentelemetry.skum.trace;

import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class SkumSampler implements Sampler {

  private final Sampler existing;
  private final AtomicReference<Sampler> mutableDelegate = new AtomicReference<>();

  public SkumSampler(Sampler existing) {
    this.existing = existing;
  }

  void set(Sampler sampler) {
    this.mutableDelegate.set(sampler);
  }

  void clear(){
    this.mutableDelegate.set(null);
  }

  @Override
  public SamplingResult shouldSample(Context parentContext, String traceId, String name,
      SpanKind spanKind, Attributes attributes, List<LinkData> parentLinks) {
    Sampler delegate = mutableDelegate.get();
    if(delegate == null){
      return existing.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
    }
    return delegate.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
  }

  @Override
  public String getDescription() {
    Sampler delegate = mutableDelegate.get();
    if(delegate == null){
      return "skum";
    }
    return "skum:" + delegate.getDescription();
  }
}
