package com.example.bstrackingtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class GetProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String api = "/full";
        String url = MainActivity.Basic + MainActivity.id + api;

        Intent intent = getIntent();
        TextView profileName = findViewById(R.id.profileName);
        TextView globalRank = findViewById(R.id.GlobalRank);
        TextView countryRank = findViewById(R.id.CountryRank);
        TextView totalPlayCount = findViewById(R.id.TotalPlayCount);
        TextView totalRankedCount = findViewById(R.id.TotalRankedCount);

        RequestQueue requestQueue = Volley.newRequestQueue(this);//create a request Queue and pass the context of the activity

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    Map<String, String> profileDetails = new HashMap<String, String>();

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            profileDetails = new ObjectMapper().readValue(response.toString(), HashMap.class);
                            String playCount = response.getJSONObject("scoreStats").get("totalPlayCount").toString();
                            String rankCount = response.getJSONObject("scoreStats").get("rankedPlayCount").toString();

                            profileName.setText(profileDetails.get("name"));
                            globalRank.setText(String.valueOf(profileDetails.get("rank")));
                            countryRank.setText(String.valueOf(profileDetails.get("countryRank")));
                            totalPlayCount.setText(playCount);
                            totalRankedCount.setText(rankCount);

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });


        requestQueue.add(jsonObjectRequest); //pass the jsonObjectRequest
    }
}