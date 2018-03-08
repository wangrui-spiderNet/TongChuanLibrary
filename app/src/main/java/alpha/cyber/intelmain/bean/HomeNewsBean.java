package alpha.cyber.intelmain.bean;

import com.google.gson.Gson;

import java.util.List;

/**
 * Created by wangrui on 2018/2/11.
 */

public class HomeNewsBean {


    /**
     * orgImgInfo : {"news":"/upload/xianlib/img/index/info_news.png","org_id":1,"introduce":"/upload/xianlib/img/index/info_lib.png","more":"/upload/xianlib/img/index/info_more.png","tip":"/upload/xianlib/img/index/info_tip.png","id":1,"opentime":"/upload/xianlib/img/index/info_time.png","notice":"/upload/xianlib/img/index/info_card.png"}
     * technical_support : 西安青果信息技术服务有限公司
     * shuduier_service_qrcode : /asset/img/sdrservice_qrcode_for_8cm.jpg
     * orgImgSlideList : [{"org_id":1,"rank":1,"id":1,"title":"轮播图1","url":"/upload/xianlib/img/index/banner.png"}]
     * logo : /upload/xian_lib_logo.png
     * service_telephone : 029-89285041
     */

    private OrgImgInfoBean orgImgInfo;
    private String technical_support;
    private String shuduier_service_qrcode;
    private String logo;
    private String service_telephone;
    private List<OrgImgSlideListBean> orgImgSlideList;

    public static HomeNewsBean objectFromData(String str) {

        return new Gson().fromJson(str, HomeNewsBean.class);
    }

    public OrgImgInfoBean getOrgImgInfo() {
        return orgImgInfo;
    }

    public void setOrgImgInfo(OrgImgInfoBean orgImgInfo) {
        this.orgImgInfo = orgImgInfo;
    }

    public String getTechnical_support() {
        return technical_support;
    }

    public void setTechnical_support(String technical_support) {
        this.technical_support = technical_support;
    }

    public String getShuduier_service_qrcode() {
        return shuduier_service_qrcode;
    }

    public void setShuduier_service_qrcode(String shuduier_service_qrcode) {
        this.shuduier_service_qrcode = shuduier_service_qrcode;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getService_telephone() {
        return service_telephone;
    }

    public void setService_telephone(String service_telephone) {
        this.service_telephone = service_telephone;
    }

    public List<OrgImgSlideListBean> getOrgImgSlideList() {
        return orgImgSlideList;
    }

    public void setOrgImgSlideList(List<OrgImgSlideListBean> orgImgSlideList) {
        this.orgImgSlideList = orgImgSlideList;
    }

    @Override
    public String toString() {
        return "HomeNewsBean{" +
                "orgImgInfo=" + orgImgInfo +
                ", technical_support='" + technical_support + '\'' +
                ", shuduier_service_qrcode='" + shuduier_service_qrcode + '\'' +
                ", logo='" + logo + '\'' +
                ", service_telephone='" + service_telephone + '\'' +
                ", orgImgSlideList=" + orgImgSlideList +
                '}';
    }
}
