package com.senghong.kafka.lisenter;

import com.alibaba.otter.canal.protocol.FlatMessage;
import com.senghong.common.util.AnnotationUtil;
import com.senghong.common.util.DataSourceHolder;
import com.senghong.common.util.JSONUtil;
import com.senghong.database.util.DbUtil;
import com.senghong.kafka.enums.CanalEventType;
import com.senghong.kafka.model.CanalRow;
import com.senghong.kafka.util.CanalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

import java.util.ArrayList;
import java.util.List;

/**
 * note:
 *
 * @author cgl
 * @since 2021/8/17
 */
@Slf4j
public abstract class CanalListener {
    @Autowired
    private DbUtil dbUtil;

    protected void consume(ConsumerRecord<String, String> record, Acknowledgment ack) {
        List<CanalRow> rows = new ArrayList<>();
        FlatMessage message = null;
        var track = Thread.currentThread().getStackTrace()[2];
        KafkaListener listener = AnnotationUtil.fetchMethodAnnotation(track.getClassName(), track.getMethodName(), KafkaListener.class);
        CanalEventType type = null;
        Long begin = null;
        String dsCode = null;
        try {
            message = JSONUtil.parse(record.value(), FlatMessage.class);
            if (message.getIsDdl()) {//忽略ddl语句
                log.warn("发现ddl语句!，已自动忽略[{}]", message.getSql());
                return;
            }
            if (listener == null) {
                log.warn("canal listener is null!!!");
                return;
            }
            type = CanalEventType.valueOf(message.getType());
            rows = CanalUtil.fetchRows(message, type);
            String dataBase = message.getDatabase().replace("_0", "").replace("_1", "");
            dsCode = dbUtil.getSchemaCodeMap().get(dataBase);
            DataSourceHolder.set(dsCode);
            begin = System.currentTimeMillis();
            log.info("[起始]canal listen:[{}][{}][{}][topic:{}][分区:{}][{}]{}[{}]", dsCode, message.getTable(), type.name(), record.topic(), record.partition(), listener.groupId(), System.lineSeparator(), rows);
            switch (type) {
                case INSERT:
                    insert(rows);
                    break;
                case UPDATE:
                    update(rows);
                    break;
                case DELETE:
                    delete(rows);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            whenException(e, rows, record);
        } finally {
            ack.acknowledge();
            if (message != null && type != null && begin != null) {
                long cost = System.currentTimeMillis() - begin;
                log.info("[结束][{}ms]canal listen:[{}][{}][{}][topic:{}][分区:{}][{}]", cost, dsCode, message.getTable(), type.name(), record.topic(), record.partition(), listener.groupId());
            }
        }
    }

    protected abstract void insert(List<CanalRow> rows) throws Exception;

    protected abstract void update(List<CanalRow> rows) throws Exception;

    protected abstract void delete(List<CanalRow> rows) throws Exception;

    protected abstract void whenError(List<CanalRow> rows);

    protected void whenException(Exception e, List<CanalRow> rows, ConsumerRecord<String, String> record) {
        log.error("consume error: data: {}", record, e);
        //记录到mysql，等待后续处理
        whenError(rows);
    }
}
