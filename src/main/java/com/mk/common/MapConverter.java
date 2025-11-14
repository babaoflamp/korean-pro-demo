package com.mk.common;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapConverter {
  private static final Logger LOGGER = LoggerFactory.getLogger(MapConverter.class);

  /**
   * @param map으로 변환할 object
   * @return Map<String, Object>
   */
  public static Map<String, Object> convertToMap(Object object) {
    Map<String, Object> map = new HashMap<>();
    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true); // private 필드에도 접근할 수 있도록 설정
      try {
        map.put(field.getName(), field.get(object));
      } catch (IllegalAccessException e) {
        LOGGER.error(e.getMessage());
      }
    }
    return map;
  }

  /**
   * @param mapList로 변환할 List 객체
   * @return List<Map<String, Object>>
   */
  public static List<Map<String, Object>> convertToMapList(List<?> list) {
    return list
        .stream()
        .map(MapConverter::convertToMap)
        .collect(Collectors.toList());
  }

}
