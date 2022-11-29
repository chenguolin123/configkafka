package com.senghong.kafka.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.otter.canal.protocol.FlatMessage;
import com.senghong.common.util.AnnotationUtil;
import com.senghong.common.util.ConvertUtil;
import com.senghong.common.util.JSONUtil;
import com.senghong.kafka.annotations.CanalField;
import com.senghong.kafka.enums.CanalEventType;
import com.senghong.kafka.model.CanalRow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * note:
 *
 * @author cgl
 * @since 2021/8/17
 */
@Slf4j
public class CanalUtil {

    public static <T> Map<String, Object> convertToMap(Map<String, String> dataMap, Class<T> baseClass) throws Exception {
        Map<String, Object> result = new HashMap<>();
        T esModel = baseClass.getDeclaredConstructor().newInstance();
        var canalFieldMap = AnnotationUtil.fetchFieldAnnotationMap(baseClass, CanalField.class);
        dataMap.forEach((col, colValue) -> {
            String field = StrUtil.toCamelCase(col);
            var canalField = canalFieldMap.get(field);
            try {
                Object value = typeConvert(colValue, PropertyUtils.getPropertyType(esModel, field), canalField);
                result.put(field, value);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                // 没有找到这个属性，可能是数据库改动导致，不要中断，仅仅发日志
                log.warn("error occurred while setting property: [{}] from [{}={}]", field, col, colValue, e);
            } catch (Exception e) {
                throw new RuntimeException("error occurred while setting property: [" + field + "] from [" + col + "=" + colValue + "]", e);
            }
        });
        return result;
    }

    public static Object typeConvert(String value, Class<?> clazz, CanalField canalField) {
        if (value == null || clazz == null) return null;
        if (clazz == Integer.class || clazz.getName().equals("int")) {
            return ConvertUtil.toInt(value);
        } else if (clazz == Long.class || clazz.getName().equals("long")) {
            return ConvertUtil.toLong(value);
        } else if (clazz == Boolean.class || clazz.getName().equals("boolean")) {
            return ConvertUtil.toBoolean(value);
        } else if (clazz == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (clazz == Date.class) {
            if (canalField != null && canalField.dateFormat() != null) {
                return DateUtil.parse(value, canalField.dateFormat()).toJdkDate();
            } else
                return DateUtil.parse(value).toJdkDate();
        } else if (clazz == String.class) {
            return value;
        } else {
            return JSONUtil.parse(value, clazz);
        }
    }


    /**
     * all cols
     *
     * @param message
     * @param type
     * @return
     */
    public static List<CanalRow> fetchRows(FlatMessage message, CanalEventType type) {
        if (message == null) return new ArrayList<>();
        List<CanalRow> rows = new ArrayList<>();
        message.getData().forEach(map -> {
            CanalRow row = new CanalRow();
            rows.add(row);
            row.setId(map.get("id"));
            row.setCurrent(map);
        });
        if (type == CanalEventType.UPDATE && CollUtil.isNotEmpty(message.getOld())) {
            for (int i = 0; i < rows.size(); i++) {
                CanalRow row = rows.get(i);
                //修改
                Map<String, String> oldMap = message.getOld().get(i);
                Map<String, String> updated = new HashMap<>();
                row.setUpdated(updated);
                oldMap.forEach((col, value) -> {
                    updated.put(col, row.getCurrent().get(col));
                });
                //填充旧值
                Map<String, String> newOldMap = new HashMap<>(row.getCurrent());
                newOldMap.putAll(oldMap);
                row.setOld(newOldMap);
            }
        }
        return rows;
    }


}
