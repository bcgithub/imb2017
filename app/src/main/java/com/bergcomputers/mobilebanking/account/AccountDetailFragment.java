package com.bergcomputers.mobilebanking.account;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bergcomputers.mobilebanking.R;
import com.bergcomputers.mobilebanking.common.Util;
import com.bergcomputers.mobilebanking.common.net.IJSONNetworkActivity;
import com.bergcomputers.mobilebanking.common.net.JSONAsyncTask;
import com.bergcomputers.mobilebanking.model.Account;
import com.bergcomputers.mobilebanking.model.Currency;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a single Account detail screen.
 * This fragment is either contained in a {@link AccountListActivity}
 * in two-pane mode (on tablets) or a {@link AccountDetailActivity}
 * on handsets.
 */
public class AccountDetailFragment extends Fragment implements IJSONNetworkActivity{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Account mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AccountDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            String pJSONString = getArguments().getString(ARG_ITEM_ID);
            new JSONAsyncTask(Util.BASE_URL + Util.URL_GET_ACCOUNTS + pJSONString , this, 0).execute();

            System.out.println(pJSONString);
            mItem = new Account();
            try {
                JSONObject jsonObj = new JSONObject(pJSONString);
                mItem.setId(jsonObj.getLong(Account.FIELD_ID));
                mItem.setIban(jsonObj.getString(Account.FIELD_IBAN));
                mItem.setAmount(jsonObj.getDouble(Account.FIELD_AMOUNT));
                mItem.setSymbol(jsonObj.getString(Account.FIELD_SYMBOL));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getIban());
            }
        }
    }
    @Override
    public void handleResult(String pJSONString, int currentAction)
    {
        if(pJSONString != null)
        {
            try
            {
                JSONObject jsonObj = new JSONObject(pJSONString);
                Account account = new Account();
                account.setId(jsonObj.getLong((Account.FIELD_ID)));
                account.setIban(jsonObj.getString(Account.FIELD_IBAN));
                account.setAmount(jsonObj.getDouble(Account.FIELD_AMOUNT));
                JSONObject currency = jsonObj.getJSONObject("currency");
                account.setSymbol(currency.getString((Account.FIELD_SYMBOL)));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.account_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.account_detail)).setText(mItem.getAmount().toString());
        }

        return rootView;
    }
}
