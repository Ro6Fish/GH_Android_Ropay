package me.rokevin.android.lib.ropay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 微信处理结果广播
 */
public class WXPayReceiver extends BroadcastReceiver
{
	public static final String ACTION_WXPAY = "action.wxpay";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		if (intent.getAction().equals(ACTION_WXPAY))
		{

		}
	}
}
