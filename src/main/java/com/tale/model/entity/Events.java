package com.tale.model.entity;

import io.github.biezhi.anima.Model;
import io.github.biezhi.anima.annotation.Ignore;
import io.github.biezhi.anima.annotation.Table;
import lombok.Data;
import lombok.Getter;

/**
 * 事件
 */
@Data
@Table(name = "t_events", pk = "eid")
public class Events extends Model {
    // Event表主键
    private Integer eid;

    // 关联id,关联字段
    private Integer jid;

    // 事件类型
    private Integer type;

    // 事件描述
    private String description;

    // 事件相关图片地址
    private String img;

    // 事件创建时间
    private Integer created;

    @Getter
    public enum EventType {
        SETTLE_DOWN(0, "网站建立", "/static/img/icon/1230464.png"),
        POST(1, "发布文章", "/static/img/icon/1209882.png"),
        VISIT(2, "逛逛买买", "/static/img/icon/1211267.png"),
        DINNER(3, "吃吃喝喝", "/static/img/icon/1211255.png"),
        FLIGHT_DEPART(4, "飞机旅行", "/static/img/icon/1209897.png"),
        FLIGHT_BACK(5, "飞机归来", "/static/img/icon/1209897.png"),
        TRAIN_DEPART(6, "火车旅行", "/static/img/icon/1209867.png"),
        TRAIN_BACK(7, "火车归来", "/static/img/icon/1209867.png"),
        DRIVE_DEPART(8, "自驾旅行", "/static/img/icon/1209887.png"),
        DRIVE_BACK(9, "自驾归来", "/static/img/icon/1209887.png"),
        MONUMENTS(10, "名胜古迹", "/static/img/icon/1211265.png"),
        SEA(11, "海边风光", "/static/img/icon/1211251.png"),
        MOUNTAIN(12, "游山玩水", "/static/img/icon/1209898.png"),
        SITE(13, "自然景点", "/static/img/icon/1209870.png"),
        PARK(14, "公园拍拍", "/static/img/icon/1209876.png"),
        ;


        private int value;
        private String title;
        private String icon;

        EventType(int value, String title, String icon) {
            this.value = value;
            this.title = title;
            this.icon = icon;
        }
    }

    @Ignore
    private String title;
    @Ignore
    private String icon;
    @Ignore
    private String formattedDateTime;
    @Ignore
    private String url;
}
