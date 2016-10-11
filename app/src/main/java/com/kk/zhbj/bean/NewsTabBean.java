package com.kk.zhbj.bean;

import java.util.ArrayList;

/**
 * 注释：封装新闻标签的详细内容  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/10 - 18:32 <br>
 */

public class NewsTabBean {
    public NewsTabdata data;

    @Override
    public String toString() {
        return "NewsTabBean{" +
                "data=" + data +
                '}';
    }

    public class NewsTabdata {
        public String more;
        public ArrayList<NewsTabnews> news;
        public ArrayList<NewsTabtopnews> topnews;

        @Override
        public String toString() {
            return "NewsTabdata{" +
                    "more='" + more + '\'' +
                    ", news=" + news +
                    ", topnews=" + topnews +
                    '}';
        }
    }

    public class NewsTabnews {
        public int id;
        public String listimage;
        public String pubdate;
        public String title;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabnews{" +
                    "id=" + id +
                    ", listimage='" + listimage + '\'' +
                    ", pubdate='" + pubdate + '\'' +
                    ", title='" + title + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public class NewsTabtopnews {
        public int id;
        public String pubdate;
        public String title;
        public String topimage;
        public String type;
        public String url;

        @Override
        public String toString() {
            return "NewsTabtopnews{" +
                    "id=" + id +
                    ", pubdate='" + pubdate + '\'' +
                    ", title='" + title + '\'' +
                    ", topimage='" + topimage + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }


}
