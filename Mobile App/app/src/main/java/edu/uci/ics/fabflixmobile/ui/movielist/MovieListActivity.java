package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.databinding.ActivityMovielistBinding;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieListActivity extends AppCompatActivity {
//    private final String host = "10.0.2.2";
    private final String host = "ec2-18-144-86-79.us-west-1.compute.amazonaws.com";

    private  String input;
    private final String port = "8080";
    private final String domain = "cs122b-project2-form-example";
    private final String baseURL = "http://" + host + ":" + port + "/" + domain;
    private int pageNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pageNum = 1;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movielist);
        //set button functionalities

        input = getIntent().getStringExtra("userinput");
        // TODO: this should be retrieved from the backend server

        fetchMoviefromDB();
        final Button prev = findViewById(R.id.Button1);
        final Button next = findViewById(R.id.Button2);
        prev.setOnClickListener(view -> goPrev());
        next.setOnClickListener(view -> goNext());

    }
    private void fetchMoviefromDB(){
        final ArrayList<Movie> movies = new ArrayList<>();
//        System.out.println("The input is: " + userInput);
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        //issue the get request & process the reponse from the full_text_search api
        final StringRequest searchRequest = new StringRequest(
                Request.Method.GET,
                baseURL + "/api/full_text_search" + "?query=" + input + "&pageNum="+pageNum,
                response -> {
                    // TODO: should parse the json response to redirect to appropriate functions
                    //  upon different response value.
                    Log.d("login.success", response);

                    try {
                        JSONArray jsonResponse = new JSONArray(response);
                        System.out.println(jsonResponse.toString());
                        System.out.println("The number of movies is: " + jsonResponse.length());
                        for(int i = 0; i < jsonResponse.length(); i++){
                            JSONObject jso = jsonResponse.getJSONObject(i);
                            String title = jso.getString("movieTitle");
                            String year = jso.getString("movieYear");
                            String director = jso.getString("movieDirector");
                            String stars = "";
                            String[] starNames = jso.getString("movieStar").split(",");
                            String genres = jso.getString("genres");
                            String rating = jso.getString("movieRating");
                            //concatenate the stars
                            for (int j = 0; j < Math.min(starNames.length, 3); j++){
                                if (j == 0) stars += starNames[j];
                                else stars += starNames[j] + ", ";
                            }
                            String id = jso.getString("movieId");
                            //add the movie to the movie array
                            Movie m = new Movie(title, year, stars, director, genres, id, rating);
                            movies.add(m);

                        }
                        MovieListViewAdapter adapter = new MovieListViewAdapter(this, movies);
                        ListView listView = findViewById(R.id.list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener((parent, view, position, id) -> {
                            Movie movie = movies.get(position);
                            @SuppressLint("DefaultLocale") String message = String.format("Clicked on position: %d, name: %d, %d", position, movie.getName(), movie.getYear());
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        });

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

    @Override
    public void onBackPressed() {
        // Perform your desired actions when the back button is pressed
        // For example, you can go back to the previous activity or close the app

        // Call super.onBackPressed() to allow the default back button behavior (going back to the previous activity)
        super.onBackPressed();
    }
    private void goPrev(){

        if (pageNum > 1) {
            pageNum--;
            fetchMoviefromDB();
        };
    }

    private void goNext(){
        pageNum++;
        fetchMoviefromDB();
    }
}