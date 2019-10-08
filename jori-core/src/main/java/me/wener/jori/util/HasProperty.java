package me.wener.jori.util;

import java.util.Map;

/**
 * @author <a href=http://github.com/wenerme>wener</a>
 * @since 2019/10/1
 */
public interface HasProperty {
  Map<String, Object> getProperties();

  Object setProperty(String name, Object value);

  Object getProperty(String name);

  boolean hasProperty(String name);

  void clear();
}
