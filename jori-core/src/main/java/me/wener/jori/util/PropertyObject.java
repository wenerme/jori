package me.wener.jori.util;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import lombok.ToString;

/**
 * A generic base object can hold any json model.
 *
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019-07-05
 */
@EqualsAndHashCode
@ToString
public class PropertyObject implements HasProperty {

  @Setter private Map<String, Object> properties;

  /** @return all properties */
  @Override
  public Map<String, Object> getProperties() {
    return properties;
  }

  /** set the property */
  @Override
  public Object setProperty(String name, Object value) {
    if (properties == null) {
      properties = Maps.newHashMap();
    }
    return properties.put(name, value);
  }

  /** get value of the property */
  @Override
  public Object getProperty(String name) {
    if (properties != null) {
      return properties.get(name);
    }
    return null;
  }

  /** @return properties contain this name */
  @Override
  public boolean hasProperty(String name) {
    if (properties != null) {
      return properties.containsKey(name);
    }
    return false;
  }

  /** clear all extra properties */
  @Override
  public void clear() {
    if (properties != null) {
      properties.clear();
    }
  }

  /** try cast this object to type */
  public <T> Optional<T> tryCast(Class<T> type) {
    if (type.isAssignableFrom(getClass())) {
      return Optional.of(type.cast(this));
    }
    return Optional.empty();
  }
}
