package me.rokevin.android.lib.ropay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import me.rokevin.android.lib.ropay.modle.AliPayInfo;
import me.rokevin.android.lib.ropay.modle.WXPayInfo;
import me.rokevin.android.lib.ropay.util.AlipayResultUtil;
import me.rokevin.android.lib.ropay.util.AlipayUtil;
import me.rokevin.android.lib.ropay.util.RoPayLog;

/**
 * Created by luokaiwen on 15/7/13.
 * <p>
 * 支付帮助类
 */
public class RoPay {

    private static final String TAG = RoPay.class.getSimpleName();
    private Activity mActivity;
    private Handler mHandler;
    private AlipayUtil mAlipayUtil;
    private WXPayReceiver mWXPayReceiver;

    /**
     * 支付方式 3:支付宝, 1:微信支付
     */
    private String mPayment = "";

    public RoPay(Activity activity) {

        mActivity = activity;
        initPay();
    }

    public void initPay() {

        // 支付宝Handler回调处理
        mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (1 == msg.what) {
                    // 支付宝返回结果处理帮助类
                    AlipayResultUtil result = new AlipayResultUtil((String) msg.obj);

                    // 获取返回的信息
                    String info = result.getMemo();

                    if (AlipayResultUtil.SUCC.equals(result.getResultStatus())) {

                        // 成功
                        if (null != mOnPayCallback) {
                            mOnPayCallback.onAlipaySucc();
                        }

                    } else {

                        if (null != mOnPayCallback) {
                            mOnPayCallback.onAlipayFail();
                        }

                        Toast.makeText(mActivity, info, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            ;
        };

        mAlipayUtil = new AlipayUtil(mActivity, mHandler);

        // 微信广播通知
        mWXPayReceiver = new WXPayReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);

                if (null != mOnPayCallback) {

                    int code = intent.getExtras().getInt("1");

                    String result = "微信支付失败";

                    RoPayLog.e(TAG, "微信支付code:" + code);

                    switch (code) {
                        case BaseResp.ErrCode.ERR_OK:

                            if (null != mOnPayCallback) {

                                result = "微信支付成功";
                                mOnPayCallback.onWxpaySucc();
                            }

                            break;

                        case BaseResp.ErrCode.ERR_USER_CANCEL:

                            if (null != mOnPayCallback) {

                                result = "取消微信支付";
                                mOnPayCallback.onWxpayFail();
                            }

                            break;

                        case BaseResp.ErrCode.ERR_AUTH_DENIED:

                            if (null != mOnPayCallback) {
                                result = "微信支付失败";
                                mOnPayCallback.onWxpayFail();
                            }

                            break;

                        default:

                            if (null != mOnPayCallback) {
                                result = "微信支付失败";
                                mOnPayCallback.onWxpayFail();
                            }

                            break;
                    }

                    Toast.makeText(mActivity, result, Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    /**
     * 获取支付宝待签名字符串
     *
     * @param aliPayInfo
     * @return
     */
    public String getOrderInfo(AliPayInfo aliPayInfo) {

        return mAlipayUtil.assemblySignInfo(aliPayInfo);
    }

    /**
     * 注册支付信息
     */
    public void registPay() {

        // 注册广播接收器（动态注册）
        IntentFilter filter = new IntentFilter();
        filter.addAction(WXPayReceiver.ACTION_WXPAY);
        mActivity.registerReceiver(mWXPayReceiver, filter);
    }

    /**
     * 解绑支付信息
     */
    public void unregistPay() {

        mActivity.unregisterReceiver(mWXPayReceiver);
    }

    /**
     * 调起微信支付 服务端返回的微信支付参数结果
     *
     * @param wxpayInfo 微信支付参数
     * @param handler   回调处理
     */
    public void doWxpay(WXPayInfo wxpayInfo, IWXAPIEventHandler handler) {

        // 微信AppId
        String appId = wxpayInfo.getAppId();

        IWXAPI api = WXAPIFactory.createWXAPI(mActivity, appId, false);
        api.handleIntent(mActivity.getIntent(), handler);
        api.openWXApp();

        boolean isInstalled = api.isWXAppInstalled();

        if (!isInstalled) {

            if (null != mOnPayCallback) {
                mOnPayCallback.onWxpayFail();
            }

            Toast.makeText(mActivity, "您没有安装微信，不能进行微信支付", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;

        if (isPaySupported) {
            // 发送微信支付

            PayReq req = new PayReq();

            req.appId = appId;
            //req.appId = wxpayInfo.getAppId();
            req.partnerId = wxpayInfo.getPartnerId();
            req.prepayId = wxpayInfo.getPrepayId();
            req.nonceStr = wxpayInfo.getNoncestr();
            req.timeStamp = wxpayInfo.getTimestamp();
            req.packageValue = wxpayInfo.getPackageValue();
            //req.packageValue = "Sign=WXPay";
            req.sign = wxpayInfo.getSign();

            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            boolean isRegister = api.registerApp(appId);
            RoPayLog.e("", "isRegister:" + isRegister);

            boolean isSend = api.sendReq(req);
            Log.e(TAG, "isSend:" + isSend);
            Log.e(TAG, "appId:" + appId);
            Log.e(TAG, "partnerId:" + wxpayInfo.getPartnerId());
            Log.e(TAG, "nonceStr:" + wxpayInfo.getNoncestr());
            Log.e(TAG, "timeStamp:" + wxpayInfo.getTimestamp());
            Log.e(TAG, "packageValue:" + wxpayInfo.getPackageValue());
            Log.e(TAG, "sign:" + wxpayInfo.getSign());

        } else {

            if (null != mOnPayCallback) {
                mOnPayCallback.onWxpayFail();
            }

            Toast.makeText(mActivity, "此版本微信不支持微信支付", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 调起支付宝支付
     *
     * @param sign 服务器返回的请求字符串的签名字符串
     */
    public void doAlipay(String sign) {

        // 调用支付宝支付
        mAlipayUtil.pay(sign);
    }

    /**
     * 获取支付方式
     *
     * @return
     */
    public String getPayWay() {
        return mPayment;
    }

    /**
     * 设置支付方式
     *
     * @param mPayWay
     */
    public void setPayWay(String mPayWay) {
        this.mPayment = mPayWay;
    }

    /**
     * 确认支付
     *
     * @param realPrice
     */
    public void doConfirmPay(String realPrice) {

        if (null != mOnPayConfirmListener) {
            mOnPayConfirmListener.onConfirm(realPrice);
        }
    }

    private OnPayCallback mOnPayCallback;

    private OnPayConfirmListener mOnPayConfirmListener;

    public void setOnPayListener(OnPayCallback onPayCallback) {
        mOnPayCallback = onPayCallback;
    }

    public void setOnPayConfirmListener(OnPayConfirmListener onPayConfirmListener) {

        mOnPayConfirmListener = onPayConfirmListener;
    }

    /**
     * 支付回调
     */
    public interface OnPayCallback {

        void onAlipaySucc();

        void onAlipayFail();

        void onWxpaySucc();

        void onWxpayFail();
    }

    /**
     * 支付确认监听
     */
    public interface OnPayConfirmListener {

        void onConfirm(String realPrice);
    }
}
