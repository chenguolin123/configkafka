package com.senghong.kafka.model;

import lombok.Data;

/**
 * note: 延迟消息
 *
 * @author cgl
 * @since 2022/2/9
 */
@Data
public class DelayMessage extends KafkaBaseModel implements java.io.Serializable {
    /**
     * topic
     */
    private String topic;
    /**
     * message
     */
    private KafkaBaseModel message;
    /**
     * 延迟时间
     */
    private long delayMillisecond;
    /**
     * 是否顺序消息
     */
    private boolean order;
}
