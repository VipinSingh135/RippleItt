package com.rippleitt.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.rippleitt.R;
import com.rippleitt.activities.ProductDetailsActivity;

import java.util.ArrayList;

/**
 * Created by hp on 06-03-2018.
 */

public class ProductMultipleImagesAdapter extends  RecyclerView.Adapter<ProductMultipleImagesAdapter.MyViewHolder>{

    ArrayList productImages;
    Context context;
    ArrayList arrayList_new=new ArrayList();
    int i;

    public ProductMultipleImagesAdapter(Context context, ArrayList productImages) {
        this.context=context;
        this.productImages = productImages;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_multi_images_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        i=position;
        arrayList_new=productImages;


        //=======remove image from array lis=======================
        holder.mrelCrossIconProdList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // arrayList_new.remove(i);
                Log.e("REMOVED_ITEM ",position+", NEWPOSITIOn"+i);
            }
        });

        Glide.with(context)
                .load(arrayList_new.get(i))
                .asBitmap()// Uri of the picture
                .into(holder.mimgVwProdListImages);

    }

    @Override
    public int getItemCount() {
        return productImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mimgVwProdListImages;
        RelativeLayout mrelCrossIconProdList;

        public MyViewHolder(View itemView) {
            super(itemView);
            mimgVwProdListImages=(ImageView)itemView.findViewById(R.id.imgVwProdListImages);
            mrelCrossIconProdList=(RelativeLayout) itemView.findViewById(R.id.relCrossIconProdList);
            // get the reference of item view's
        }
    }
}
