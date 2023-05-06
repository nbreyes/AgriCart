package com.example.agricart;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RecommendationFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RecommendationFragment() {
    }
    public static RecommendationFragment newInstance(String param1, String param2) {
        RecommendationFragment fragment = new RecommendationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static final MediaType JSON

            = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    TextView responseChatGPT;
    String chatGPTReply;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recommendation, container, false);
        responseChatGPT = (TextView) view.findViewById(R.id.responseChatGPT);
        promptChatGPT();
        return view;
    }



    public void promptChatGPT() {
        JSONObject JsonBody = new JSONObject();
        try {
            JsonBody.put("model","text-davinci-003");
            String prompt = "What crop should I plant in 28 degrees celsius in Philippines? Give at least 10. Cite sources and percentage of accuracy";
            JsonBody.put("prompt",prompt);
            JsonBody.put("max_tokens", 1000);
            JsonBody.put("temperature",1);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        RequestBody body = RequestBody.create(JsonBody.toString(),JSON);
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

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        responseChatGPT.setText(chatGPTReply);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                responseChatGPT.setText("failed!");
            }
        });
    }
}