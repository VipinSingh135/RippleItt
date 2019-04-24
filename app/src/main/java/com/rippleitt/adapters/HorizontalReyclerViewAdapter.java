package com.rippleitt.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.rippleitt.R;
import com.rippleitt.callback.RemovePicCallback;
import com.rippleitt.controller.RippleittAppInstance;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by pc on password/15/2018.
 */

public class HorizontalReyclerViewAdapter extends RecyclerView
                                                 .Adapter<HorizontalReyclerViewAdapter.GroceryViewHolder>{
    private String[] imagePathList;
    Context context;
    RemovePicCallback callbackInstance;
    int imgPathType=0;   // 1= local. 1= remote
    private int primayDisplaymode=0; // 0 -- addition, show 1st as primary,
                                    // 1-- existing , show tapped as primary
                                    // 1 -- add while edit, show none as primary....
    public HorizontalReyclerViewAdapter(String[] imagePathList_,
                                        Context context,
                                        RemovePicCallback callback,int imgUrlType,int mode){
        this.imagePathList= imagePathList_;
        this.context = context;
        callbackInstance=callback;
        imgPathType=imgUrlType;
        primayDisplaymode=mode;
    }

    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate the layout file
        View groceryProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_gallery_cell, parent, false);
        GroceryViewHolder gvh = new GroceryViewHolder(groceryProductView);
        return gvh;
    }


    @Override
    public void onBindViewHolder(GroceryViewHolder holder, final int position) {
       // holder.imageViewPic.setImageResource(imagePathList.get(position));
        if(this.imgPathType==1){
            Picasso.with(context)
                    .load(new File(imagePathList[position]))
                    .placeholder(R.drawable.rounded_corner_)
                    .error(R.drawable.rounded_corner_)
                    .into(holder.imageViewPic);
        }else{
            Picasso.with(context)
                    .load(RippleittAppInstance.formatPicPath(imagePathList[position]))
                    .placeholder(R.drawable.rounded_corner_)
                    .error(R.drawable.rounded_corner_)
                    .into(holder.imageViewPic);
        }
        if(position==0 && primayDisplaymode==0){
            holder.frmLytPrimaryTag.setVisibility(View.VISIBLE);
        }else{
            holder.frmLytPrimaryTag.setVisibility(View.GONE);
        }
        if(primayDisplaymode==1 && position==RippleittAppInstance.getInstance().getCurrentTappedImageindex()){
            holder.frmLytPrimaryTag.setVisibility(View.VISIBLE);
        }if(primayDisplaymode==2){
            holder.frmLytPrimaryTag.setVisibility(View.GONE);
        }
        holder.ImageViewCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackInstance.removePic(imagePathList[position],imgPathType,position);
               //0 Toast.makeText(context, productName + " is selected", Toast.LENGTH_SHORT).show();
            }
        });
        holder.imageViewPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callbackInstance.onImageCicked(imagePathList[position],
                                                imgPathType,position,primayDisplaymode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePathList.length;
    }

    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPic;
        ImageView ImageViewCross;
        FrameLayout frmLytPrimaryTag;
        public GroceryViewHolder(View view) {
            super(view);
            imageViewPic=view.findViewById(R.id.imgvwPic);
            ImageViewCross=view.findViewById(R.id.imgvwCrossHair);
            frmLytPrimaryTag=view.findViewById(R.id.frmLytPrimaryTag);
        }
    }


}
