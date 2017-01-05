package me.rokevin.android.ropay;

/**
 * Created by luokaiwen on 17/1/3.
 * <p>
 * 支付配置
 */
public class PayConfig {

    private static final String TAG = PayConfig.class.getSimpleName();

    /**
     * 用商户私钥签名,支付回调地址
     */

    // 商户PID(商户合作ID)
    public static final String ALI_PARTNER_TEST = "";
    // 商户收款账号
    public static final String ALI_SELLER_TEST = "";

    // 商户PID(商户合作ID) 新疆
    public static final String ALI_PARTNER = "";
    // 商户收款账号 新疆
    public static final String ALI_SELLER = "";

    // 支付宝回调给服务器地址
    public static final String ALI_PAY_URL_CALL_BACK_TEST = "";     // 测试支付回调地址

    // 支付宝回调给服务器地址
    public static final String ALI_PAY_URL_CALL_BACK = "";              // 线上支付回调地址

    // 商户私钥，pkcs8格式
    public static final String ALI_RSA_PRIVATE = "";

    // 支付宝公钥
    public static final String ALI_RSA_PUBLIC = "";

    /**
     * 微信AppID
     */
    public static final String WX_APP_ID_TEST = ""; // 微信APP_ID
    public static final String WX_APP_ID = ""; // 新疆微信 APP_ID

    /**
     * 获取支付地址
     *
     * @return
     */
    public static String getPartner() {

        if (BuildConfig.DEBUG) {
            return ALI_PARTNER_TEST;
        } else {
            return ALI_PARTNER;
        }
    }

    /**
     * 获取支付地址
     *
     * @return
     */
    public static String getSeller() {

        if (BuildConfig.DEBUG) {
            return ALI_SELLER_TEST;
        } else {
            return ALI_SELLER;
        }
    }

    /**
     * 获取支付地址
     *
     * @return
     */
    public static String getPayUrl() {

        if (BuildConfig.DEBUG) {
            return ALI_PAY_URL_CALL_BACK_TEST;
        } else {
            return ALI_PAY_URL_CALL_BACK;
        }
    }

    /**
     * 微信还有 商户ID(收钱的) KEY(属于一个商户) AppId(应用的ID) AppSecret
     *
     * https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12
     */

    /**
     * 获取微信AppId
     *
     * @return
     */
    public String getWXAppId() {

        if (BuildConfig.DEBUG) {
            return WX_APP_ID_TEST;
        } else {
            return WX_APP_ID;
        }
    }
}
