package com.digitalscale.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.digitalscale.R;
import com.digitalscale.services.TipsService;
import com.digitalscale.vollyrest.APIService;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Rahul Padaliya on 3/31/2017.
 */
@EFragment(R.layout.fragment_tips_details)
public class TipsDetailsFragment extends MasterFragment {

    private static final String TAG = TipsDetailsFragment.class.getSimpleName();

    @ViewById(R.id.webview_frg_tips_details)
    WebView webview_frg_tips_details;

    @ViewById(R.id.iv_frg_tips_details)
    ImageView iv_frg_tips_details;

    private Bundle bundle;

    private ProgressDialog progressBar;

    public static Fragment newInstance() {
        return new TipsDetailsFragment_();
    }

    @AfterViews
    public void init() {
        bundle = this.getArguments();

        setHasOptionsMenu(true);
        //Call API Of The Tips Details
        callTipsDetailsAPI();
    }

    /**
     * Call TipsDetails API
     */
    private void callTipsDetailsAPI() {
        HashMap<String, String> param = new HashMap<>();
        param.put("tip_id", bundle.getString("TipId"));
        TipsService.getTipsDetail(getContext(), param, new APIService.Success<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String status;
                    String message;
                    if (response != null && response.length() > 0) {
                        status = response.optString("status");
                        message = response.optString("message");
                        String data = response.optString("data");

                        if (data != null && data.length() > 0) {
                            if (status.equalsIgnoreCase("1")) {
                                JSONObject jo = response.getJSONObject("data");
                                String image = jo.optString("image");
                                String content = jo.optString("content");

                                Glide.with(getActivity()).load(image)
                                        .placeholder(R.drawable.ic_placeholder)
                                        .into(iv_frg_tips_details);
                                loadWebContent(content);
                            } else {
                                toastMessage(message);
                            }
                        } else {
                            debugLog(TAG,"Get tips detail response \"data\" not available");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    /**
     * Load Web Content
     */
    private void loadWebContent(String content) {
        WebSettings settings = webview_frg_tips_details.getSettings();
        settings.setJavaScriptEnabled(true);
        webview_frg_tips_details.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        progressBar = ProgressDialog.show(getActivity(),"", "Loading...");
        webview_frg_tips_details.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                debugLog(TAG,"Processing webview url click...");
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                debugLog(TAG,"Finished loading URL: " +url);
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                debugLog(TAG, "Error: " + description);
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        webview_frg_tips_details.loadData(content,"text/html", null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
