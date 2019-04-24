package com.rippleitt.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rippleitt.R;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.ListingTemplate;

/**
 * Created by pc on mail/user/2018.
 */

public class MyOrdersAdapter extends BaseAdapter {

    private Context parentRef;
    private ListingTemplate[] data;
    private LayoutInflater mInflater;

    public MyOrdersAdapter(Context context, ListingTemplate[] listngs) {
        parentRef = context;
        data = listngs;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.length;
    }


    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = ((Activity) parentRef).getLayoutInflater();
            view = inflater.inflate(R.layout.layout_my_orders, viewGroup,
                    false);
            holder.mImgVwProductThumb = (ImageView) view.findViewById(R.id.imgVwProductThumb);
            holder.mTxtVwProductTitle = (TextView) view.findViewById(R.id.txtVwProductNam);
            holder.mTxtVwProductAskPrice = (TextView) view.findViewById(R.id.txtvwItemAskPrice);
            holder.mTxtVwProductDiscPrice = (TextView) view.findViewById(R.id.txtvwItemDiscPrice);
            holder.mFrmLytBadge = (FrameLayout) view.findViewById(R.id.frmlytOrderFlagColor);
            holder.mTxtVwOrderNumber = (TextView) view.findViewById(R.id.txtvwrderNumber);
            holder.mTxtvwBadgeStatus = (TextView) view.findViewById(R.id.txtvwLabelOrderStatus);
            holder.mTxtVwProductAcceptedPrice = (TextView) view.findViewById(R.id.txtVwUserBid);
            //txtvwrderStatus
            holder.mTxtVwOrderStatus = (TextView) view.findViewById(R.id.txtvwrderStatus);


            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (ViewHolder) view.getTag();
        }


        //====================setImgae=================

        if (data[i].getListing_photos().length != 0) {

            Glide.with(parentRef)
                    .load(RippleittAppInstance.formatPicPath(
                            data[i].getListing_photos()[0].getPhoto_path()
                    ))
                    .override(80, 80)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mImgVwProductThumb);
        } else {

        }

        holder.mTxtVwProductAskPrice.setText("Asking Price: $" + data[i].getListing_price());
        holder.mTxtVwProductTitle.setText(data[i].getProductname());

        holder.mTxtVwProductAcceptedPrice.setText("Accepted Price: $" + data[i].getOrder_value()
                + " (" + data[i].getQuantity() + ""
                + " Qty)");

//        if (data[i].getDirect_buy().equals("1")) {

//            holder.mTxtVwProductAcceptedPrice.setText("Accepted Price: $" + calculatePrice(data[i].getQuantity(),data[i].getListing_price())
//                    + " (" + data[i].getQuantity() + ""
//                    + " Qty)");

//        } else if (data[i].getDirect_buy().equals("0")) {
//
//            holder.mTxtVwProductAcceptedPrice.setText("Accepted Price: $" + calculatePrice(data[i].getQuantity(), data[i].getBid_price())
//                    + " (" + data[i].getQuantity() + ""
//                    + " Qty)");
//
//        }

//        if (data[i].getHas_voucher() != null && data[i].getVoucher_details() != null && data[i].getHas_voucher().equals("1")) {

//            holder.mTxtVwProductDiscPrice.setText("Discounted Price: $" + calculateVoucherFee(data[i].getVoucher_details().getAmount(),
//                    data[i].getVoucher_details().getType(), data[i].getQuantity(), data[i].getListing_price()));

//            if (data[i].getIs_buying().equalsIgnoreCase("1")) {

//                holder.mTxtVwProductDiscPrice.setText("Discounted Price: $" + calculateDiscountedPrice(data[i].getVoucher_value(), data[i].getOrder_value()));

//            } else {
//                holder.mTxtVwProductDiscPrice.setText("Discounted Price: $" + data[i].getOrder_net());
//            }

//        } else {
//            holder.mTxtVwProductDiscPrice.setVisibility(View.GONE);
//        }

        holder.mTxtVwOrderNumber.setText(data[i].getOrder_id());

        if (data[i].getIs_buying().equalsIgnoreCase("1")) {
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#65b488"));
            holder.mTxtvwBadgeStatus.setText(" Buying ");
            holder.mTxtVwProductDiscPrice.setText("Discounted Price: $" + calculateDiscountedPrice(data[i].getVoucher_value(), data[i].getOrder_value()));
            if (data[i].getHas_voucher() != null && data[i].getVoucher_details() != null && data[i].getHas_voucher().equals("1")) {
                holder.mTxtVwProductDiscPrice.setVisibility(View.VISIBLE);
            }else
                holder.mTxtVwProductDiscPrice.setVisibility(View.GONE);

        } else {
            holder.mFrmLytBadge.setBackgroundColor(Color.parseColor("#555555"));
            holder.mTxtvwBadgeStatus.setText(" Selling");
            holder.mTxtVwProductDiscPrice.setText("Amount Received: $" + data[i].getOrder_net());
//            if (data[i].getHas_voucher() != null && data[i].getVoucher_details() != null && data[i].getHas_voucher().equals("1")) {
//                holder.mTxtVwProductDiscPrice.setVisibility(View.VISIBLE);
//            }else

            holder.mTxtVwProductDiscPrice.setVisibility(View.VISIBLE);
        }
        //order status
        if (data[i].getIs_paid().equalsIgnoreCase("1")) {
            if (data[i].getDirect_buy().equalsIgnoreCase("1")
                    && data[i].getPayment_mode().equalsIgnoreCase("1")) {
                holder.mTxtVwOrderStatus.setText("Processing");
            } else {
                holder.mTxtVwOrderStatus.setText("Funds Secured");
            }
        } else if (data[i].getIs_paid().equalsIgnoreCase("2")) {
            holder.mTxtVwOrderStatus.setText("Completed");
        } else if (data[i].getIs_paid().equalsIgnoreCase("3")) {
            holder.mTxtVwOrderStatus.setText("Cancelled by Admin");
        } else {
            holder.mTxtVwOrderStatus.setText("Processing");
        }

        return view;
    }


    private double calculateVoucherFee(String voucherAmount, String voucherType, String qty, String listingPrice) {
//        if (mEdtxtAddQty.getText().toString().trim()==null)
//        double quantity= Float.parseFloat(qty);
        double voucherPrice = Float.parseFloat(voucherAmount);
        double totalPrice = Float.parseFloat(listingPrice);
        double quantity = Float.parseFloat(qty);
        double total = 0;

        if (voucherType.equals("1")) {
            total = (totalPrice * quantity) - voucherPrice;
        } else if (voucherType.equals("2")) {
            double percent = (totalPrice * voucherPrice) / 100;
            total = (totalPrice - percent) * quantity;
        }
//        return total*quantity;
        return total;
    }

    private double calculateDiscountedPrice(String voucherAmount, String listingPrice) {
//        if (mEdtxtAddQty.getText().toString().trim()==null)
//        double quantity= Float.parseFloat(qty);
        double voucherPrice = Float.parseFloat(voucherAmount);
        double totalPrice = Float.parseFloat(listingPrice);
        double total = 0;

        total = totalPrice - voucherPrice;

//        return total*quantity;
        return total;
    }

    private double calculatePrice(String qty, String listingPrice) {
//        if (mEdtxtAddQty.getText().toString().trim()==null)
        double quantity = Float.parseFloat(qty);
        double totalPrice = Float.parseFloat(listingPrice);

        return totalPrice * quantity;
    }

    static class ViewHolder {
        ImageView mImgVwProductThumb;
        TextView mTxtVwProductTitle;
        TextView mTxtVwProductAskPrice;
        TextView mTxtVwProductDiscPrice;
        TextView mTxtVwProductAcceptedPrice;
        TextView mTxtVwOrderNumber;
        FrameLayout mFrmLytBadge;
        TextView mTxtvwBadgeStatus;
        TextView mTxtVwOrderStatus;
    }

}
