package com.voidsong.eccu.support_classes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.support.v4.app.Fragment;

import com.voidsong.eccu.LoginActivity;
import com.voidsong.eccu.R;

/**
 * Created by CoolHawk on 2/24/2017.
 */

public class IPDialog extends DialogFragment{
    private EditText editText;
    private Settings settings;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(inflater.inflate(R.layout.dialog_login, null))
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            editText = (EditText)  main_view.findViewById(R.id.ipaddress);
                        }catch (NullPointerException e) {
                            e.printStackTrace(); //TODO CHANGE
                        }
                        setIP();
                        IPDialog.this.getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IPDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }


    public void setIP(){
        settings.setIp(editText.getText().toString());
    }


}

