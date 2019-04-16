package com.tale.model.entity;

import io.github.biezhi.anima.Model;
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
        SETTLE_DOWN(0, "网站建立"),
        POST(1, "发布文章"),
        VISIT(2, "逛逛看看"),
        DINNER(3, "吃吃喝喝"),
        DEPART(4, "出发旅行"),
        BACK(5, "旅行归来");

        private int value;
        private String description;

        EventType(int value, String description) {
            this.value = value;
            this.description = description;
        }
    }
}
