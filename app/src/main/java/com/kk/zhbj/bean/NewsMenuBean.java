package com.kk.zhbj.bean;

import java.util.ArrayList;

/**
 * 注释：封装有Gson解析的字段  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/8 - 21:46 <br>
 */

public class NewsMenuBean {
    public ArrayList<NewsData> data;
    public ArrayList<Integer> extend;
    public int retcode;

    @Override
    public String toString() {
        return "NewMenuBean{" +
                "data=" + data +
                '}';
    }

    public class NewsData {
        public ArrayList<ChildContent> children;
        public int id;
        public String title;
        public int type;

        @Override
        public String toString() {
            return "NewsData{" +
                    "title='" + title + '\'' +
                    ", children=" + children +
                    '}';
        }
    }

    public class ChildContent {
        public int id;
        public String title;
        public int type;
        public String url;

        @Override
        public String toString() {
            return "ChildContent{" +
                    "title='" + title + '\'' +
                    ", url='" + url + '\'' +
                    '}';
        }
    }
}
