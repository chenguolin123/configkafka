package com.senghong.kafka.model;

import com.senghong.common.model.UserModel;
import lombok.Data;

/**
 * todo: kafka base model，所有自定义kafka消息必须继承此类
 *
 * @author cgl
 * @since 2022/7/6
 */
@Data
public class KafkaBaseModel {
    public Long id;                 //id
    private String dsCode;          //dsCode
    private UserModel user;    //user


    public static KafkaBaseModel of(Long id) {
        KafkaBaseModel model = new KafkaBaseModel();
        model.setId(id);
        return model;
    }
}
