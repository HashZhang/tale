package com.tale.service;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.DateKit;
import com.tale.model.entity.Events;
import io.github.biezhi.anima.Anima;
import io.github.biezhi.anima.page.Page;

import static com.tale.bootstrap.TaleConst.SQL_QUERY_EVENTS;
import static io.github.biezhi.anima.Anima.select;

@Bean
public class EventsService {
    public void savePostEvents(Integer pid) {
        Events events = new Events();
        events.setJid(pid);
        events.setType(Events.EventType.POST.getValue());
        events.setDescription(Events.EventType.POST.getTitle());
        events.setCreated(DateKit.nowUnix());
        events.save();
    }

    public void addEvents(int type, String description, String img) {
        Events events = new Events();
        events.setType(type);
        events.setDescription(description);
        events.setImg(img);
        events.setCreated(DateKit.nowUnix());
        events.save();
    }

    public void removeEvents(int eid) {
        Anima.delete().from(Events.class).where(Events::getEid, eid).execute();
    }

    public Page<Events> getEvents(int page, int limit) {
        return select().bySQL(Events.class, SQL_QUERY_EVENTS).page(page, limit);
    }
}
