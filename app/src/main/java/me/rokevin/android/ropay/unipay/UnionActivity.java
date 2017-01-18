package me.rokevin.android.ropay.unipay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.rokevin.android.ropay.R;

public class UnionActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unionpay_base);

        View v1 = findViewById(R.id.payBYapk);
        View v2 = findViewById(R.id.payBYjar);

        v1.setOnClickListener(this);
        v2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        if (v.getId() == R.id.payBYapk) {
            intent.setClass(this, APKActivity.class);
        } else if (v.getId() == R.id.payBYjar) {
            intent.setClass(this, JARActivity.class);
        }

        startActivity(intent);
    }
}
