package com.demo.scanacr.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class DeliveryAdapter extends RealmBaseAdapter<ScanDeliveryModel> implements ListAdapter {

    public DeliveryAdapter(OrderedRealmCollection<ScanDeliveryModel> realmResults) {
        super(realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scan, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }

        if (adapterData != null) {
            final ScanDeliveryModel item = getItem(position);
            setDataToViews(viewHolder, item);
        }
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, ScanDeliveryModel item) {
        holder.txtBarcode.setText(item.getBarcode());
        holder.txtDate.setText(ConvertUtils.ConvertStringToShortDate(item.getDeviceTime()));

    }

    public class HistoryHolder {

        TextView txtBarcode;
        TextView txtDate;

        private HistoryHolder(View v) {
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtDate = (TextView) v.findViewById(R.id.txt_date_create);
        }

    }

}
