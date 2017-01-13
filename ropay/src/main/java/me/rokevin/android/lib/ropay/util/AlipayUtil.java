package me.rokevin.android.lib.ropay.util;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import me.rokevin.android.lib.ropay.modle.AliPayInfo;

/**
 * Created by luokaiwen on 15/5/18.
 * <p>
 * 支付宝支付帮助类
 */
public class AlipayUtil {

    private final String TAG = AlipayUtil.class.getSimpleName();

    private Activity mActivity;

    private Handler mHandler;

    private String mOrderInfo;

    public AlipayUtil(Activity activity, Handler handler) {

        mActivity = activity;
        mHandler = handler;
    }

    /**
     * 调起支付宝支付
     *
     * @param sign
     */
    public void pay(String sign) {

        try {
            sign = URLEncoder.encode(sign, "UTF-8");
            RoPayLog.e(TAG, "ALIPAYUTIL: sign:" + sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = mOrderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        new Thread() {
            public void run() {
                //AliPay alipay = new AliPay(PayActivity.this, mHandler);

                PayTask alipay = new PayTask(mActivity);
                String result = alipay.pay(payInfo);

                // 设置为沙箱模式，不设置默认为线上环境
                //alipay.setSandBox(true);

                //String result = alipay.pay(orderInfo);

                RoPayLog.e(TAG, "result = " + result);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;

                mHandler.sendMessage(msg);
            }
        }.start();
    }

    /**
     * 组装签名字符串
     *
     * @param alipayInfo 支付信息
     * @return 待签名的字符串
     */
    public String assemblySignInfo(AliPayInfo alipayInfo) {

        String partner = alipayInfo.getPartner();
        String sellerId = alipayInfo.getSellerId();
        String outTradeNo = alipayInfo.getOutTradeNo();
        String subject = alipayInfo.getSubject();
        String body = alipayInfo.getBody();
        String totalFee = alipayInfo.getTotalFee();
        String notifyUrl = alipayInfo.getNotifyUrl();

        mOrderInfo = "partner=\"" + partner + "\"" +
                "&seller_id=\"" + sellerId + "\"" +
                "&out_trade_no=\"" + outTradeNo + "\"" +
                "&subject=\"" + subject + "\"" +
                "&body=\"" + body + "\"" +
                "&total_fee=\"" + totalFee + "\"" +
                "&notify_url=\"" + notifyUrl + "\"" +
                "&service=\"mobile.securitypay.pay\"" +
                "&payment_type=\"1\"" +
                "&_input_charset=\"utf-8\"&" +
                "it_b_pay=\"30m\"";

        RoPayLog.e(TAG, "支付宝支付签名的字符串:" + mOrderInfo);

        return mOrderInfo;
    }

    /**
     * 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
