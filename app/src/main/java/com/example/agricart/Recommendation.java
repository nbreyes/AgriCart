package com.example.agricart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Recommendation extends AppCompatActivity implements LocationListener{
    Toolbar toolbar;
    TextView responseChatGPT;
    TextView test;
    public static final MediaType JSON

            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    String chatGPTReply;
    double lat;
    double longitude;
    double celsiusTemp;
    String city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);

        test = (TextView) findViewById(R.id.test);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        try {
            getUserLocation();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        responseChatGPT = (TextView) findViewById(R.id.responseChatGPT);
        promptChatGPT();
    }

    public void getUserLocation() throws IOException {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

    private void openWeatherMap() {

        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=0bc8c9fc7d82306fb795fff1e0105067";

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string()).getJSONObject("main");
                        String kelvinTemp = jsonObject.getString("temp");
                        celsiusTemp = Double.parseDouble(kelvinTemp) - 273.15;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                test.setText(String.format(Locale.getDefault(), "%.2f", celsiusTemp) + " lat: " + lat + ", long: " + longitude + ", " + city);
                            }
                        });
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new IOException();
                }
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void getLocation() throws IOException {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        celsiusTemp = 0.0;
        lat = 0;
        longitude = 0;
        city = null;
        if (location != null) {
            lat = location.getLatitude();
            longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(Recommendation.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, longitude, 1);
            city = addresses.get(0).getLocality();
            openWeatherMap();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 200 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                getLocation();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            test.setText("Permission Denied");
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }


    public void promptChatGPT() {


        JSONObject JsonBody = new JSONObject();
        try {
            JsonBody.put("model", "text-davinci-003");
            String prompt = "What crop should I plant in " + String.format(Locale.getDefault(), "%.2f", celsiusTemp) + " degrees celsius in "+ city +"? Give at least 10. Cite sources and percentage of accuracy";
            JsonBody.put("prompt", prompt);
            JsonBody.put("max_tokens", 1000);
            JsonBody.put("temperature", 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(JsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/completions")
                .header("Authorization", "Bearer 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")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {

                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray jsonArray = jsonObject.getJSONArray("choices");
                    chatGPTReply = jsonArray.getJSONObject(0).getString("text");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        responseChatGPT.setText(chatGPTReply);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        responseChatGPT.setText("What crop should I plant in " + String.format(Locale.getDefault(), "%.2f", celsiusTemp) + " degrees celsius in "+ city +"? Give at least 10. Cite sources and percentage of accuracy");
                    }
                });
            }
        });
    }
}