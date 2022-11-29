package com.senghong.kafka.model;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CanalRow {
    //id
    private String id;
    //当前数据
    private Map<String, String> current = new HashMap<>();
    //旧数据
    private Map<String, String> old = new HashMap<>();
    //修改的数据
    private Map<String, String> updated = new HashMap<>();
}
