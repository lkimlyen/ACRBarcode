package com.demo.scanacr.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class CreateCodePackListViewAdapter extends RealmBaseAdapter<LogScanCreatePack> implements ListAdapter {

    private OnItemClearListener listener;
    private OnEditTextChangeListener onEditTextChangeListener;

    public CreateCodePackListViewAdapter(OrderedRealmCollection<LogScanCreatePack> realmResults, OnItemClearListener listener,
                                         OnEditTextChangeListener onEditTextChangeListener) {
        super(realmResults);
        this.listener = listener;
        this.onEditTextChangeListener = onEditTextChangeListener;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scan_create_pack, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final LogScanCreatePack item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews( HistoryHolder holder, LogScanCreatePack item) {
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
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item);
            }
        });
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

    }

    public interface OnItemClearListener {
        void onItemClick(LogScanCreatePack item);
    }

    public interface OnEditTextChangeListener {
        void onEditTextChange(LogScanCreatePack item, int number);
    }

}
