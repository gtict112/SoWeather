package com.example.administrator.soweather.com.example.administrator.soweather.mode;

import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */

public class MoodLineDate {
    public String tagid;
    public String cityname;
    public List<Content> content;

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public class Content {
        public String content_txt;
        public String contentid;

        public String getContent_txt() {
            return content_txt;
        }

        public void setContent_txt(String content_txt) {
            this.content_txt = content_txt;
        }

        public String getContentid() {
            return contentid;
        }

        public void setContentid(String contentid) {
            this.contentid = contentid;
        }
    }
}
