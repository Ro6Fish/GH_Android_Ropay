package me.rokevin.android.lib.ropay.alipay.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import me.rokevin.android.lib.ropay.alipay.AliPayInfo;

/**
 * Created by luokaiwen on 17/1/18.
 */

public class AliOrderInfoUtil {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private static String getTime() {

        format.setLenient(false);
        format.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return format.format(Calendar.getInstance().getTime());
    }

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    public static String buildOrderParam(Map<String, String> map, boolean isEncode) {

        List<String> keys = new ArrayList<String>(map.keySet());

        // key排序
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, isEncode));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, isEncode));

        return sb.toString();
    }

    /**
     * 构造支付订单参数列表
     *
     * @param aliPayInfo
     * @return
     */
    public static Map<String, String> buildOrderParamMap(AliPayInfo aliPayInfo) {

        String appId = aliPayInfo.getAppId();
        String notifyUrl = aliPayInfo.getNotifyUrl();
        String outTradeNo = aliPayInfo.getOutTradNo();
        String totalAmount = aliPayInfo.getTotalAmount();
        String subject = aliPayInfo.getSubject();
        String body = aliPayInfo.getBody();
        boolean rsa2 = aliPayInfo.isRas2();

        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put("app_id", appId);

        keyValues.put("biz_content", "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"" + totalAmount + "\",\"subject\":\"" + subject + "\",\"body\":\"" + body + "\",\"out_trade_no\":\"" + outTradeNo + "\"}");

        keyValues.put("charset", "utf-8");

        keyValues.put("method", "alipay.trade.app.pay");

        keyValues.put("sign_type", rsa2 ? "RSA2" : "RSA");

         keyValues.put("timestamp", getTime());
//        keyValues.put("timestamp", "2017-01-18 15:27:51");

        keyValues.put("version", "1.0");

        keyValues.put("notify_url", notifyUrl);

        return keyValues;
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }
}
