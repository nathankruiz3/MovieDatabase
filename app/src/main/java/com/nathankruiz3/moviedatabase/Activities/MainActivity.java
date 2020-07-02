package com.nathankruiz3.moviedatabase.Activities;

import android.os.Bundle;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nathankruiz3.moviedatabase.Data.MovieRecyclerViewAdapter;
import com.nathankruiz3.moviedatabase.Model.Movie;
import com.nathankruiz3.moviedatabase.R;
import com.nathankruiz3.moviedatabase.Util.Constants;
import com.nathankruiz3.moviedatabase.Util.Prefs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Initializing variables
    private RecyclerView recyclerView;
    private MovieRecyclerViewAdapter movieRecyclerViewAdapter;
    private List<Movie> movieList;
    private RequestQueue queue;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting up request queue
        queue = Volley.newRequestQueue(this);



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputDialog();
            }
        });

        // Setting up recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        movieList = new ArrayList<>();

        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        getMovies(search);
        movieRecyclerViewAdapter.notifyDataSetChanged();

    }

    // Get movies method
    public List<Movie> getMovies(String searchTerm) {
        movieList.clear();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, (Constants.URL_LEFT + searchTerm + Constants.URL_RIGHT), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{
                            JSONArray moviesArray = response.getJSONArray("Search");


                            for (int i = 0; i < moviesArray.length(); i++) {

                                JSONObject movieObject = moviesArray.getJSONObject(i);

                                Movie movie = new Movie();
                                movie.setTitle(movieObject.getString("Title"));
                                movie.setYear("Released : " + movieObject.getString("Year"));
                                movie.setType("Type : " + movieObject.getString("Type"));
                                movie.setPoster(movieObject.getString("Poster"));
                                movie.setImdbID(movieObject.getString("imdbID"));

                                movieList.add(movie);

                                Log.d("Movies" , movie.getTitle());

                            }

                            movieRecyclerViewAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error: " , error.getLocalizedMessage());
            }
        });

        queue.add(request);

        return movieList;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            showInputDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showInputDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.search_popup, null);
        final EditText newSearchET = (EditText) view.findViewById(R.id.searchET);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);

        builder.setView(view);
        dialog = builder.create();
        dialog.show();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs = new Prefs(MainActivity.this);

                if(!newSearchET.getText().toString().isEmpty()) {

                    String newSearch = newSearchET.getText().toString();
                    prefs.setSearch(newSearch);
                    movieList.clear();
                    getMovies(newSearch);
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                    dialog.dismiss();

                }else {
                    Toast.makeText(MainActivity.this, "Must enter text", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}