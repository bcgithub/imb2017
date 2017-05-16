package com.bergcomputers.mobilebanking.transaction;

/**
 * Created by berg on 5/15/2017.
 */

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bergcomputers.mobilebanking.R;
import com.bergcomputers.mobilebanking.transaction.TransactionDetailActivity;
import com.bergcomputers.mobilebanking.transaction.TransactionListActivity;
import com.bergcomputers.mobilebanking.model.Transaction;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a single Transaction detail screen.
 * This fragment is either contained in a {@link TransactionListActivity}
 * in two-pane mode (on tablets) or a {@link TransactionDetailActivity}
 * on handsets.
 */
public class TransactionDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Transaction mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransactionDetailFragment() {
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
            System.out.println(pJSONString);
            mItem = new Transaction();
            try {
                JSONObject jsonObj = new JSONObject(pJSONString);
                mItem.setId(jsonObj.getLong(Transaction.FIELD_ID));
                //mItem.setDate(jsonObj.get(Transaction.FIELD_DATE));
                mItem.setDetails(jsonObj.getString(Transaction.FIELD_DETAILS));
                mItem.setAmount(jsonObj.getDouble(Transaction.FIELD_AMOUNT));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getDetails());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transaction_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.transaction_detail)).setText(mItem.getDetails());
        }

        return rootView;
    }
}
