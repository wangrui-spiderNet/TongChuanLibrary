package alpha.cyber.intelmain.bean;

import com.google.gson.Gson;

/**
 * Created by wangrui on 2018/3/7.
 */

public class OrgImgInfoBean {
    /**
     * news : /upload/xianlib/img/index/info_news.png
     * org_id : 1
     * introduce : /upload/xianlib/img/index/info_lib.png
     * more : /upload/xianlib/img/index/info_more.png
     * tip : /upload/xianlib/img/index/info_tip.png
     * id : 1
     * opentime : /upload/xianlib/img/index/info_time.png
     * notice : /upload/xianlib/img/index/info_card.png
     */

    private String news;
    private int org_id;
    private String introduce;
    private String more;
    private String tip;
    private int id;
    private String opentime;
    private String notice;

    public static OrgImgInfoBean objectFromData(String str) {

        return new Gson().fromJson(str, OrgImgInfoBean.class);
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public String toString() {
        return "OrgImgInfoBean{" +
                "news='" + news + '\'' +
                ", org_id=" + org_id +
                ", introduce='" + introduce + '\'' +
                ", more='" + more + '\'' +
                ", tip='" + tip + '\'' +
                ", id=" + id +
                ", opentime='" + opentime + '\'' +
                ", notice='" + notice + '\'' +
                '}';
    }
}
