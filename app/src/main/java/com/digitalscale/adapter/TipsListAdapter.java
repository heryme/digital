package com.digitalscale.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.digitalscale.R;
import com.digitalscale.activity.MainActivity;
import com.digitalscale.fragments.TipsDetailsFragment;
import com.digitalscale.fragments.TipsDetailsFragment_;
import com.digitalscale.model.TipsModel;
import com.digitalscale.utility.FontUtility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vishal Gadhiya on 3/14/2017.
 */

public class TipsListAdapter extends RecyclerView.Adapter<TipsListAdapter.TipViewHolder> {

    List<TipsModel> tipsList;
    Context context;

    public static class TipViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTipTitle, tvTipDescription, tvReadMore;
        public ImageView ivTip;
        public LinearLayout ll_tips;
        public TipViewHolder(View itemView) {
            super(itemView);

            ivTip = (ImageView)itemView.findViewById(R.id.ivTipimage);
            tvTipTitle = (TextView) itemView.findViewById(R.id.tvTipTitle);
            tvTipDescription = (TextView) itemView.findViewById(R.id.tvTipDescription);
            tvReadMore = (TextView) itemView.findViewById(R.id.tvReadMore);
            ll_tips = (LinearLayout)itemView.findViewById(R.id.ll_tips);


        }
    }

    public TipsListAdapter(ArrayList<TipsModel> tipsList, Context context) {
        this.tipsList = tipsList;
        this.context = context;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tips_view,parent,false);

        return new TipViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(final TipViewHolder holder, int position) {

        //Set Font Style
        FontUtility.condBold(holder.tvTipTitle,context);
        FontUtility.condLight(holder.tvTipDescription,context);
        FontUtility.condLight(holder.tvReadMore,context);

        final TipsModel tipsDetail = tipsList.get(position);
        holder.tvTipTitle.setText(tipsDetail.getTitle());
        holder.tvTipDescription.setText(tipsDetail.getDescription());
        Glide.with(context).load(tipsDetail.getImage()).into(holder.ivTip);

        holder.ll_tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tag","Tip Id-->" + tipsDetail.getTipId());
                Bundle bundle = new Bundle();
                bundle.putString("TipId",tipsDetail.getTipId());
                TipsDetailsFragment tipsDetailsFragment = new TipsDetailsFragment_();
                tipsDetailsFragment.setArguments(bundle);
                ((MainActivity)context).loadFragment(tipsDetailsFragment,"",false,context.getString(R.string.lbl_detail));
                //((MainActivity)context).loadFragment(new TipsDetailsFragment_(),"");
            }
        });

       /* holder.tvReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Tag","Tip Id-->" + tipsDetail.getTipId());
                Bundle bundle = new Bundle();
                bundle.putString("TipId",tipsDetail.getTipId());
                TipsDetailsFragment tipsDetailsFragment = new TipsDetailsFragment_();
                tipsDetailsFragment.setArguments(bundle);
                ((MainActivity)context).loadFragment(tipsDetailsFragment,"",false,context.getString(R.string.lbl_detail));
                //((MainActivity)context).loadFragment(new TipsDetailsFragment_(),"");
            }
        });*/
    }

    @Override
    public int getItemCount() {
        Log.d("TAG","tipsList.size() >> "+ tipsList.size());
        return tipsList.size();
    }


}
