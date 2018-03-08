package alpha.cyber.intelmain.bean;

import com.google.gson.Gson;

/**
 * Created by wangrui on 2018/3/7.
 */

public class OrgImgSlideListBean {
    /**
     * org_id : 1
     * rank : 1
     * id : 1
     * title : 轮播图1
     * url : /upload/xianlib/img/index/banner.png
     */

    private int org_id;
    private int rank;
    private int id;
    private String title;
    private String url;

    public static OrgImgSlideListBean objectFromData(String str) {

        return new Gson().fromJson(str, OrgImgSlideListBean.class);
    }

    public int getOrg_id() {
        return org_id;
    }

    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "OrgImgSlideListBean{" +
                "org_id=" + org_id +
                ", rank=" + rank +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
