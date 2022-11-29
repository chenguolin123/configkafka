package com.senghong.kafka.constant;

/**
 * note:
 *
 * @author cgl
 * @since 2021/8/17
 */
public class KafkaConstant {
    //服务 group
    public static final String GROUP_WAYBILL = "waybill_group";
    public static final String GROUP_PRICE = "price_group";
    public static final String GROUP_BASIC_DATA = "basic_data_group";
    public static final String GROUP_USER = "user_group";

    //es group
    //运单
    public static final String ES_WAYBILL_GROUP = "es_waybill";
    //费用
    public static final String ES_FEE_GROUP = "es_fee";
    //问题件
    public static final String ES_ISSUE_GROUP = "es_issue";

    //0费用
    public static final String GROUP_ZERO_FEE = "zero_fee";
    //总账相关，汇总任务
    public static final String GROUP_SUM_TASK = "sum_task";
    //一票单
    public static final String ONE_BILL_TASK = "one_bill_task";

    //topics

    //延迟队列
    public static final String TOPIC_DELAY_TOPIC = "delay_message";
    //mysql topic
    public static final String TOPIC_CANAL_WAYBILL = "waybill_waybill";
    public static final String TOPIC_CANAL_WAYBILL_INFO = "waybill_waybill_info";
    public static final String TOPIC_CANAL_WAYBILL_PACKAGE = "waybill_waybill_package";
    public static final String TOPIC_CANAL_WAYBILL_RECIPIENT = "waybill_waybill_recipient";
    public static final String TOPIC_CANAL_WAYBILL_SENDER = "waybill_waybill_sender";
    public static final String TOPIC_CANAL_PIECE = "waybill_piece";
    public static final String TOPIC_CANAL_WAYBILL_WARN_TRACK = "waybill_waybill_watch_track";
    public static final String TOPIC_CANAL_FEE = "waybill_fee";
    public static final String TOPIC_CANAL_ISSUE = "waybill_issue";
    //oms同步
    public static final String TOPIC_OMS_SYNC_SITE = "oms_sync_site";
    public static final String TOPIC_OMS_SYNC_CHANNEL = "oms_sync_channel";
    public static final String TOPIC_OMS_SYNC_ORDER_TYPE = "oms_sync_order_type";
    public static final String TOPIC_OMS_SYNC_TRACK_STATUS = "oms_sync_track_status";
    public static final String TOPIC_OMS_SYNC_WAREHOUSE = "oms_sync_warehouse";
    public static final String TOPIC_OMS_SYNC_USERS = "oms_sync_users";
    public static final String TOPIC_OMS_SYNC_CUSTOMER = "oms_sync_customer";
    public static final String TOPIC_OMS_SYNC_CUSTOMER_CONTACT = "oms_sync_customer_contact";
    public static final String TOPIC_OMS_SYNC_RETURN_TYPE = "oms_sync_return_type";
    //运单最新状态同步
    public static final String TOPIC_WAYBILL_LATEST_STATUS_SYNC = "waybill_latest_status_sync";
    //渠道补全
    public static final String TOPIC_CHANNEL_COMPLETE = "channel_complete";
    //费用重算
    public static final String TOPIC_FEE_RECAL = "fee_recal";
    //0费用
    public static final String TOPIC_ZERO_FEE = "zero_fee";
    //报价修改、新增
    public static final String TOPIC_PRICE_SAVE = "price_save";
    /**
     * 制作/撤销账单
     */
    public static final String TOPIC_MAKE_BILL = "make_bill";
    //修改账单汇率
    public static final String TOPIC_UPDATE_BILL_EXCHANGE_RATE = "update_bill_exchange_rate";
    //收付款申请
    public static final String TOPIC_CHARGE_PAY_APPLY = "charge_pay_apply";
    //其他表
    public static final String TOPIC_OTHER_TABLE = "other_table";

}
