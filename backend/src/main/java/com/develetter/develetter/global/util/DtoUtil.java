package com.develetter.develetter.global.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.RecordComponent;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class DtoUtil {

    public static Map<String, Object> toMap(Object record) {
        Map<String, Object> result = new HashMap<>();

        // record만 사용 가능
        if (record instanceof Record) {
            RecordComponent[] components = record.getClass().getRecordComponents();
            for (RecordComponent component : components) {
                try {
                    result.put(component.getName(), component.getAccessor().invoke(record));
                } catch (Exception e) {
                    log.error("Error occurred while parsing record component: " + component.getName(), e);
                    throw new RuntimeException("Error occurred while parsing record component: " + component.getName(), e);
                }
            }
        } else {
            log.warn("The provided object is not a Record: " + record.getClass().getName());
        }

        return result;
    }
}
