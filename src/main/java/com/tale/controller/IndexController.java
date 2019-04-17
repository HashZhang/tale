package com.tale.controller;

import com.blade.ioc.annotation.Inject;
import com.blade.kit.StringKit;
import com.blade.mvc.RouteContext;
import com.blade.mvc.annotation.*;
import com.blade.mvc.http.Request;
import com.blade.mvc.http.Response;
import com.blade.mvc.http.Session;
import com.tale.bootstrap.TaleConst;
import com.tale.extension.Commons;
import com.tale.model.dto.Archive;
import com.tale.model.dto.Types;
import com.tale.model.entity.Contents;
import com.tale.model.entity.Events;
import com.tale.model.params.PageParam;
import com.tale.service.EventsService;
import com.tale.service.SiteService;
import com.tale.utils.TaleUtils;
import io.github.biezhi.anima.enums.OrderBy;
import io.github.biezhi.anima.page.Page;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.tale.extension.Commons.theme_url;
import static com.tale.extension.Theme.intro;
import static com.tale.extension.Theme.show_thumb;
import static io.github.biezhi.anima.Anima.select;

/**
 * 首页、归档、Feed、评论
 *
 * @author biezhi
 * @since 1.3.1
 */
@Path
@Slf4j
public class IndexController extends BaseController {

    @Inject
    private SiteService siteService;
    @Inject
    private EventsService eventsService;

    /**
     * 首页
     *
     * @return
     */
    @GetRoute
    public String index(Request request, PageParam pageParam) {
        return this.index(request, 1, pageParam.getLimit());
    }

    /**
     * 首页分页
     *
     * @param request
     * @param page
     * @param limit
     * @return
     */
    @GetRoute(value = {"page/:page", "page/:page.html"})
    public String index(Request request, @PathParam int page, @Param(defaultValue = "12") int limit) {
        page = page < 0 || page > TaleConst.MAX_PAGE ? 1 : page;
        if (page > 1) {
            this.title(request, "第" + page + "页");
        }
        request.attribute("page_num", page);
        request.attribute("limit", limit);
        request.attribute("is_home", true);
        request.attribute("page_prefix", "/page");
        return this.render("index");
    }


    /**
     * 搜索页
     *
     * @param keyword
     * @return
     */
    @GetRoute(value = {"search/:keyword", "search/:keyword.html"})
    public String search(Request request, @PathParam String keyword, @Param(defaultValue = "12") int limit) {
        return this.search(request, keyword, 1, limit);
    }

    @GetRoute(value = {"search", "search.html"})
    public String search(Request request, @Param(defaultValue = "12") int limit) {
        String keyword = request.query("s").orElse("");
        return this.search(request, keyword, 1, limit);
    }

    @GetRoute(value = {"search/:keyword/:page", "search/:keyword/:page.html"})
    public String search(Request request, @PathParam String keyword, @PathParam int page, @Param(defaultValue = "12") int limit) {

        page = page < 0 || page > TaleConst.MAX_PAGE ? 1 : page;

        Page<Contents> articles = select().from(Contents.class)
                .where(Contents::getType, Types.ARTICLE)
                .and(Contents::getStatus, Types.PUBLISH)
                .like(Contents::getTitle, "%" + keyword + "%")
                .order(Contents::getCreated, OrderBy.DESC)
                .page(page, limit);

        request.attribute("articles", articles);
        request.attribute("type", "搜索");
        request.attribute("keyword", keyword);
        request.attribute("page_prefix", "/search/" + keyword);
        return this.render("page-category");
    }

    /**
     * 归档页
     *
     * @return
     */
    @GetRoute(value = {"archives", "archives.html"})
    public String archives(Request request) {
        List<Archive> archives = siteService.getArchives();
        request.attribute("archives", archives);
        request.attribute("is_archive", true);
        return this.render("archives");
    }

    @GetRoute(value = {"timeline", "timeline.html"})
    public String timeline(Request request) {
        Page<Events> events = eventsService.getEvents(0, 10);
        events.getRows().forEach(event -> {
            if (event.getType() == Events.EventType.POST.getValue()) {
                Contents contents = select().from(Contents.class).byId(event.getJid());
                event.setImg(show_thumb(contents));
                event.setTitle(contents.getTitle());
                event.setDescription(intro(contents.getContent(), 75));
                event.setIcon(theme_url(Events.EventType.POST.getIcon()));
            } else {
                for (Events.EventType eventType : Events.EventType.values()) {
                    if (eventType.getValue() == event.getType()) {
                        event.setIcon(theme_url(eventType.getIcon()));
                        event.setTitle(eventType.getTitle());
                        break;
                    }
                }
            }
            event.setFormattedDateTime(Commons.fmtdate(event.getCreated(), "yyyy-MM-dd hh:mm:ss"));
        });
        request.attribute("events", events);
        return this.render("timeline");
    }

    /**
     * feed页
     *
     * @return
     */
    @GetRoute(value = {"feed", "feed.xml", "atom.xml"})
    public void feed(Response response) {

        List<Contents> articles = select().from(Contents.class)
                .where(Contents::getType, Types.ARTICLE)
                .and(Contents::getStatus, Types.PUBLISH)
                .and(Contents::getAllowFeed, true)
                .order(Contents::getCreated, OrderBy.DESC)
                .all();

        try {
            String xml = TaleUtils.getRssXml(articles);
            response.contentType("text/xml; charset=utf-8");
            response.body(xml);
        } catch (Exception e) {
            log.error("生成 rss 失败", e);
        }
    }

    /**
     * sitemap 站点地图
     *
     * @return
     */
    @GetRoute(value = {"sitemap", "sitemap.xml"})
    public void sitemap(Response response) {
        List<Contents> articles = select().from(Contents.class)
                .where(Contents::getType, Types.ARTICLE)
                .and(Contents::getStatus, Types.PUBLISH)
                .and(Contents::getAllowFeed, true)
                .order(Contents::getCreated, OrderBy.DESC)
                .all();
        try {
            String xml = TaleUtils.getSitemapXml(articles);
            response.contentType("text/xml; charset=utf-8");
            response.body(xml);
        } catch (Exception e) {
            log.error("生成 sitemap 失败", e);
        }
    }

    /**
     * 注销
     */
    @Route(value = "logout")
    public void logout(RouteContext context) {
        TaleUtils.logout(context);
    }

}