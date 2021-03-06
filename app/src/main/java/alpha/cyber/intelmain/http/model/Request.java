package alpha.cyber.intelmain.http.model;


import java.util.Hashtable;
import java.util.Map;

import alpha.cyber.intelmain.MyApplication;
import alpha.cyber.intelmain.util.AppSharedPreference;
import alpha.cyber.intelmain.util.DeviceUtils;

/**
 * Created by huxin on 16/6/13.
 */
public class Request {

    private ClientInfo clientInfo;
    private Map<String, Object> data = new Hashtable<>();

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public static class Builder {
        private ClientInfo clientInfo = new ClientInfo();
        private Map<String, Object> data = new Hashtable<>();

        public Builder() {

        }

        public Builder withClientInfo(ClientInfo clientInfo) {
            this.clientInfo = clientInfo;
            return this;
        }

        public Builder withData(Map<String, Object> data) {
            this.data = data;
            return this;
        }

        public Builder withParam(String key, Object value) {
            this.data.put(key, value);
            return this;
        }

        public Request build() {
            Request request = new Request();
            request.setClientInfo(this.clientInfo);
            request.setData(this.data);
            return request;
        }
    }

    private static class ClientInfo {
        private String version = DeviceUtils.getVersionName(MyApplication.getInstance());
        private String clientType = "android";
        private String clientModel = DeviceUtils.getDeviceModel();
        private String clientOs = DeviceUtils.getOS();
        private String cNet = DeviceUtils.getNetworkType(MyApplication.getInstance());
        private String clientMac = DeviceUtils.getMacAddressByWifi(MyApplication.getInstance().getApplicationContext());
        private String clientToken = AppSharedPreference.getInstance().getClientXgToken();

        public String getClientToken() {
            return clientToken;
        }

        public void setClientToken(String clientToken) {
            this.clientToken = clientToken;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }

        public String getClientModel() {
            return clientModel;
        }

        public void setClientModel(String clientModel) {
            this.clientModel = clientModel;
        }

        public String getClientOs() {
            return clientOs;
        }

        public void setClientOs(String clientOs) {
            this.clientOs = clientOs;
        }

        public String getcNet() {
            return cNet;
        }

        public void setcNet(String cNet) {
            this.cNet = cNet;
        }

        public String getClientMac() {
            return clientMac;
        }

        public void setClientMac(String clientMac) {
            this.clientMac = clientMac;
        }
    }
}
