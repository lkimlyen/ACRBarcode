package com.demo.scanacr.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.architect.data.model.offline.ProductModel;
import com.demo.scanacr.R;
import com.demo.scanacr.app.CoreApplication;
import com.demo.scanacr.util.ConvertUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CreateBarcodeDialog extends DialogFragment {

    private OnItemSaveListener listener;

    private ProductModel model;
    private String barcode;
    private int numberOld = 1;

    public void setListener(OnItemSaveListener listener) {
        this.listener = listener;
    }

    public void setModel(ProductModel model, String barcode) {
        this.model = model;
        this.barcode = barcode;
    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_create_barcode);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent_black_hex_11);

        TextView txtRequestCode = (TextView) dialog.findViewById(R.id.txt_request_code);
        TextView txtDate = (TextView) dialog.findViewById(R.id.txt_date);
        TextView txtQuantityProduct = (TextView) dialog.findViewById(R.id.txt_quantity_product);
        TextView txtQuantityRest = (TextView) dialog.findViewById(R.id.txt_quantity_rest);
        TextView txtQuantityScan = (TextView) dialog.findViewById(R.id.txt_quantity_scan);
        EditText edtNumberScan = (EditText) dialog.findViewById(R.id.edt_number);
        txtRequestCode.setText(String.format(CoreApplication.getInstance().getString(R.string.text_code_request), barcode));
        txtDate.setText(String.format(CoreApplication.getInstance().getString(R.string.text_date_scan), ConvertUtils.ConvertStringToShortDate(ConvertUtils.getDateTimeCurrent())));
        txtQuantityProduct.setText(model.getNumber() + "");
        txtQuantityRest.setText(model.getNumberRest() + "");
        txtQuantityScan.setText(model.getNumCompleteScan() + "");
        edtNumberScan.setText("1");

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
                        edtNumberScan.setText("1");
                        return;
                    }
                    if (numberInput - numberOld > model.getNumberRest()) {
                        Toast.makeText(CoreApplication.getInstance(),
                                CoreApplication.getInstance().getText(R.string.text_quantity_input_bigger_quantity_rest)
                                , Toast.LENGTH_SHORT).show();
                        edtNumberScan.setText(model.getNumberRest() + "");
                        return;
                    }
                    numberOld = numberInput;
                    txtQuantityRest.setText(String.valueOf(model.getNumber() - numberOld));

                } catch (Exception e) {

                }
            }
        };

        edtNumberScan.addTextChangedListener(textWatcher);
        dialog.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(getString(R.string.text_title_noti))
                        .setContentText(getString(R.string.text_cancel_create_barcode))
                        .setConfirmText(getString(R.string.text_yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                dialog.dismiss();
                                sweetAlertDialog.dismiss();
                            }
                        })
                        .setCancelText(getString(R.string.text_no))
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                            }
                        });
            }
        });

        dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                listener.onSave(Integer.parseInt(edtNumberScan.getText().toString()));
            }
        });
        return dialog;
    }

    public interface OnItemSaveListener {
        void onSave(int numberInput);
    }
}
