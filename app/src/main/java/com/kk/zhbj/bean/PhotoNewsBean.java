package com.kk.zhbj.bean;

import java.util.ArrayList;

/**
 * 注释：  <br>
 * 作者： kk <br>
 * QQ  : <br>
 * 创建时间：2016/10/14 - 9:50 <br>
 */

public class PhotoNewsBean {

    public PhotoNewsBeanData data;
    public int retcode;

    @Override
    public String toString() {
        return "PhotoNewsBean{" +
                "data=" + data +
                ", retcode=" + retcode +
                '}';
    }

    public class PhotoNewsBeanData {
        public String more;
        public String title;
        public ArrayList<NewsBean> news;

        @Override
        public String toString() {
            return "PhotoNewsBeanData{" +
                    "more='" + more + '\'' +
                    ", title='" + title + '\'' +
                    ", news=" + news +
                    '}';
        }
    }


    public class NewsBean {
        public int id;
        public String listimage;
        public String pubdate;
        public String title;

        @Override
        public String toString() {
            return "NewsBean{" +
                    "listimage='" + listimage + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
