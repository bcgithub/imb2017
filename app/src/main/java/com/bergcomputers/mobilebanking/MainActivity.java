package com.bergcomputers.mobilebanking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.bergcomputers.mobilebanking.account.AccountListActivity;
import com.bergcomputers.mobilebanking.common.activity.BaseActivity;
import com.bergcomputers.mobilebanking.config.ConfigActivity;
import com.bergcomputers.mobilebanking.currency.CurrencyListActivity;
import com.bergcomputers.mobilebanking.transaction.TransactionListActivity;

public class MainActivity extends BaseActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_accounts:
                    mTextMessage.setText(R.string.title_accounts);
                    Intent configIntentA = new Intent(MainActivity.this, AccountListActivity.class);
                    startActivity(configIntentA);
                    return true;
                case R.id.navigation_payment:
                    mTextMessage.setText(R.string.title_payment);
                    return true;
                case R.id.navigation_pending:
                    mTextMessage.setText(R.string.title_pending);
                    Intent configIntentT = new Intent(MainActivity.this, TransactionListActivity.class);
                    startActivity(configIntentT);
                    return true;
                case R.id.navigation_config:
                    mTextMessage.setText(R.string.title_config);
                    Intent configIntent = new Intent(MainActivity.this, ConfigActivity.class);
                    startActivity(configIntent);

                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


}
