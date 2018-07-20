package com.demo.scanacr.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.demo.scanacr.R;

public class ChangeIPAddressDialog extends DialogFragment {

    private OnItemSaveListener listener;

    private String ipAddress, port;

    public void setListener(OnItemSaveListener listener) {
        this.listener = listener;
    }

    public void setContent(String ipAddress, String port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.setContentView(R.layout.dialog_change_ip_address);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawableResource(R.color.transparent_black_hex_11);
        EditText edtIPAddress = dialog.findViewById(R.id.edt_ip_address);
        EditText edtPort = dialog.findViewById(R.id.edt_port);

        edtIPAddress.setText(ipAddress);
        edtPort.setText(port);
        dialog.findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        dialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int port = Integer.parseInt(edtPort.getText().toString());
                    listener.onSave(edtIPAddress.getText().toString(), port);
                } catch (NumberFormatException e) {
                    Toast.makeText(getActivity(), getString(R.string.text_port_null), Toast.LENGTH_LONG).show();
                }

            }
        });
        return dialog;
    }

    public interface OnItemSaveListener {
        void onSave(String ipAddress, int port);
    }
}
