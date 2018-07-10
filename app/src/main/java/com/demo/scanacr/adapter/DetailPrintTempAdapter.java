package com.demo.scanacr.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogCompleteCreatePack;
import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.scanacr.R;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class DetailPrintTempAdapter extends RealmBaseAdapter<LogScanCreatePack> implements ListAdapter {

    public DetailPrintTempAdapter(OrderedRealmCollection<LogScanCreatePack> realmResults) {
        super(realmResults);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_scan_print_pack, parent, false);
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

    private void setDataToViews(HistoryHolder holder, LogScanCreatePack item) {
        holder.txtCodeColor.setText(item.getProductModel().getCodeColor());
        holder.txtHeight.setText(item.getProductModel().getDeep()+"");
        holder.txtLenght.setText(item.getProductModel().getLenght()+"");
        holder.txtWidth.setText(item.getProductModel().getWide()+"");
        holder.txtNumber.setText(item.getNumInput() + "");
        holder.txtSerial.setText(String.valueOf(item.getSerial()));
    }

    public class HistoryHolder extends RecyclerView.ViewHolder {

        TextView txtSerial;
        TextView txtWidth;
        TextView txtHeight;
        TextView txtLenght;
        TextView txtCodeColor;
        TextView txtNumber;

        private HistoryHolder(View v) {
            super(v);
            txtSerial = (TextView) v.findViewById(R.id.txt_serial);
            txtWidth = (TextView) v.findViewById(R.id.txt_width);
            txtHeight = (TextView) v.findViewById(R.id.txt_height);
            txtLenght = (TextView) v.findViewById(R.id.txt_lenght);
            txtCodeColor = (TextView) v.findViewById(R.id.txt_code_color);
            txtNumber = (TextView) v.findViewById(R.id.txt_number);
        }
    }
}
