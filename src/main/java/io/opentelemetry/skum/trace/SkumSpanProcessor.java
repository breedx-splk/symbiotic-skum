package io.opentelemetry.skum.trace;

import io.opentelemetry.context.Context;
import io.opentelemetry.sdk.trace.ReadWriteSpan;
import io.opentelemetry.sdk.trace.ReadableSpan;
import io.opentelemetry.sdk.trace.SpanProcessor;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SkumSpanProcessor implements SpanProcessor {

  private final List<SpanProcessor> delegates = new CopyOnWriteArrayList<>();

  public void add(SpanProcessor spanProcessor) {
    delegates.add(spanProcessor);
  }

  public void remove(SpanProcessor spanProcessor){
    delegates.remove(spanProcessor);
  }

  @Override
  public void onStart(Context parentContext, ReadWriteSpan span) {
    for (SpanProcessor spanProcessor : delegates) {
      spanProcessor.onStart(parentContext, span);
    }
  }

  @Override
  public boolean isStartRequired() {
    return true;
  }

  @Override
  public void onEnd(ReadableSpan span) {
    for (SpanProcessor spanProcessor : delegates) {
      spanProcessor.onEnd(span);
    }
  }

  @Override
  public boolean isEndRequired() {
    return true;
  }

}
