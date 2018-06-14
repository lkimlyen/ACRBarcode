package com.demo.scanacr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import java.util.HashSet;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class CreateCodePackAdapter extends RealmRecyclerViewAdapter<LogScanCreatePack, CreateCodePackAdapter.HistoryHolder> {

    private boolean inDeletionMode = false;
    private Set<Integer> countersToDelete = new HashSet<>();
    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;

    public CreateCodePackAdapter(OrderedRealmCollection<LogScanCreatePack> data, OnItemClearListener listener,
                                 OnEditTextChangeListener onEditTextChangeListener) {
        super(data, true);
        this.onEditTextChangeListener = onEditTextChangeListener;
        setHasStableIds(true);
        this.listener = listener;
    }

    void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
        if (!enabled) {
            countersToDelete.clear();
        }
        notifyDataSetChanged();
    }

    Set<Integer> getCountersToDelete() {
        return countersToDelete;
    }

    public void selectAll(boolean isChecked, Set<Integer> integerSet) {
        if (isChecked) {
            countersToDelete.addAll(integerSet);
        } else {
            countersToDelete.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public HistoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_scan_create_pack, parent, false);
        HistoryHolder holder = new HistoryHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(HistoryHolder holder, int position) {
        final LogScanCreatePack obj = getItem(position);
        setDataToViews(holder, obj);
        holder.bind(obj, listener, onEditTextChangeListener);


    }

    private void setDataToViews(HistoryHolder holder, LogScanCreatePack item) {
        holder.txtRequestCode.setText(String.format(CoreApplication.getInstance().getString(R.string.text_code_request),item.getBarcode()));
        holder.txtDate.setText(String.format(CoreApplication.getInstance().getString(R.string.text_date_scan),ConvertUtils.ConvertStringToShortDate(item.getDeviceTime())));
        holder.txtQuantityProduct.setText(item.getNumTotal() + "");
        holder.txtQuantityRest.setText(item.getNumRest() + "");
        holder.txtQuantityScan.setText(item.getNumCodeScan() + "");
        holder.edtNumberScan.setText(String.valueOf(item.getNumInput()));

//        if (Long.valueOf(numTotal) == 0) {
//            String s3 = ("<font color='#FF0000' font size='60' >" + (0) + "</font>");
//            viewHolder.txtN55.setText(Html.fromHtml(s3), TextView.BufferType.SPANNABLE);
//        } else {
//            String s3 = ("<font color='#FF0000' font size='60' >" + (numTotal) + "</font>");
//            viewHolder.txtN55.setText(Html.fromHtml(s3), TextView.BufferType.SPANNABLE);
//        }
    }


    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtRequestCode;
        TextView txtDate;
        ImageView imgDelete;
        TextView txtQuantityProduct;
        TextView txtQuantityRest;
        TextView txtQuantityScan;
        EditText edtNumberScan;

        private HistoryHolder(View v) {
            super(v);
            txtRequestCode = (TextView) v.findViewById(R.id.txt_request_code);
            txtDate = (TextView) v.findViewById(R.id.txt_date);
            imgDelete = (ImageView) v.findViewById(R.id.img_delete);
            txtQuantityProduct = (TextView) v.findViewById(R.id.txt_quantity_product);
            txtQuantityRest = (TextView) v.findViewById(R.id.txt_quantity_rest);
            txtQuantityScan = (TextView) v.findViewById(R.id.txt_quantity_scan);
            edtNumberScan = (EditText) v.findViewById(R.id.edt_number);
        }

        private void bind(final LogScanCreatePack item, final OnItemClearListener listener, final OnEditTextChangeListener onEditTextChangeListener) {
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
            edtNumberScan.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        EditText number = (EditText) v;
                        try {
                            int numberInput = Integer.parseInt(number.getText().toString());
                            if (numberInput <= 0) {
                                Toast.makeText(CoreApplication.getInstance(),
                                        CoreApplication.getInstance().getText(R.string.text_number_bigger_zero)
                                        , Toast.LENGTH_SHORT).show();
                                edtNumberScan.setText("1");
                                return;
                            }

                            if (numberInput > (Integer.parseInt(txtQuantityRest.getText().toString())+numberInput)){
                                Toast.makeText(CoreApplication.getInstance(),
                                        CoreApplication.getInstance().getText(R.string.text_quantity_input_bigger_quantity_rest)
                                        , Toast.LENGTH_SHORT).show();
                                edtNumberScan.setText(txtQuantityRest.getText().toString());
                                return;
                            }
                            if (numberInput > 0) {
                                onEditTextChangeListener.onEditTextChange(item, Integer.parseInt(number.getText().toString()));
                            }

                        } catch (Exception e) {

                        }
                    }
                }
            });


        }
    }

    public interface OnItemClearListener {
        void onItemClick(LogScanCreatePack item);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanCreatePack item, int number);
    }

}
