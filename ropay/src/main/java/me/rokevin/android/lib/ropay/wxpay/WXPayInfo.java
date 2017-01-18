package me.rokevin.android.lib.ropay.wxpay;

/**
 * Created by luokaiwen on 16/6/27.
 * <p>
 * 微信支付参数
 */
public class WXPayInfo {

    // 字段名	             变量名	    类型	        必填	示例值	                              描述
    // 应用ID	        appid	    String(32)	是	wx8888888888888888	                  微信开放平台审核通过的应用APPID
    // 商户号 	        partnerid	String(32)	是	1900000109	                          微信支付分配的商户号
    // 预支付交易会话ID	prepayid	String(32)	是	WX1217752501201407033233368018	     微信返回的支付交易会话ID
    // 扩展字段	        package	    String(128)	是	Sign=WXPay	                          暂填写固定值Sign=WXPay
    // 随机字符串	        noncestr	String(32)	是	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	 随机字符串，不长于32位。推荐随机数生成算法
    // 时间戳	            timestamp	String(10)	是	1412000000	                          时间戳，请见接口规则-参数规定
    // 签名	            sign	    String(32)	是	C380BEC2BFD727A4B6845133519F3AD6	 签名，详见签名生成算法

    private String appId;
    private String partnerId;
    private String prepayId;
    private String noncestr;
    private String timestamp;
    private String packageValue;
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "WXPayInfo{" +
                "appId='" + appId + '\'' +
                ", partnerId='" + partnerId + '\'' +
                ", prepayId='" + prepayId + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", packageValue='" + packageValue + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
