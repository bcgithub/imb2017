package com.bergcomputers.mobilebanking.currency;

import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.bergcomputers.mobilebanking.R;

import com.bergcomputers.mobilebanking.common.Util;
import com.bergcomputers.mobilebanking.common.activity.BaseActivity;
import com.bergcomputers.mobilebanking.common.net.*;
import com.bergcomputers.mobilebanking.model.Currency;
import org.json.*;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Currencies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CurrencyDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CurrencyListActivity extends BaseActivity implements IJSONNetworkActivity{


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_list);

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

        //View recyclerView = findViewById(R.id.currency_list);
        //assert recyclerView != null;

        //load the data from the backend
        new JSONAsyncTask(Util.BASE_URL+ Util.URL_GET_CURRENCIES, this, 0).execute();

        //setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.currency_detail_container) != null) {
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
            List<Currency> currencies = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(pJSONString);
                for(int i=0; i< jsonArray.length();i++){
                    JSONObject jsonObj = (JSONObject)jsonArray.get(i);
                    Currency currency = new Currency();
                    currency.setId(jsonObj.getLong(Currency.FIELD_ID));
                    currency.setSymbol(jsonObj.getString(Currency.FIELD_SYMBOL));
                    currency.setExchangerate(jsonObj.getDouble(Currency.FIELD_EXCHANGE_RATE));
                    currencies.add(currency);

                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            View recyclerView = findViewById(R.id.currency_list);
            assert recyclerView != null;

            ((RecyclerView)recyclerView).setAdapter(new SimpleItemRecyclerViewAdapter(currencies));
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Currency> mValues;

        public SimpleItemRecyclerViewAdapter(List<Currency> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.currency_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).getSymbol());
            holder.mContentView.setText(mValues.get(position).getExchangerate().toString());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CurrencyDetailFragment.ARG_ITEM_ID, holder.mItem.getId().toString());
                        CurrencyDetailFragment fragment = new CurrencyDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.currency_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CurrencyDetailActivity.class);
                        intent.putExtra(CurrencyDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

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
            public Currency mItem;

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
