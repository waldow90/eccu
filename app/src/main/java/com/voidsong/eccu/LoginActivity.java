package com.voidsong.eccu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.ShareActionProvider;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.preference.DialogPreference;
import android.widget.ImageButton;
import android.support.v4.app.Fragment;

import com.voidsong.eccu.network.User;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.exceptions.SecurityErrorException;
import com.voidsong.eccu.support_classes.IPDialog;
import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.support_classes.Checker;

import org.w3c.dom.Attr;

import java.security.GeneralSecurityException;


public class LoginActivity extends AppCompatActivity {

    private final String TAG = "ECCU/LoginActivity";

    private EditText login;
    private EditText password;
    private EditText ipset;
    private AppCompatButton button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Checker.isDebuggable(getApplicationContext())) {
            Log.d(TAG, "detecting debug"); // TODO change
        }

        if (Checker.isRunningEmulator()) {
            Log.d(TAG, "detecting emulator"); // TODO change
        }

        if (Checker.isRooted()) {
            Log.d(TAG, "detecting root"); // TODO change
        }

        login = (EditText)findViewById(R.id.input_login);
        password = (EditText)findViewById(R.id.input_password);
        button = (AppCompatButton)findViewById(R.id.btn_login);

        try {
            Internet.Init();
        } catch (SecurityErrorException | GeneralSecurityException e) {
            e.printStackTrace(); // TODO change
        }

        int result = Settings.load();
        if (result != 0) {
            // TODO change
        }



    }

    public void IPImageButton(View view){
        DialogFragment newFragment = new IPDialog();
        newFragment.show(getSupportFragmentManager(), "IPSettings");
    }



}
