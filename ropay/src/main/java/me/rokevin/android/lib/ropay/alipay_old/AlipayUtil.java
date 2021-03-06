package me.rokevin.android.lib.ropay.alipay_old;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import me.rokevin.android.lib.ropay.alipay.AliPayInfo;
import me.rokevin.android.lib.ropay.alipay.util.AliOrderInfoUtil;

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
     * @param aliPayInfo
     */
    public void pay(AliPayInfo aliPayInfo) {

        Map<String, String> params = AliOrderInfoUtil.buildOrderParamMap(aliPayInfo);

        String orderParam = AliOrderInfoUtil.buildOrderParam(params, true);

        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(aliPayInfo.getSign(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String orderInfo = orderParam + "&sign=" + encodedSign;

//        Log.e("TAG", "哈哈 orderInfo:" + orderInfo);

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起支付宝支付
     *
     * @param payInfo 服务端直接拼好的支付信息 orderParam + "&sign=" + encodedSign
     * @param sign    签名信息 sign
     */
    public void pay(final String payInfo, String sign) {

        if (TextUtils.isEmpty(payInfo) || TextUtils.isEmpty(sign)) {
            Log.e("AlipayUtil", "支付宝支付信息有误");
            return;
        }

        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String orderInfo = payInfo + "&sign=" + encodedSign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 调起支付宝支付
     *
     * @param payInfo 服务端直接拼好的支付信息 orderParam + "&sign=" + encodedSign
     */
    public void pay(final String payInfo) {

        if (TextUtils.isEmpty(payInfo)) {
            Log.e("AlipayUtil", "支付宝支付信息有误");
            return;
        }

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {

                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(payInfo, true);

                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
