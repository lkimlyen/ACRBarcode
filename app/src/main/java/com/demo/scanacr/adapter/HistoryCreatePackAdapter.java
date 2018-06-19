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

import com.demo.architect.data.model.offline.LogCompleteCreatePackList;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class HistoryCreatePackAdapter extends RealmBaseAdapter<LogCompleteCreatePackList> implements ListAdapter {

    public HistoryCreatePackAdapter(OrderedRealmCollection<LogCompleteCreatePackList> realmResults) {
        super(realmResults);
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
            final LogCompleteCreatePackList item = adapterData.get(position);
            setDataToViews(viewHolder, item);

        }
        return convertView;
    }

    private void setDataToViews( HistoryHolder holder, LogCompleteCreatePackList item) {
        holder.txtOrder.setText(item.getCodeRequest());
        holder.txtSerial.setText(String.valueOf(item.getSerial()));


    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtSerial;
        TextView txtOrder;

        private HistoryHolder(View v) {
            super(v);
            txtSerial = (TextView) v.findViewById(R.id.txt_serial);
            txtOrder = (TextView) v.findViewById(R.id.txt_order);
        }

    }


}
