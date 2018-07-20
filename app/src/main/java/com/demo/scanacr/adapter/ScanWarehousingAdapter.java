package com.demo.scanacr.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.ScanWarehousingModel;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

public class ScanWarehousingAdapter extends BaseAdapter {
    public Context context;
    private List<ScanWarehousingModel> list;

    public ScanWarehousingAdapter(Context context, List<ScanWarehousingModel> list) {
        this.context = context;
        this.list = list;
    }

    public void addItem(ScanWarehousingModel item){
        list.add(item);
        notifyDataSetChanged();
    }

    public void clearItem(){
        list = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public ScanWarehousingModel getItem(int position) {
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

            final ScanWarehousingModel item = getItem(position);
            setDataToViews(viewHolder, item);

        return convertView;
    }

    private void setDataToViews( HistoryHolder holder, ScanWarehousingModel item) {
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
