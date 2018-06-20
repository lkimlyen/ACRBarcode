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
import com.demo.architect.data.model.offline.ScanWarehousingModel;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class ScanWarehousingAdapter extends RealmBaseAdapter<ScanWarehousingModel> implements ListAdapter {


    public ScanWarehousingAdapter(OrderedRealmCollection<ScanWarehousingModel> realmResults) {
        super(realmResults);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scan_warehousing, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final ScanWarehousingModel item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews( HistoryHolder holder, ScanWarehousingModel item) {
        holder.txtBarcode.setText(String.format(CoreApplication.getInstance().getString(R.string.text_code_request), item.getBarcode()));
        holder.txtDate.setText(String.format(CoreApplication.getInstance().getString(R.string.text_date_scan), ConvertUtils.ConvertStringToShortDate(item.getDeviceTime())));

    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtBarcode;
        TextView txtDate;

        private HistoryHolder(View v) {
            super(v);
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtDate = (TextView) v.findViewById(R.id.txt_date);
        }

    }

}
