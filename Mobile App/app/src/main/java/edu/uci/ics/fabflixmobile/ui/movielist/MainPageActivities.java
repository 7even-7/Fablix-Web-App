package edu.uci.ics.fabflixmobile.ui.movielist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.NetworkManager;
import edu.uci.ics.fabflixmobile.data.model.Movie;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.databinding.ActivityMainBinding;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainPageActivities extends AppCompatActivity {
    EditText input;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        // upon creation, inflate and initialize the layout
        setContentView(binding.getRoot());
        final Button searchButton = binding.search;
        searchButton.setOnClickListener(view -> search());
        input = (EditText) findViewById(R.id.userinput);


        //assign a listener to call a function to handle the user request when clicking a button
    }
    public void search(){

        //Complete and destroy login activity once successful

        String userInput = input.getText().toString();
        System.out.println("The input text is " + userInput);
        if (userInput.length() > 0){
            System.out.println("Success");

            //finish();
            // initialize the activity(page)/destination
            Intent MoviePage = new Intent(MainPageActivities.this, MovieListActivity.class);
            // activate the list page.
            //pass the userinput to moviepage into intent
            MoviePage.putExtra("userinput", userInput);
            startActivity(MoviePage);
        }



    }

    @Override
    public void onBackPressed() {
        // Perform your desired actions when the back button is pressed
        // For example, you can go back to the previous activity or close the app

        // Call super.onBackPressed() to allow the default back button behavior (going back to the previous activity)
        super.onBackPressed();
    }
}