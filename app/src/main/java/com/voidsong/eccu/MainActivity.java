package com.voidsong.eccu;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import com.dd.CircularProgressButton;
import com.voidsong.eccu.abstract_classes.RefreshableFragment;
import com.voidsong.eccu.dialogs.BulbDialog;
import com.voidsong.eccu.dialogs.EngineDialog;
import com.voidsong.eccu.dialogs.InfoDialog;
import com.voidsong.eccu.fragments.FragmentCamera;
import com.voidsong.eccu.fragments.FragmentWeather;
import com.voidsong.eccu.network.API;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.CustomFragmentPagerAdapter;
import com.voidsong.eccu.support_classes.EccuCipher;
import com.voidsong.eccu.support_classes.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements BulbDialog.IBulbController,
        EngineDialog.IEngineController, InfoDialog.IInfoController, FragmentCamera.IFragmentCameraControl,
        FragmentWeather.IFragmentWeatherControl{

    private final String TAG = "ECCU/MainActivity";

    ViewPager pager;
    CustomFragmentPagerAdapter pagerAdapter;
    RefreshableFragment fragment;
    TabLayout tabLayout;

    AppCompatButton infoButton;
    AppCompatButton refreshButton;
    AppCompatButton bulbButton;
    AppCompatButton engineButton;

    // progress button
    CircularProgressButton pbutton;
    // end progress button

    EngineDialog engineDialog;
    BulbDialog bulbDialog;
    InfoDialog infoDialog;

    SecureRandom random = new SecureRandom();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("TAGMYTAG", "started main activity");

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        infoButton = (AppCompatButton) findViewById(R.id.info);
        //refreshButton = (AppCompatButton) findViewById(R.id.refresh);
        bulbButton = (AppCompatButton) findViewById(R.id.bulb);
        engineButton = (AppCompatButton) findViewById(R.id.engine);

        // progress button
        pbutton = (CircularProgressButton) findViewById(R.id.PB);
        pbutton.setIndeterminateProgressMode(true);
        // end progress button

        Log.d("TAGMYTAG", "started create dialogs");
        engineDialog = new EngineDialog();
        bulbDialog = new BulbDialog();
        infoDialog = new InfoDialog();

        Log.d("TAGMYTAG", "created dialogs");

        engineButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                engineDialog.show(getSupportFragmentManager(), EngineDialog.ID);
            }
        });

        bulbButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                bulbDialog.show(getSupportFragmentManager(), BulbDialog.ID);
            }
        });

        infoButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                infoDialog.show(getSupportFragmentManager(), InfoDialog.ID);
            }
        });

        // progress button
        pbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbutton.setProgress(50);
                RefreshableFragment fragment = pagerAdapter.getFragment(pager.getCurrentItem());
                fragment.refresh();
            }
        });

        initial_get_data_from_server();
    }

    @Override
    public void onBackPressed() {
        // ignore :)
    }

    @Override
    public void setActiveBulbCount(Integer active, Integer all) {
        String text = getResources().getString(R.string.bulb_label) +
                getResources().getString(R.string.double_space) +
                active +
                getResources().getString(R.string.splitter) +
                all;
        bulbButton.setText(text);
        Log.d("TAGMYTAG", bulbButton.getText().toString());
    }

    @Override
    public void setActiveEngineCount(Integer opened, Integer all) {
        String text = getResources().getString(R.string.engine_label) +
                getResources().getString(R.string.double_space) +
                opened +
                getResources().getString(R.string.splitter) +
                all;
        engineButton.setText(text);
    }

    @Override
    public void setTemperature(Integer temperature) {
        final String text = String.valueOf(temperature) +
                getResources().getString(R.string.one_space) +
                getResources().getString(R.string.degree);
        infoButton.post(new Runnable() {
            @Override
            public void run() {
                infoButton.setText(text);
            }
        });
    }

    @Override
    public void stopProgress() {
        pbutton.post(new Runnable() {
            @Override
            public void run() {
                pbutton.setProgress(0);
            }
        });
    }

    private void initial_get_data_from_server() {
        // updating temperature
        String rnd = String.valueOf(random.nextInt());
        HttpUrl temperature_url = new HttpUrl.Builder()
                .scheme(API.SCHEME)
                .host(Settings.getIp())
                .addPathSegment(API.GET_SETTED_TEMPERATURE)
                .addQueryParameter("rnd", rnd)
                .addQueryParameter("hash", EccuCipher.hash(rnd))
                .build();
        Log.d("TAGMYTAG", temperature_url.toString());
        Request temperature_request = new Request.Builder()
                .url(temperature_url)
                .build();
        Internet.getClient().newCall(temperature_request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int temperature = Integer.valueOf(response.body().string());
                setTemperature(temperature);
            }
        });

        // updating active count engines
        rnd = String.valueOf(random.nextInt());
        HttpUrl engine_count_url = new HttpUrl.Builder()
                .scheme(API.SCHEME)
                .host(Settings.getIp())
                .addPathSegment(API.GET_COUNT_ENGINE)
                .addQueryParameter("rnd", rnd)
                .addQueryParameter("hash", EccuCipher.hash(rnd))
                .build();
        Log.d("TAGMYTAG", engine_count_url.toString());
        Request engine_count_request = new Request.Builder()
                .url(engine_count_url)
                .build();
        Internet.getClient().newCall(engine_count_request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    final int active = json.getInt("active");
                    final int all = json.getInt("all");
                    setActiveEngineCount(active, all);
                } catch (JSONException e) {
                    // TODO
                }
            }
        });

        // updating active count lights
        rnd = String.valueOf(random.nextInt());
        HttpUrl light_count_url = new HttpUrl.Builder()
                .scheme(API.SCHEME)
                .host(Settings.getIp())
                .addPathSegment(API.GET_COUNT_LIGHT)
                .addQueryParameter("rnd", rnd)
                .addQueryParameter("hash", EccuCipher.hash(rnd))
                .build();
        Log.d("TAGMYTAG", light_count_url.toString());
        Request light_count_request = new Request.Builder()
                .url(light_count_url)
                .build();
        Internet.getClient().newCall(light_count_request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String body = response.body().string();
                try {
                    JSONObject json = new JSONObject(body);
                    final int active = json.getInt("active");
                    final int all = json.getInt("all");
                    setActiveBulbCount(active, all);
                } catch (JSONException e) {
                    // TODO
                }
            }
        });
    }
}
