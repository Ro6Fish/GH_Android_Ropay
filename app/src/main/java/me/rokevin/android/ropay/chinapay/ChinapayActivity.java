package me.rokevin.android.ropay.chinapay;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chinapay.cppaysdk.activity.Initialize;
import com.chinapay.cppaysdk.bean.OrderInfo;
import com.chinapay.cppaysdk.global.CPGlobalInfo;
import com.chinapay.cppaysdk.global.ResultInfo;
import com.chinapay.cppaysdk.util.LogUtils;
import com.chinapay.cppaysdk.util.Utils;

import me.rokevin.android.lib.ropay.RoPay;
import me.rokevin.android.ropay.BuildConfig;
import me.rokevin.android.ropay.R;

public class ChinapayActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinapay);

        // 初始化手机POS环境
        Utils.setPackageName(BuildConfig.APPLICATION_ID);//MY_PKG是你项目的包名

        /**
         * 版本号 Version  固定值：20140728
         * <p>
         * 商户号	MerId	Y	N15	由ChinaPay分配的15位定长数字，用于确认商户身份
         * <p>
         * 商户订单号	MerOrderNo	Y	AN1..32	必填，变长 32位，同一商户同一交易日期内不可重复
         * <p>
         * 商户交易日期	TranDate	Y	N8	商户提交交易的日期，例如交易日期为2015年1月2日，则值为20150102
         * <p>
         * 商户交易时间	TranTime	Y	N6	商户提交交易的时间，例如交易时间10点11分22秒，则值为101122
         * <p>
         * 订单金额	OrderAmt	Y	N1..20	单位：分
         * <p>
         * 业务类型	BusiType	Y	N4	固定值：0001
         * <p>
         * 商户后台通知地址	MerBgUrl	Y	ANS1..256	商户后台接收交易结果的地址
         * <p>
         * 防钓鱼客户浏览器IP	RemoteAddr	Y	ANS1..128	客户浏览器端IP，如商户开通校验IP防钓鱼验证，可填写此域做防钓鱼使用。ChinaPay会获取持卡人访问IP和该字段进行比较，如果不一致，则会进行防钓鱼提示或拦截交易
         * <p>
         * 签名	Signature	Y		商户报文签名信息，报文中的所有字段都参与签名（Signature除外）
         */

        final OrderInfo orderInfo = new OrderInfo();

        orderInfo.setVersion("20140728");
        orderInfo.setBusiType("0001");
        orderInfo.setMerId("000001702201180");
        orderInfo.setOrderAmt("000000000001");
        orderInfo.setMerBgUrl("http://hmtest.hlvan.cn/chinapayCallBackController/callback");
        orderInfo.setRemoteAddr("114.244.89.50");
        orderInfo.setTranType("0005");
        orderInfo.setRiskData("XhBDOw86HCcpA2tu8QXc4fmZlt9N+BR2N9DkZ2XehZIC+jgQZ72klVwjDRe9MYMxah/0rmzoQs9tWS3TEhSaDvses3+R1LRpCYN6RSkzq/BZ1fAXRBNuNHNDpnTffu3fmQkJNERwMeHa5cTdc7uJSO8nk7LV5hjb0GQOTuuGkuc=");
        orderInfo.setTranDate("20170228");
        orderInfo.setTranTime("182428");
        orderInfo.setMerOrderNo("170228182004557003");
        orderInfo.setSignature("qevYydnv8UWWHZ3+VWDy2WVPgQxBtq5xAMw/6DD2PYiyqv3c3nchISi40W55/8RtyJ8WAxFlBOGWtlmRlK7vPVvncSiDrvUgMUsd59bhhH20DLHxI+zE0VoeeRYVTDEnfACdyKa7sciz+ihjDU+IfoVipjhWF8yydZrVXMTxOuQ=");


        orderInfo.setTranReserved("");

// orderInfo.setCurryNo("CNY");
//        orderInfo.setMerResv("京东商城");
//        orderInfo.setCommodityMsg("京东商城购买iphone6s");

//        orderInfo.setVersion("20140728");
//        orderInfo.setBusiType("0001");
//        orderInfo.setMerId("000000000000001");
//        orderInfo.setMerOrderNo("20170223160715376275");
//        orderInfo.setOrderAmt("000000000001");
//        orderInfo.setTranDate("20170223");
//        orderInfo.setTranTime("160715");
//        orderInfo.setMerBgUrl("http://131.252.85.244:9080/CPPayWeb/trans!returnCommercial.ac");
//        orderInfo.setRemoteAddr("192.168.1.136");
//        orderInfo.setSignature("xDIoUhvocKL1C/0s+U7NronL0ndqPlYED5v4JomkcKmPve5ssL2mLkCRbNpW2zbISlrbLn868HP1bbubZ2A2rLFbBmwJM5UxUHPX2TWeQW8WessLc2wDhhmcbCd8fvmXsWxcYqxcqjP0FIVI0Z6H07soW46hvKHN8uj3dKB43Xk=");
//        orderInfo.setTranType("0005");
//        orderInfo.setCurryNo("CNY");
//        orderInfo.setMerResv("京东商城");
//        orderInfo.setCommodityMsg("京东商城购买iphone6s");

//        JSONObject json = new JSONObject();
//        try {
//            json.put("PayMode", "MUP");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        String riskData = json.toString();
//        Log.e("TAG", "Base64编码前riskData=[" + riskData + "]");
//        try {
//            CPGlobalInfo.riskData = Base64.encodeToString(riskData.toString().getBytes("utf-8"),
//                    Base64.DEFAULT).replaceAll("\\n", "");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        Log.e("TAG", "Base64编码后riskData=[" + CPGlobalInfo.riskData + "]");
//        orderInfo.setRiskData(CPGlobalInfo.riskData);
        // orderInfo.setRiskData("XhBDOw86HCcpA2tu8QXc4fmZlt9N+BR2N9DkZ2XehZIC+jgQZ72klVwjDRe9MYMxah/0rmzoQs9tWS3TEhSaDvses3+R1LRpCYN6RSkzq/BZ1fAXRBNuNHNDpnTffu3fmQkJNERwMeHa5cTdc7uJSO8nk7LV5hjb0GQOTuuGkuc=");

        findViewById(R.id.btn_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addPermission();

                // 设置Intent指向Initialize.class
                Intent intent = new Intent(ChinapayActivity.this, Initialize.class);
                // this为你当前的activity.this
                // 传入对象参数
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderInfo", orderInfo);
                intent.putExtras(bundle);
                intent.putExtra("mode", "01"); // 测试
                // orderInfo为启动插件时传入的OrderInfo对象。
                // 使用intent跳转至移动认证插件
                startActivity(intent);
            }
        });
    }

    /**
     * onResume方法.
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.getResultInfo() != null) {
            ResultInfo resultInfo = Utils.getResultInfo();
            if (resultInfo.getRespCode() != null && !resultInfo.getRespCode().equals("")) {
                if (resultInfo.getRespCode().equals("0000")) {
                    String orderInfo = Utils.getResultInfo().getOrderInfo();
                    if (orderInfo != null) {
                        Log.e("chinapay", "chinapay:" + "应答码：" + resultInfo.getRespCode() + "\n应答描述:" + resultInfo.getRespDesc() + "\n详细结果：" + orderInfo);
                    }
                } else {
                    Log.e("chinapay", "chinapay:" + "应答码：" + resultInfo.getRespCode() + "\n应答描述:" + resultInfo.getRespDesc());
                }
            }
        }
        CPGlobalInfo.init();
    }

    public void log(OrderInfo orderInfo) {

        String log = "MyOrderInfo{" +
                "AccessType='" + orderInfo.getAccessType() + '\'' +
                ", Version='" + orderInfo.getVersion() + '\'' +
                ", InstuId='" + orderInfo.getInstuId() + '\'' +
                ", AcqCode='" + orderInfo.getAcqCode() + '\'' +
                ", MerId='" + orderInfo.getMerId() + '\'' +
                ", MerOrderNo='" + orderInfo.getMerOrderNo() + '\'' +
                ", TranDate='" + orderInfo.getTranDate() + '\'' +
                ", TranTime='" + orderInfo.getTranTime() + '\'' +
                ", OrderAmt='" + orderInfo.getOrderAmt() + '\'' +
                ", TranType='" + orderInfo.getTranType() + '\'' +
                ", BusiType='" + orderInfo.getBusiType() + '\'' +
                ", CurryNo='" + orderInfo.getCurryNo() + '\'' +
                ", SplitType='" + orderInfo.getSplitType() + '\'' +
                ", SplitMethod='" + orderInfo.getSplitMethod() + '\'' +
                ", MerSplitMsg='" + orderInfo.getMerSplitMsg() + '\'' +
                ", BankInstNo='" + orderInfo.getBankInstNo() + '\'' +
                ", MerPageUrl='" + orderInfo.getMerPageUrl() + '\'' +
                ", MerBgUrl='" + orderInfo.getMerBgUrl() + '\'' +
                ", CommodityMsg='" + orderInfo.getCommodityMsg() + '\'' +
                ", MerResv='" + orderInfo.getMerResv() + '\'' +
                ", TranReserved='" + orderInfo.getTranReserved() + '\'' +
                ", CardTranData='" + orderInfo.getCardTranData() + '\'' +
                ", PayTimeOut='" + orderInfo.getPayTimeOut() + '\'' +
                ", TimeStamp='" + orderInfo.getTimeStamp() + '\'' +
                ", RemoteAddr='" + orderInfo.getRemoteAddr() + '\'' +
                ", RiskData='" + orderInfo.getRiskData() + '\'' +
                ", Signature='" + orderInfo.getSignature() + '\'' +
                '}';

        Log.e("OrderInfo", "orderInfo:" + log);
    }

    /**
     * addPermission方法.
     */
    private void addPermission() {

        int PERMISSION_REQUEST_READ_PHONE_STATE = 0;

        if (isMarshmallow()) {
//            LogUtils.i("动态添加权限");
//            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE},
//                        PERMISSION_REQUEST_READ_PHONE_STATE);
//            }
        }
    }

    /**
     * isMarshmallow方法.
     *
     * @return 是否是安卓23以上
     */
    @TargetApi(23)
    private static boolean isMarshmallow() {
        return Build.VERSION.SDK_INT >= 23;
    }
}
