package com.bergcomputers.mobilebanking.beneficiary;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bergcomputers.mobilebanking.R;
import com.bergcomputers.mobilebanking.beneficiary.BeneficiaryDetailActivity;
import com.bergcomputers.mobilebanking.model.Beneficiary;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A fragment representing a single Beneficiary detail screen.
 * This fragment is either contained in a {@link BeneficiaryListActivity}
 * in two-pane mode (on tablets) or a {@link BeneficiaryDetailActivity}
 * on handsets.
 */
public class BeneficiaryDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Beneficiary mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BeneficiaryDetailFragment() {
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
            mItem = new Beneficiary();
            try {
                JSONObject jsonObj = new JSONObject(pJSONString);
                mItem.setId(jsonObj.getLong(Beneficiary.FIELD_ID));
                mItem.setIban(jsonObj.getString(Beneficiary.FIELD_IBAN));
                mItem.setName(jsonObj.getString(Beneficiary.FIELD_NAME));
                mItem.setDetails(jsonObj.getString(Beneficiary.FIELD_DETAILS));
                mItem.setAccountHolder(jsonObj.getString(Beneficiary.FIELD_ACCOUNTHOLDER));
                
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.beneficiary_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.beneficiary_detail)).setText(mItem.getIban().toString());
            ((TextView) rootView.findViewById(R.id.beneficiary_detail)).setText(mItem.getName().toString());
            ((TextView) rootView.findViewById(R.id.beneficiary_detail)).setText(mItem.getDetails().toString());
            ((TextView) rootView.findViewById(R.id.beneficiary_detail)).setText(mItem.getAccountHolder().toString());
        }

        return rootView;
    }
}
