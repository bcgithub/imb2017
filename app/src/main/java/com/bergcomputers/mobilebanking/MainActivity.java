package com.bergcomputers.mobilebanking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.bergcomputers.mobilebanking.currency.CurrencyListActivity;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_accounts:
                    mTextMessage.setText(R.string.title_accounts);
                    return true;
                case R.id.navigation_payment:
                    mTextMessage.setText(R.string.title_payment);
                    return true;
                case R.id.navigation_pending:
                    mTextMessage.setText(R.string.title_pending);
                    return true;
                case R.id.navigation_currencies:
                    mTextMessage.setText(R.string.title_currencies);
                    Intent intent = new Intent(MainActivity.this, CurrencyListActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_samples:
                    mTextMessage.setText(R.string.title_sample);
                    startActivity(new Intent(MainActivity.this, CurrencyListActivity.class));
                    return true;
                case R.id.navigation_config:
                    mTextMessage.setText(R.string.title_config);
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
