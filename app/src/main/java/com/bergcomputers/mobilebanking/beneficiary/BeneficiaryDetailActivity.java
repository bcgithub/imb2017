package com.bergcomputers.mobilebanking.beneficiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.bergcomputers.mobilebanking.R;
import com.bergcomputers.mobilebanking.common.Util;
import com.bergcomputers.mobilebanking.common.activity.BaseActivity;
import com.bergcomputers.mobilebanking.common.net.IJSONNetworkActivity;
import com.bergcomputers.mobilebanking.common.net.JSONAsyncTask;
import com.bergcomputers.mobilebanking.beneficiary.BeneficiaryDetailFragment;
import com.bergcomputers.mobilebanking.beneficiary.BeneficiaryListActivity;
import com.bergcomputers.mobilebanking.model.Beneficiary;
import com.bergcomputers.mobilebanking.model.Beneficiary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BeneficiaryDetailActivity extends BaseActivity implements IJSONNetworkActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            Long benericiaryId = getIntent().getLongExtra(BeneficiaryDetailFragment.ARG_ITEM_ID, -1L);
            new JSONAsyncTask(Util.BASE_URL+ Util.URL_GET_CURRENCIES+"/"+benericiaryId, this, 0).execute();

        }
    }

    @Override
    protected void init() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, BeneficiaryListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(String pJSONString, int currentAction) {
        if (null != pJSONString) {

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(BeneficiaryDetailFragment.ARG_ITEM_ID, pJSONString);
            BeneficiaryDetailFragment fragment = new BeneficiaryDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.beneficiary_detail_container, fragment)
                    .commit();

        }
    }
}