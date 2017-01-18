package me.rokevin.android.lib.ropay.alipay;

/**
 * Created by luokaiwen on 17/1/5.
 * <p>
 * 支付宝支付需要的信息
 */

public class AliPayInfo {

    private String appId;
    private String outTradNo;
    private String subject;
    private String body;
    private String totalAmount;
    private String sign;
    private String notifyUrl;
    private boolean isRas2;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getOutTradNo() {
        return outTradNo;
    }

    public void setOutTradNo(String outTradNo) {
        this.outTradNo = outTradNo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public boolean isRas2() {
        return isRas2;
    }

    public void setRas2(boolean ras2) {
        isRas2 = ras2;
    }

    @Override
    public String toString() {
        return "AliPayInfo{" +
                "appId='" + appId + '\'' +
                ", outTradNo='" + outTradNo + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", sign='" + sign + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", isRas2=" + isRas2 +
                '}';
    }
}
