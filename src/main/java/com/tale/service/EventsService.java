package com.tale.service;

import com.blade.ioc.annotation.Bean;
import com.blade.kit.DateKit;
import com.tale.model.entity.Events;
import io.github.biezhi.anima.Anima;

@Bean
public class EventsService {
    public void savePostEvents(Integer pid) {
        Events events = new Events();
        events.setJid(pid);
        events.setType(Events.EventType.POST.getValue());
        events.setDescription(Events.EventType.POST.getDescription());
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
}
