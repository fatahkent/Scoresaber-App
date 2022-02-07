package com.example.bstrackingtest;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "Jisoo";
    public static final String Basic = "https://scoresaber.com/api/player/";
    public static String id = "76561198147476743";
    public static String profileType = "/full";
    public static String total;
    public static String rank;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RequestQueue requestQueue = Volley.newRequestQueue(this);//create a request Queue and pass the context of the activity

        String url = Basic + id + profileType;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String playCount = response.getJSONObject("scoreStats").get("totalPlayCount").toString();
                            String rankCount = response.getJSONObject("scoreStats").get("rankedPlayCount").toString();
                            total = playCount;
                            rank = rankCount;

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });

        System.out.println(total);
        requestQueue.add(jsonObjectRequest); //pass the jsonObjectRequest
    }

    /** Called when the user taps the BeatSaber Profile button */
    public void getProfile(View view) {
        Intent intent = new Intent(this, GetProfileActivity.class);
        startActivity(intent);
    }

    public void getRanked(View view) {
        System.out.println("1: " + total);
        System.out.println("2: " + rank);
        bundle.putString("total", total);
        bundle.putString("rank", rank);
        Intent intent2 = new Intent(this, GetRankedSongsActivity.class);
        intent2.putExtras(bundle);
        startActivity(intent2);
    }

}