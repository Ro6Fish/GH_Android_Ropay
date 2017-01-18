package me.rokevin.android.ropay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import me.rokevin.android.lib.ropay.RoPay;
import me.rokevin.android.lib.ropay.alipay.AliPayInfo;
import me.rokevin.android.ropay.alipay.AlipayActivity;
import me.rokevin.android.ropay.unipay.UnionActivity;
import me.rokevin.android.ropay.wxpay.WXActivity;

public class MainActivity extends Activity {

    private Context mContext;

    /**
     * 支付帮助类
     */
    private RoPay mRoPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        mRoPay = new RoPay(this);
        mRoPay.setOnPayListener(new RoPay.OnPayCallback() {

            @Override
            public void onAlipaySucc() {

                Toast.makeText(mContext, "支付宝支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAlipayFail() {

                Toast.makeText(mContext, "支付宝支付失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWxpaySucc() {

            }

            @Override
            public void onWxpayFail() {

            }
        });

        mRoPay.setOnPayConfirmListener(new RoPay.OnPayConfirmListener() {
            @Override
            public void onConfirm(String realPrice) {

                switch (mRoPay.getPayWay()) {

                    case "1":

                        /**
                         * 商品名：叮当到-160713XXX师傅
                         * 交易描述：由XXX（发货地）发往XXX（收货地）的XXX（车型）用车需求订单
                         */

                        AliPayInfo aliPayInfo = new AliPayInfo();

                        aliPayInfo.setAppId(PayConfig.getAliAppId());
                        aliPayInfo.setNotifyUrl(PayConfig.getPayUrl());
                        aliPayInfo.setOutTradNo("170113181304579004");
                        aliPayInfo.setTotalAmount("0.01");
                        aliPayInfo.setSubject("干线物流");
                        aliPayInfo.setBody("物流支付");
                        aliPayInfo.setRas2(true);

                        String aliOrder = mRoPay.getAliOrder(aliPayInfo);

                        Log.e("TAG", "aliOrder:" + aliOrder);

                        // 获取Alipay支付的订单字符串

                        String sign = "hnD5hqSh8LBIgvE+gL1Ysujbfa5E0EVCIgWlxTH4ifb8iwnsNAblcxPYLFsNDsyLg6gCvzuGsz6E5wb+j8aTsxK+yZXAWhTxT3kuS3d5TpUVEFrA7AkKb5SbsaJdyO7Se9X5cCKXzjquFDCNJtSKJWsMW0w5QDm/xQhbwLEMQqE1Y6kEl2didYaVxdnmcg3IaRejx7QDXbJDC8QVTTuvj9O4SnmOako85VGYXolV3yDud7DZhWmVzzTDUAKgTUjb59+8nn6urst66zkbszRZ1Z7cYi/R1v5bObnX7mRj3pDkLOkRjQhYTRToC9T6yHEnfYWImW97ajTQgKi0wCHpjg==";

                        aliPayInfo.setSign(sign);

                        // 获取支付信息的签名字符串
                        mRoPay.doAlipay(aliPayInfo);

                        break;
                }
            }
        });


        findViewById(R.id.btn_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doAlipay();
                Toast.makeText(MainActivity.this, "支付宝支付", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_wxpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doWxpay();
                Toast.makeText(MainActivity.this, "微信支付", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_unionpay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doUnionpay();
                Toast.makeText(MainActivity.this, "银联支付", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doAlipay() {

        mRoPay.setPayWay("1");

        // 去第三方支付
        mRoPay.doConfirmPay("0.01");

        // startActivity(new Intent(this, AlipayActivity.class));
    }

    public void doWxpay() {

        startActivity(new Intent(this, WXActivity.class));
    }

    public void doUnionpay() {

        startActivity(new Intent(this, UnionActivity.class));
    }
}
