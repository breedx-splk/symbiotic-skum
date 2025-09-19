package io.opentelemetry.skum.trace;

import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.context.propagation.TextMapSetter;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;

public class SkumPropagator implements TextMapPropagator {

  private final AtomicReference<TextMapPropagator> delegate = new AtomicReference<>(TextMapPropagator.noop());

  public void setDelegate(TextMapPropagator propagator) {
    delegate.set(propagator);
  }

  @Override
  public Collection<String> fields() {
    return delegate.get().fields();
  }

  @Override
  public <C> void inject(Context context, @Nullable C carrier, TextMapSetter<C> setter) {
    delegate.get().inject(context, carrier, setter);
  }

  @Override
  public <C> Context extract(Context context, @Nullable C carrier, TextMapGetter<C> getter) {
    return delegate.get().extract(context, carrier, getter);
  }
}
