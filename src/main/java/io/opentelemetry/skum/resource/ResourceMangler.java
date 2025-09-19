package io.opentelemetry.skum.resource;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.internal.shaded.WeakConcurrentMap;
import io.opentelemetry.exporter.internal.otlp.ResourceMarshaler;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import java.lang.reflect.Field;
import java.util.function.Consumer;

/**
 * Jackin' up the shared state since 2025! (•̀ᴗ•́)و
 */
public class ResourceMangler {

  private final OpenTelemetrySdk otel;

  public ResourceMangler(OpenTelemetrySdk otel) {
    this.otel = otel;
  }

  public void setAttribute(String key, String value) {
    setAttribute(b -> b.put(key, value));
  }

  public void setAttribute(String key, double value) {
    setAttribute(b -> b.put(key, value));
  }

  public void setAttribute(String key, long value) {
    setAttribute(b -> b.put(key, value));
  }

  public void setAttribute(String key, boolean value) {
    setAttribute(b -> b.put(key, value));
  }

  public <T> void setAttribute(AttributeKey<T> key, T value) {
    setAttribute(b -> b.put(key, value));
  }

  private <T> void setAttribute(Consumer<AttributesBuilder> setter) {
    try {
      Attributes resourceAttributes = getResourceAttributes();
      AttributesBuilder builder = resourceAttributes.toBuilder();
      setter.accept(builder);
      Attributes newAttr = builder.build();
      Object data = getDataFromAttributes(newAttr);
      Field attrDataField = resourceAttributes.getClass().getSuperclass().getDeclaredField("data");
      attrDataField.setAccessible(true);
      attrDataField.set(resourceAttributes, data);
      attrDataField.setAccessible(false);
      invalidateResourceMarshalerCachedValue();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void invalidateResourceMarshalerCachedValue() throws NoSuchFieldException, IllegalAccessException {
    Field marshalerCacheField = ResourceMarshaler.class.getDeclaredField("RESOURCE_MARSHALER_CACHE");
    marshalerCacheField.setAccessible(true);
    WeakConcurrentMap<Resource, ResourceMarshaler> cache = (WeakConcurrentMap<Resource, ResourceMarshaler>) marshalerCacheField.get(null);
    ResourceMarshaler oldValue = cache.remove(getResource());
    marshalerCacheField.setAccessible(false);
  }

  private static Object getDataFromAttributes(Attributes attributes)
      throws NoSuchFieldException, IllegalAccessException {
    Field attrDataField = attributes.getClass().getSuperclass().getDeclaredField("data");
    try {
      attrDataField.setAccessible(true);
      return attrDataField.get(attributes);
    } finally {
      attrDataField.setAccessible(false);
    }
  }

  private Attributes getResourceAttributes() throws NoSuchFieldException, IllegalAccessException {
    Resource resource = getResource();
    return resource.getAttributes();
  }

  private Resource getResource() throws NoSuchFieldException, IllegalAccessException {
    Tracer tracer = otel.getTracer("skum");
    //Note: This does indeed seem to also be shared with the MeterProviderSharedState
    Field sharedStateField = tracer.getClass().getDeclaredField("sharedState");
    sharedStateField.setAccessible(true);
    Object sharedState = sharedStateField.get(tracer);
    sharedStateField.setAccessible(false);

    Field resourceField = sharedState.getClass().getDeclaredField("resource");
    resourceField.setAccessible(true);
    Resource resource = (Resource) resourceField.get(sharedState);
    resourceField.setAccessible(false);
    return resource;
  }
}
