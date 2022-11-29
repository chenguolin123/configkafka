package com.senghong.kafka.service;

import com.senghong.common.model.Context;
import com.senghong.common.util.DataSourceHolder;
import com.senghong.common.util.JSONUtil;
import com.senghong.kafka.model.DelayMessage;
import com.senghong.kafka.model.KafkaBaseModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.TimeUnit;

import static com.senghong.kafka.constant.KafkaConstant.TOPIC_DELAY_TOPIC;
import static com.senghong.kafka.service.KafkaService.CallBack.INSTANCE;

/**
 * note:
 *
 * @author cgl
 * @since 2022/2/9
 */
@Service
@Slf4j
public class KafkaService {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * 发送消息
     *
     * @param topic
     * @param id
     */
    public void sendMsg(String topic, Long id) {
        sendMsg(topic, KafkaBaseModel.of(id));
    }

    /**
     * 发送消息
     *
     * @param topic
     * @param model
     */
    public void sendMsg(String topic, KafkaBaseModel model) {
        sendMsg(topic, model, false);
    }

    /**
     * 发送延迟消息
     *
     * @param topic     topic
     * @param model     model
     * @param delayTime delayTime
     * @param timeUnit  timeUnit
     */
    public void sendDelayMsg(String topic, KafkaBaseModel model, long delayTime, TimeUnit timeUnit) {
        sendDelayMsg(topic, model, delayTime, timeUnit, false);
    }

    /**
     * 发送延迟消息
     *
     * @param topic     topic
     * @param model     model
     * @param delayTime delayTime
     * @param timeUnit  timeUnit
     * @param order
     */
    public void sendDelayMsg(String topic, KafkaBaseModel model, long delayTime, TimeUnit timeUnit, boolean order) {
        DelayMessage delayMessage = new DelayMessage();
        delayMessage.setTopic(topic);
        delayMessage.setMessage(model);
        delayMessage.setDelayMillisecond(timeUnit.toMillis(delayTime));
        delayMessage.setOrder(order);
        sendMsg(TOPIC_DELAY_TOPIC, delayMessage);
    }


    /**
     * 发送消息
     *
     * @param topic topic
     * @param model 消息
     * @param order 是否有序
     */
    public void sendMsg(String topic, KafkaBaseModel model, boolean order) {
        if (model == null) return;
        model.setDsCode(DataSourceHolder.get());
        if (model.getUser() == null) {
            model.setUser(Context.getUser());
        }
        kafkaTemplate.send(topic, order ? 0 : null, null, JSONUtil.toJSONString(model)).addCallback(INSTANCE);
    }


    static class CallBack implements ListenableFutureCallback {
        protected final static CallBack INSTANCE = new CallBack();

        @Override
        public void onFailure(Throwable e) {
            log.error("kafka发送消息失败", e);
        }

        @Override
        public void onSuccess(Object result) {

        }
    }
}
