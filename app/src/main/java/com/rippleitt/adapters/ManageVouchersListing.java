package com.rippleitt.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rippleitt.R;
import com.rippleitt.activities.ActivityAddVoucher;
import com.rippleitt.activities.ActivitySelectListing;
import com.rippleitt.controller.RippleittAppInstance;
import com.rippleitt.modals.VoucherTemplate;
import com.rippleitt.utils.CommonUtils;
import com.rippleitt.webservices.DeleteVoucherApi;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

public class ManageVouchersListing extends BaseAdapter {

    Context context;
    List<VoucherTemplate> voucherListing;
    AVLoadingIndicatorView mLoader;

    public ManageVouchersListing(Context context, List<VoucherTemplate> voucherListingArr, AVLoadingIndicatorView loader) {
        this.context = context;
        this.voucherListing = voucherListingArr;
        this.mLoader = loader;
    }

    @Override
    public int getCount() {
        return voucherListing.size();
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ManageVouchersListing.ViewHolder holder;
        //   holder= new EditProductBaseAdapter.ViewHolder();
        if (view == null) {
            holder = new ManageVouchersListing.ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(R.layout.adapter_my_voucher, viewGroup, false);
            holder.txtVwName = (TextView) view.findViewById(R.id.txtVwName);
            holder.tvVoucherAmount = (TextView) view.findViewById(R.id.tvVoucherAmount);
            holder.txtVwExpiryDate = (TextView) view.findViewById(R.id.txtVwExpiryDate);
            holder.txtVwQuantity = (TextView) view.findViewById(R.id.txtVwQuantity);
            holder.txtVwStatus = (TextView) view.findViewById(R.id.txtVwStatus);
            holder.mrelLayoutMangeList = (RelativeLayout) view.findViewById(R.id.relLayoutMangeList);
            holder.mrelMenuManage = (RelativeLayout) view.findViewById(R.id.relMenuManage);

            view.setTag(holder);
            //    view.setTag(R.string.mg_dl,mListings.get(position).getListing_id()); // setting random tag key as 12
        } else {
            holder = (ManageVouchersListing.ViewHolder) view.getTag();
        }

        //==============set data=============

        holder.txtVwName.setText(voucherListing.get(i).getName());
        if (voucherListing.get(i).getExpiry() != null && voucherListing.get(i).getExpiry().length() > 0) {
            holder.txtVwExpiryDate.setText(voucherListing.get(i).getExpiry().split(" ")[0]);
        } else {
            holder.txtVwExpiryDate.setText("Not Mentioned");
        }
        holder.txtVwQuantity.setText(voucherListing.get(i).getQuantity());

        if (voucherListing.get(i).getIsDeleted().equals("1")) {
            holder.mrelMenuManage.setVisibility(View.GONE);
            holder.txtVwStatus.setText("Deleted");
            holder.txtVwStatus.setTextColor(Color.parseColor("#FFF44336"));
        } else if (CommonUtils.isExpired(CommonUtils.getCurrentDate(),voucherListing.get(i).getExpiry())) {
            holder.mrelMenuManage.setVisibility(View.GONE);
            holder.txtVwStatus.setText("Expired");
            holder.txtVwStatus.setTextColor(Color.parseColor("#FFF44336"));
        } else {
            holder.mrelMenuManage.setVisibility(View.VISIBLE);
            holder.txtVwStatus.setText("Active");
            holder.txtVwStatus.setTextColor(Color.parseColor("#0c9695"));
        }


        //====================setImgae=================

        if (voucherListing.get(i).getType().equals("1")) {
            holder.tvVoucherAmount.setText("$" + voucherListing.get(i).getAmount());
        } else {
            holder.tvVoucherAmount.setText(voucherListing.get(i).getAmount() + "%");
        }

        //================click on menu==================

        holder.mrelMenuManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final CharSequence[] options = {"Edit", "Assign Listings", "Delete", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setItems(options, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Edit")) {
                            RippleittAppInstance.getInstance()
                                    .setCURRENT_VOUCHER_OBJECT(voucherListing.get(i));
                            context.startActivity(new Intent(context,
                                    ActivityAddVoucher.class));
                        }
                        if (options[item].equals("Assign Listings")) {
                            Intent intent = new Intent(context,
                                    ActivitySelectListing.class);
                            intent.putExtra("voucher_id", voucherListing.get(i).getVoucherId());
                            intent.putExtra("amount_type", voucherListing.get(i).getType());
                            if (voucherListing.get(i).getAsignedListingId() != null)
                                intent.putExtra("added_listing", voucherListing.get(i).getAsignedListingId());
                            context.startActivity(intent);
                        }
                        if (options[item].equals("Delete")) {
                            showDeletionConfirmation((voucherListing.get(i).getVoucherId()));

                        } else if (options[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        return view;
    }

    private void showDeletionConfirmation(final String voucherID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to suspend this voucher?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(context, "Deleting listing please wait", Toast.LENGTH_LONG).show();
                        new DeleteVoucherApi()
                                .deleteVoucherApi(context, voucherID, mLoader);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    static class ViewHolder {
        TextView tvVoucherAmount;
        TextView txtVwName;
        TextView txtVwExpiryDate;
        TextView txtVwQuantity;
        TextView txtVwStatus;
        RelativeLayout mrelLayoutMangeList;
        RelativeLayout mrelMenuManage;
    }

}
