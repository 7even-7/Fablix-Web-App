package edu.uci.ics.fabflixmobile.ui.movielist;

import android.content.Intent;
import android.widget.Button;
import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.model.Movie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.uci.ics.fabflixmobile.databinding.ActivityLoginBinding;
import edu.uci.ics.fabflixmobile.ui.login.LoginActivity;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<Movie> {
    private final ArrayList<Movie> movies;
    private Context context;

    // View lookup cache
    private static class ViewHolder {
        TextView title;
        TextView subtitle;
        TextView year;
        TextView stars;
        TextView genres;
        TextView rating;
    }

    public MovieListViewAdapter(Context context, ArrayList<Movie> movies) {
        super(context, R.layout.movielist_row, movies);
        this.context = context;
        this.movies = movies;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the movie item for this position
        Movie movie = movies.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.movielist_row, parent, false);
            viewHolder.title = convertView.findViewById(R.id.title);
            viewHolder.subtitle = convertView.findViewById(R.id.subtitle);
            viewHolder.year = convertView.findViewById(R.id.year);
            viewHolder.stars = convertView.findViewById(R.id.stars);
            viewHolder.genres = convertView.findViewById(R.id.genres);
            viewHolder.rating = convertView.findViewById(R.id.rating);
            viewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the item click event

                    // Perform any other actions you want to take when a movie is clicked
                    // For example, you can start a new activity to display movie details:
                    Intent intent = new Intent(getContext(), SingleMovieActivities.class);
                    intent.putExtra("movie", movie.getId());
                    getContext().startActivity(intent);
                }
            });
            //set subtitle
            viewHolder.subtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the item click event

                    // Perform any other actions you want to take when a movie is clicked
                    // For example, you can start a new activity to display movie details:
                    Intent intent = new Intent(getContext(), SingleMovieActivities.class);
                    intent.putExtra("movie", movie.getId());
                    getContext().startActivity(intent);
                }
            });
            //set subtitle
            viewHolder.year.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the item click event

                    // Perform any other actions you want to take when a movie is clicked
                    // For example, you can start a new activity to display movie details:
                    Intent intent = new Intent(getContext(), SingleMovieActivities.class);
                    intent.putExtra("movie", movie.getId());
                    getContext().startActivity(intent);
                }
            });
            //set subtitle
            viewHolder.genres.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the item click event

                    // Perform any other actions you want to take when a movie is clicked
                    // For example, you can start a new activity to display movie details:
                    Intent intent = new Intent(getContext(), SingleMovieActivities.class);
                    intent.putExtra("movie", movie.getId());
                    getContext().startActivity(intent);
                }
            });
            //set subtitle
            viewHolder.stars.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle the item click event

                    // Perform any other actions you want to take when a movie is clicked
                    // For example, you can start a new activity to display movie details:
                    Intent intent = new Intent(getContext(), SingleMovieActivities.class);
                    intent.putExtra("movie", movie.getId());
                    getContext().startActivity(intent);
                }
            });
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);

        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Set the click listener on the ViewHolder


        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.title.setText(movie.getName());
        viewHolder.subtitle.setText("Director: " + movie.getDirector());
        viewHolder.year.setText("Year: " + movie.getYear());
        viewHolder.stars.setText("Stars: " + movie.getStarsName());
        viewHolder.genres.setText("Genres: " + movie.getGenres());
        viewHolder.genres.setText("Rating: " + movie.getRating());

        // Return the completed view to render on screen
        return convertView;
    }
}
