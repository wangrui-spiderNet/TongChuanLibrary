package alpha.cyber.intelmain.bean;

/**
 * Created by wangrui on 2018/2/11.
 */

public class HomeNewsBean {

    private String imgUrl;
    private String name;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "HomeNewsBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
