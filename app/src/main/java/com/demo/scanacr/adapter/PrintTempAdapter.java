package com.demo.scanacr.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.architect.data.model.offline.LogScanCreatePack;
import com.demo.architect.data.model.offline.ProductModel;
import com.demo.scanacr.R;

import java.util.List;

public class PrintTempAdapter extends BaseAdapter {
    public Context context;
    private List<LogScanCreatePack> packList;
    private List<ProductModel> productList;

    public PrintTempAdapter(Context context, List<LogScanCreatePack> packList, List<ProductModel> productList) {
        this.context = context;
        this.packList = packList;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return packList.size();
    }

    @Override
    public Object getItem(int position) {
        return packList;
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
                    .inflate(R.layout.item_scan_print_pack, parent, false);
            viewHolder = new HistoryHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HistoryHolder) convertView.getTag();
        }
        final LogScanCreatePack pack = packList.get(position);
        final ProductModel product = productList.get(position);
        setDataToViews(viewHolder, pack, product);
        return convertView;
    }

    private void setDataToViews(HistoryHolder holder, LogScanCreatePack item, ProductModel model) {
        holder.txtCodeColor.setText(model.getCodeColor());
        holder.txtHeight.setText(model.getDeep()+"");
        holder.txtLenght.setText(model.getLenght()+"");
        holder.txtWidth.setText(model.getWide()+"");
        holder.txtNumber.setText(item.getNumInput() + "");
        holder.txtSerial.setText(String.valueOf(item.getSerial()));
    }

    public class HistoryHolder{

        TextView txtSerial;
        TextView txtWidth;
        TextView txtHeight;
        TextView txtLenght;
        TextView txtCodeColor;
        TextView txtNumber;

        private HistoryHolder(View v) {
            txtSerial = (TextView) v.findViewById(R.id.txt_serial);
            txtWidth = (TextView) v.findViewById(R.id.txt_width);
            txtHeight = (TextView) v.findViewById(R.id.txt_height);
            txtLenght = (TextView) v.findViewById(R.id.txt_lenght);
            txtCodeColor = (TextView) v.findViewById(R.id.txt_code_color);
            txtNumber = (TextView) v.findViewById(R.id.txt_number);
        }
    }
}
