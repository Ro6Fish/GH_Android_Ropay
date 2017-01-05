package me.rokevin.android.lib.ropay.modle;

/**
 * Created by luokaiwen on 17/1/5.
 * <p>
 * 支付宝支付需要的信息
 */

public class AliPayInfo {

    /**
     * 商户PID(商户合作ID)
     */
    private String partner;

    /**
     * 商户收款账号
     */
    private String sellerId;

    /**
     * 交易编号
     */
    private String outTradeNo;

    /**
     * 商品名称
     */
    private String subject;

    /**
     * 交易描述
     */
    private String body;

    /**
     * 交易回调地址
     */
    private String notifyUrl;

    /**
     * 交易金额
     */
    private String totalFee;

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
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

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    @Override
    public String toString() {
        return "AliPayInfo{" +
                "partner='" + partner + '\'' +
                ", sellerId='" + sellerId + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", totalFee='" + totalFee + '\'' +
                '}';
    }
}
