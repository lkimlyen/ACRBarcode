package com.demo.scanacr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.ScanDeliveryModel;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import java.util.List;

public class ScanDeliveryAdapter extends BaseAdapter {
    public Context context;
    private List<ScanDeliveryModel> list;

    public ScanDeliveryAdapter(Context context, List<ScanDeliveryModel> list) {
        this.context = context;
        this.list = list;
    }

    public void addItem(ScanDeliveryModel item){
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ScanDeliveryModel getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

            final ScanDeliveryModel item = getItem(position);
            setDataToViews(viewHolder, item);

        return convertView;
    }

    private void setDataToViews( HistoryHolder holder, ScanDeliveryModel item) {
        holder.txtBarcode.setText(String.format(CoreApplication.getInstance().getString(R.string.text_code_request), item.getBarcode()));
        holder.txtDate.setText(String.format(CoreApplication.getInstance().getString(R.string.text_date_scan), ConvertUtils.ConvertStringToShortDate(item.getDeviceTime())));

    }

    public class HistoryHolder{

        TextView txtBarcode;
        TextView txtDate;

        private HistoryHolder(View v) {
            txtBarcode = (TextView) v.findViewById(R.id.txt_barcode);
            txtDate = (TextView) v.findViewById(R.id.txt_date);
        }

    }

}
