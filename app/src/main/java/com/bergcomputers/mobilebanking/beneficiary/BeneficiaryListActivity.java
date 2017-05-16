package com.bergcomputers.mobilebanking.beneficiary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bergcomputers.mobilebanking.R;
import com.bergcomputers.mobilebanking.common.Util;
import com.bergcomputers.mobilebanking.common.activity.BaseActivity;
import com.bergcomputers.mobilebanking.common.net.IJSONNetworkActivity;
import com.bergcomputers.mobilebanking.common.net.JSONAsyncTask;
import com.bergcomputers.mobilebanking.beneficiary.BeneficiaryDetailActivity;
import com.bergcomputers.mobilebanking.model.Beneficiary;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Currencies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link BeneficiaryDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class BeneficiaryListActivity extends BaseActivity implements IJSONNetworkActivity{


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beneficiary_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //View recyclerView = findViewById(R.id.beneficiary_list);
        //assert recyclerView != null;

        //load the data from the backend
        new JSONAsyncTask(Util.BASE_URL+ Util.URL_GET_BENEFICIARIES, this, 0).execute();

        //setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.beneficiary_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
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
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void handleResult(String pJSONString, int currentAction) {
        if (null != pJSONString){
            List<Beneficiary> currencies = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(pJSONString);
                for(int i=0; i< jsonArray.length();i++){
                    JSONObject jsonObj = (JSONObject)jsonArray.get(i);
                    Beneficiary beneficiary = new Beneficiary();
                    beneficiary.setId(jsonObj.getLong(Beneficiary.FIELD_ID));
                    beneficiary.setIban(jsonObj.getString(Beneficiary.FIELD_IBAN));
                    beneficiary.setName(jsonObj.getString(Beneficiary.FIELD_NAME));
                    beneficiary.setDetails(jsonObj.getString(Beneficiary.FIELD_DETAILS));
                    beneficiary.setAccountHolder(jsonObj.getString(Beneficiary.FIELD_ACCOUNTHOLDER));
                    currencies.add(beneficiary);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            View recyclerView = findViewById(R.id.beneficiary_list);
            assert recyclerView != null;

            ((RecyclerView)recyclerView).setAdapter(new SimpleItemRecyclerViewAdapter(currencies));
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Beneficiary> mValues;

        public SimpleItemRecyclerViewAdapter(List<Beneficiary> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.beneficiary_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mContentView.setText(mValues.get(position).getIban().toString());
            holder.mContentView.setText(mValues.get(position).getName().toString());
            holder.mContentView.setText(mValues.get(position).getDetails().toString());
            holder.mContentView.setText(mValues.get(position).getAccountHolder().toString());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(BeneficiaryDetailFragment.ARG_ITEM_ID, holder.mItem.getId().toString());
                        BeneficiaryDetailFragment fragment = new BeneficiaryDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.beneficiary_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, BeneficiaryDetailActivity.class);
                        intent.putExtra(BeneficiaryDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Beneficiary mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}