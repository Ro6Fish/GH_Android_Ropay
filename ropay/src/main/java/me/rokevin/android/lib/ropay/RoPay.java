package me.rokevin.android.lib.ropay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.chinapay.cppaysdk.activity.Initialize;
import com.chinapay.cppaysdk.bean.OrderInfo;
import com.chinapay.cppaysdk.global.CPGlobalInfo;
import com.chinapay.cppaysdk.global.ResultInfo;
import com.chinapay.cppaysdk.util.Utils;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import me.rokevin.android.lib.ropay.alipay.AliPayInfo;
import me.rokevin.android.lib.ropay.alipay.PayResult;
import me.rokevin.android.lib.ropay.alipay.util.AliOrderInfoUtil;
import me.rokevin.android.lib.ropay.alipay_old.AlipayResultUtil;
import me.rokevin.android.lib.ropay.alipay_old.AlipayUtil;
import me.rokevin.android.lib.ropay.util.ChinapayUtil;
import me.rokevin.android.lib.ropay.util.RoPayLog;
import me.rokevin.android.lib.ropay.wxpay.WXPayInfo;
import me.rokevin.android.lib.ropay.wxpay.WXPayReceiver;

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
    private String mMode; // 00:正式  01:测试

    /**
     * 支付方式 3:支付宝, 1:微信支付
     */
    private String mPayment = "";

    public RoPay(Activity activity) {

        mActivity = activity;
        // 初始化手机POS环境
        Utils.setPackageName(mActivity.getPackageName());//MY_PKG是你项目的包名
        initPay();
    }

    public void initPay() {

        // 支付宝Handler回调处理
        mHandler = new Handler() {
            public void handleMessage(Message msg) {

                if (1 == msg.what) {

                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    String memo = payResult.getMemo();

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                    }

                    if (AlipayResultUtil.SUCC.equals(resultStatus)) {

                        // 成功
                        if (null != mOnPayCallback) {
                            mOnPayCallback.onAlipaySucc();
                        }

                    } else {

                        if (null != mOnPayCallback) {
                            mOnPayCallback.onAlipayFail();
                        }

                        Toast.makeText(mActivity, memo, Toast.LENGTH_SHORT).show();
                    }
                }
            }
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
    public String getAliOrder(AliPayInfo aliPayInfo) {

        return AliOrderInfoUtil.buildOrderParam(AliOrderInfoUtil.buildOrderParamMap(aliPayInfo), false);
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
     * @param aliPayInfo 服务器返回的请求字符串的签名字符串
     */
    public void doAlipay(AliPayInfo aliPayInfo) {

        // 调用支付宝支付
        mAlipayUtil.pay(aliPayInfo);
    }

    /**
     * 调起支付宝客户端
     *
     * @param payInfo 服务端拼好的支付信息
     */
    public void doAlipay(String payInfo) {

        // 调用支付宝支付
        mAlipayUtil.pay(payInfo);
    }

    /**
     * 银联支付
     *
     * @param tn 银联给出的交易号
     */
    public void doUnionpay(String tn, String mode) {

        mMode = mode;

        UPPayAssistEx.startPay(mActivity, null, null, tn, mode);
    }

    public void doChinapay(OrderInfo orderInfo, String mode) {

        ChinapayUtil.addPermission(mActivity);

        // 设置Intent指向Initialize.class
        Intent intent = new Intent(mActivity, Initialize.class);
        // this为你当前的activity.this
        // 传入对象参数
        Bundle bundle = new Bundle();
        bundle.putSerializable("orderInfo", orderInfo);
        intent.putExtras(bundle);
        intent.putExtra("mode", mode); // 测试
        // orderInfo为启动插件时传入的OrderInfo对象。
        // 使用intent跳转至移动认证插件
        mActivity.startActivity(intent);
    }

    public void onResume() {

        if (Utils.getResultInfo() != null) {
            ResultInfo resultInfo = Utils.getResultInfo();
            if (resultInfo.getRespCode() != null && !resultInfo.getRespCode().equals("")) {
                if (resultInfo.getRespCode().equals("0000")) {
                    String orderInfo = Utils.getResultInfo().getOrderInfo();
                    if (orderInfo != null) {

                        if (mOnPayCallback != null) {
                            mOnPayCallback.onChinapaySucc();
                        }

                        RoPayLog.e("chinapay", "chinapay:" + "应答码：" + resultInfo.getRespCode() + "\n应答描述:" + resultInfo.getRespDesc() + "\n详细结果：" + orderInfo);
                    }
                } else {

                    if (mOnPayCallback != null) {
                        mOnPayCallback.onChinapayFail("支付失败，错误码：" + resultInfo.getRespCode());
                    }

                    RoPayLog.e("chinapay", "chinapay:" + "应答码：" + resultInfo.getRespCode() + "\n应答描述:" + resultInfo.getRespDesc());
                }
            }
        }
        CPGlobalInfo.init();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = new JSONObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = verifyUnion(dataOrg, sign, mMode);
                    if (ret) {
                        // 验证通过后，显示支付结果
                        msg = "支付成功！";

                        if (mOnPayCallback != null) {
                            mOnPayCallback.onUnionSucc();
                        }
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        msg = "支付失败！";

                        if (mOnPayCallback != null) {
                            mOnPayCallback.onUnionFail();
                        }
                    }
                } catch (JSONException e) {
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                msg = "支付成功！";

                if (mOnPayCallback != null) {
                    mOnPayCallback.onUnionSucc();
                }
            }
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";

            if (mOnPayCallback != null) {
                mOnPayCallback.onUnionFail();
            }
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";

            if (mOnPayCallback != null) {
                mOnPayCallback.onUnionFail();
            }
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private boolean verifyUnion(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;

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

    public OnPayCallback getOnPayListener() {
        return mOnPayCallback;
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

        void onUnionSucc();

        void onUnionFail();

        void onChinapaySucc();

        void onChinapayFail(String message);
    }

    /**
     * 支付确认监听
     */
    public interface OnPayConfirmListener {

        void onConfirm(String realPrice);
    }
}
