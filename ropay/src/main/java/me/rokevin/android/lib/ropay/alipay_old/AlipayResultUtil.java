package me.rokevin.android.lib.ropay.alipay_old;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝返回结果解析类
 */
public class AlipayResultUtil {

    /**
     * 支付宝公钥
     */
    public static final String ALI_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    /**
     * 返回码和错误信息对照表
     */
    private static final Map<String, String> sResultStatus;

    /**
     * 成功返回码
     */
    public static final String SUCC = "9000";

    /**
     * 支付宝返回的结果类
     * resultStatus={9000};memo={};result={_input_charset="utf-8"&body=
     * "应用名称"&notify_url
     * ="http://pay.kongfz.com/pay/alipayMobileNotify.do"&out_trade_no
     * ="201411182154326636"
     * &partner="2088001524509983"&payment_type="1"&seller_id
     * ="sun@kongfz.com"&service
     * ="mobile.securitypay.pay"&subject="店铺订单"&total_fee
     * ="0.02"&success="true"&sign_type="RSA"&sign=
     * "HqLsG3SwQZfQ+LU0SWD04o/N+u0HdieDTZTnYsAA+y0vYOklo0KT/y7wuRYB18JPBLe1dNuWxrg6irXa7GkVaUHe6lC0/WHh3bnWe0Idxg6P7OghUTJPXqVHRA9M8PbZIzMOPvBM19vRVQN9djUZwEwXudFFSaRcSeMptlHk0ak="
     * }
     */
    private String mResult;

    /**
     * 结果码
     */
    private String resultStatus = "99999";

    /**
     * 结果信息
     */
    private String memo = "发生错误";

    /**
     * 返回的处理结果
     */
    private String result = "";

    /**
     * 签名是否正确
     */
    private boolean isSignOk = false;

    public AlipayResultUtil(String result) {
        this.mResult = result;
        parseResult();
    }

    static {
        sResultStatus = new HashMap<String, String>();
        sResultStatus.put(SUCC, "支付宝支付成功");
        sResultStatus.put("4000", "系统异常");
        sResultStatus.put("4001", "数据格式不正确");
        sResultStatus.put("4003", "该用户绑定的支付宝账户被冻结或不允许支付");
        sResultStatus.put("4004", "该用户已解除绑定");
        sResultStatus.put("4005", "绑定失败或没有绑定");
        sResultStatus.put("4006", "订单支付失败");
        sResultStatus.put("4010", "重新绑定账户");
        sResultStatus.put("6000", "支付服务正在进行升级操作");
        sResultStatus.put("6001", "用户中途取消支付操作");
        sResultStatus.put("7001", "网页支付失败");
    }

    public String getResult() {
        // 安装后不操作会返回null
        if (null == mResult) {
            return "";
        }
        String src = mResult.replace("{", "");
        src = src.replace("}", "");
        return getContent(src, "memo=", ";result");
    }

    public void parseResult() {
        if (TextUtils.isEmpty(mResult)) {
            return;
        }

        try {
            String src = mResult.replace("{", "");
            src = src.replace("}", "");

            memo = getContent(src, "memo=", ";result");

            String rs = getContent(src, "resultStatus=", ";memo");

            if (sResultStatus.containsKey(rs)) {
                resultStatus = rs;
                memo = sResultStatus.get(rs);
            } else {
                resultStatus = "99999";
                memo = "发生错误";
            }

            result = getContent(src, "result=", null);

            isSignOk = checkSign(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkSign(String result) {
        boolean retVal = false;
        try {
            JSONObject json = string2JSON(result, "&");

            int pos = result.indexOf("&sign_type=");
            String signContent = result.substring(0, pos);

            String signType = json.getString("sign_type");
            signType = signType.replace("\"", "");

            String sign = json.getString("sign");
            sign = sign.replace("\"", "");

            if (signType.equalsIgnoreCase("RSA")) {
                retVal = RsaUtil.doCheck(signContent, sign, ALI_PUBLIC);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Result", "Exception =" + e);
        }
        Log.i("Result", "checkSign =" + retVal);
        return retVal;
    }

    public JSONObject string2JSON(String src, String split) {
        JSONObject json = new JSONObject();

        try {
            String[] arr = src.split(split);
            for (int i = 0; i < arr.length; i++) {
                String[] arrKey = arr[i].split("=");
                json.put(arrKey[0], arr[i].substring(arrKey[0].length() + 1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    private String getContent(String src, String startTag, String endTag) {
        String content = src;
        int start = src.indexOf(startTag);
        start += startTag.length();

        try {
            if (endTag != null) {
                int end = src.indexOf(endTag);
                content = src.substring(start, end);
            } else {
                content = src.substring(start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isSignOk() {
        return isSignOk;
    }

    public void setSignOk(boolean isSignOk) {
        this.isSignOk = isSignOk;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }
}
