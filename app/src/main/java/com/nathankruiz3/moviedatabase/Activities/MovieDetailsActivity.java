package com.nathankruiz3.moviedatabase.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nathankruiz3.moviedatabase.Model.Movie;
import com.nathankruiz3.moviedatabase.R;
import com.nathankruiz3.moviedatabase.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    private TextView movieTitle, movieYear, director, actors, category, rating, writers, plot, boxOffice, runtime;
    private ImageView movieImage;

    private RequestQueue queue;
    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        queue = Volley.newRequestQueue(this);

        // Casting movie object to the movie extra from our intent, setting ID
        movie = (Movie) getIntent().getSerializableExtra("movie");
        movieID = movie.getImdbID();

        setUpUI();
        getMovieDetails(movieID);

    }

    private void getMovieDetails(String id) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, (Constants.URL + id), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.has("Ratings")) {
                                JSONArray ratings = response.getJSONArray("Ratings");

                                String source = null;
                                String value = null;

                                if(ratings.length() > 0) {
                                    JSONObject mRatings = ratings.getJSONObject(ratings.length() - 1);

                                    source = mRatings.getString("Source");
                                    value = mRatings.getString("Value");
                                    rating.setText(source + " : " + value);
                                } else {
                                    rating.setText("Ratings : N/A");
                                }
                            }

                            movieTitle.setText(response.getString("Title"));
                            movieYear.setText("Released: " + response.getString("Year"));
                            director.setText("Directed By: " + response.getString("Director"));
                            actors.setText("Actors: " + response.getString("Actors"));
                            category.setText("Genre: " + response.getString("Genre"));
                            writers.setText("Writers: " + response.getString("Writer"));
                            plot.setText("Plot: " + response.getString("Plot"));
                            boxOffice.setText("Box Office: " + response.getString("BoxOffice"));
                            runtime.setText("Runtime: " + response.getString("Runtime"));

                            Picasso.get()
                                    .load(response.getString("Poster"))
                                    .placeholder(android.R.drawable.ic_btn_speak_now)
                                    .into(movieImage);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " , error.getMessage());
            }
        });
        queue.add(request);

    }

    private void setUpUI() {

        movieTitle = (TextView) findViewById(R.id.movieTitleIDDet);
        movieYear = (TextView) findViewById(R.id.movieReleaseIDDet);
        director = (TextView) findViewById(R.id.movieDirectorIDDet);
        actors = (TextView) findViewById(R.id.actorsDet);
        category = (TextView) findViewById(R.id.movieCatIDDet);
        rating = (TextView) findViewById(R.id.movieRatingIDDet);
        writers = (TextView) findViewById(R.id.writersDet);
        plot = (TextView) findViewById(R.id.plotDet);
        boxOffice = (TextView) findViewById(R.id.boxOfficeDet);
        runtime = (TextView) findViewById(R.id.movieRuntimeIDDet);
        movieImage = (ImageView) findViewById(R.id.movieImageIDDet);
    }
}