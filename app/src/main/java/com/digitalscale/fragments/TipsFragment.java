package com.digitalscale.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.digitalscale.R;
import com.digitalscale.adapter.TipsListAdapter;
import com.digitalscale.parser.TipsParser;
import com.digitalscale.services.TipsService;
import com.digitalscale.tools.ListItemDivider;
import com.digitalscale.utility.NetworkUtility;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Vishal Gadhiya on 2/22/2017.
 */


@EFragment(R.layout.tab_fragment_tips)
public class TipsFragment extends MasterFragment {

    @ViewById(R.id.rv_tips_list)
    RecyclerView rv_tips_list;

    TipsListAdapter tipsListAdapter;

    public static Fragment newInstance() {
        return new TipsFragment_();
    }

    @AfterViews
    public void init() {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rv_tips_list.setLayoutManager(mLayoutManager);
        rv_tips_list.setItemAnimator(new DefaultItemAnimator());
        rv_tips_list.addItemDecoration(new ListItemDivider(getContext()));

        //TODO: Call get tips api
        if (NetworkUtility.checkIsInternetConnectionAvailable(getActivity())){
            //Call Get Tips API
            getTips();
        }
    }

    /**
     * Call Get Tips API
     */
    private void getTips() {
        debugLog("Call tips api");

        HashMap<String, String> param = new HashMap<>();
        param.put("page", "1");
        param.put("limit", "30");
        TipsService.getTips(getContext(), param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                TipsParser.Tips tips = TipsParser.Tips.parseGetTipsResponse(response);

                if (tips.getStatus().equalsIgnoreCase("1")) {
                    tipsListAdapter = new TipsListAdapter(tips.getTipList(), getContext());
                    rv_tips_list.setAdapter(tipsListAdapter);
                } else {
                    toastMessage(tips.getMessage());
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        debugLog("Tips act : "+ requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != -1 && (requestCode & 0xffff0000) != 0) {
            throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }

    }
}
