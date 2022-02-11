package com.example.bstrackingtest;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetUnrankedSongsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranked);

        TextView totalPlayCount = findViewById(R.id.TotalPlayCount);
        TextView songs = (TextView) findViewById(R.id.unrankedSongs);
        String api = "/scores?limit=8&sort=recent&page=";
        Bundle bundle = getIntent().getExtras();

        int page = 1;
        String totalStr = bundle.getString("total");
        String rankStr = bundle.getString("rank");
        int counter = Integer.parseInt(totalStr);
        int rankTotal = Integer.parseInt(rankStr);

        songs.setText("");
        songs.setMovementMethod(new ScrollingMovementMethod());
        RequestQueue requestQueue = Volley.newRequestQueue(this);//create a request Queue and pass the context of the activity
        while (counter>0){

            String url = MainActivity.Basic + MainActivity.id + api + page;
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        Map<String, String> rankedSongs = new HashMap<String, String>();
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                rankedSongs = new ObjectMapper().readValue(response.toString(), HashMap.class);
                                String playCount = response.getJSONObject("metadata").get("total").toString();
                                totalPlayCount.setText(playCount);
                                Map<String, String> songMap = new HashMap<String, String>();
                                JSONArray scoreArray = response.getJSONArray("playerScores");
                                for(int i=0;i<scoreArray.length();i++){
                                    JSONObject user = scoreArray.getJSONObject(i);
                                    String getClass = user.getJSONObject("score").get("pp").getClass().toString();
                                    String pp = user.getJSONObject("score").get("pp").toString();
                                    if (pp.equals("0")){
                                        String rank = (user.getJSONObject("score").get("rank")).toString();
                                        String songName = (String) user.getJSONObject("leaderboard").get("songName");
                                        songMap.put(songName,rank);
                                        songs.append(songName + ": " + rank + "\n");
                                    }
                                    else{
                                        continue;
                                    }
                                }

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
            counter-=8;
            page++;
        }
        System.out.println("End rank: " + rankTotal);
    }
}
