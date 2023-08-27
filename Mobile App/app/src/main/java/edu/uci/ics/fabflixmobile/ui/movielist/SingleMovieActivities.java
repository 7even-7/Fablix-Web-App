package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

public class SingleMovieActivities extends AppCompatActivity {
    TextView title;
    TextView director;
    TextView year;
    TextView stars;
    TextView genres;
    TextView rating;
//    private final String host = "10.0.2.2";
    private final String host = "ec2-18-144-86-79.us-west-1.compute.amazonaws.com";

    private final String port = "8080";
    private final String domain = "cs122b-project2-form-example";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String movieID = getIntent().getStringExtra("movie");
        setContentView(R.layout.activity_singlemovie);
        title = findViewById(R.id.title);
        director = findViewById(R.id.director);
        year = findViewById(R.id.year);
        stars = findViewById(R.id.stars);
        genres = findViewById(R.id.genres);
        rating = findViewById(R.id.rating);


//        // TODO: this should be retrieved from the backend server
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //issue the get request & process the reponse from the full_text_search api
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/stars?id=" + movieID,
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    Log.d("login.success", response);
                    System.out.println(movieID);
                    try {
                        System.out.println(response.toString());
                        JSONArray jsonResponse = new JSONArray(response);
                        for(int i = 0; i < jsonResponse.length(); i++){
                            JSONObject jso = jsonResponse.getJSONObject(i);
                            String mtitle = jso.getString("movie_title");
                            String myear = jso.getString("movie_year");
                            String mdirector = jso.getString("movie_director");
                            String mstars = "";
                            String mrating = jso.getString("movie_rating");
                            String mgenres = jso.getString("movie_genres");
                            String[] starNames = jso.getString("movie_stars").split(",");
                            System.out.println(starNames);
                            HashSet<String> duplicateNames = new HashSet<>();
                            for (int j = 0; j < starNames.length; j++){

                                String[] name = starNames[i].split(":");
                                if (!duplicateNames.contains(name[1])) {mstars+= name[1];
                                duplicateNames.add(name[1]);
                                if (j != starNames.length-1 ) mstars+=", ";}

                            }
                            //concatenate the stars
                            title.setText(mtitle);
                            year.setText("Year: " + myear);
                            director.setText("Director:" + mdirector);
                            stars.setText("Stars: " + mstars);
                            rating.setText("Rating: " + mrating);
                            genres.setText("Genres: " + mgenres);
                            System.out.println("The return result is: " + jso.toString());
                            //add the movie to the movie array


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }






                },
                error -> {
                    // error
                    Log.d("login.error", error.toString());
                }){
        };
        queue.add(searchRequest);

    }
}