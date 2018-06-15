package com.demo.scanacr.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class CreateCodePackAdapter extends RealmRecyclerViewAdapter<LogScanCreatePack, CreateCodePackAdapter.HistoryHolder> {

    private boolean inDeletionMode = false;
    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;

    public CreateCodePackAdapter(OrderedRealmCollection<LogScanCreatePack> data, OnItemClearListener listener,
                                 OnEditTextChangeListener onEditTextChangeListener) {
        super(data, true);
        setHasStableIds(true);
        this.onEditTextChangeListener = onEditTextChangeListener;
        this.listener = listener;
    }

    public void enableDeletionMode(boolean enabled) {
        inDeletionMode = enabled;
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
        holder.txtRequestCode.setText(String.format(CoreApplication.getInstance().getString(R.string.text_code_request), item.getBarcode()));
        holder.txtDate.setText(String.format(CoreApplication.getInstance().getString(R.string.text_date_scan), ConvertUtils.ConvertStringToShortDate(item.getDeviceTime())));
        holder.txtQuantityProduct.setText(item.getNumTotal() + "");
        holder.txtQuantityRest.setText(item.getNumRest() + "");
        holder.txtQuantityScan.setText(item.getNumCodeScan() + "");
        holder.edtNumberScan.setText(String.valueOf(item.getNumInput()));

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int numberInput = Integer.parseInt(s.toString());
                    if (numberInput <= 0) {
                        Toast.makeText(CoreApplication.getInstance(),
                                CoreApplication.getInstance().getText(R.string.text_number_bigger_zero)
                                , Toast.LENGTH_SHORT).show();
                        holder.edtNumberScan.setText(item.getNumInput() + "");
                        return;
                    }
                    if (numberInput - item.getNumInput() > item.getNumRest()) {
                        Toast.makeText(CoreApplication.getInstance(),
                                CoreApplication.getInstance().getText(R.string.text_quantity_input_bigger_quantity_rest)
                                , Toast.LENGTH_SHORT).show();
                        holder.edtNumberScan.setText(item.getNumInput() + "");
                        return;
                    }
                    if (numberInput == item.getNumInput()) {
                        return;
                    }
                    onEditTextChangeListener.onEditTextChange(item, numberInput);

                } catch (Exception e) {

                }
            }
        };

        holder.edtNumberScan.removeTextChangedListener(textWatcher);
        holder.edtNumberScan.addTextChangedListener(textWatcher);

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


        }
    }

    public interface OnItemClearListener {
        void onItemClick(LogScanCreatePack item);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanCreatePack item, int number);
    }

}
