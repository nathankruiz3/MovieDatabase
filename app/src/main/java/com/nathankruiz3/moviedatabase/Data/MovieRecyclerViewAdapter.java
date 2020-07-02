package com.nathankruiz3.moviedatabase.Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nathankruiz3.moviedatabase.Activities.MovieDetailsActivity;
import com.nathankruiz3.moviedatabase.Model.Movie;
import com.nathankruiz3.moviedatabase.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Movie> movieList;

    public MovieRecyclerViewAdapter(Context context, List<Movie> movies) {
        this.context = context;
        movieList = movies;
    }

    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_row, parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieRecyclerViewAdapter.ViewHolder holder, int position) {
        Movie movie = movieList.get(position);
        String posterLink = movie.getPoster();

        holder.title.setText(movie.getTitle());
        holder.type.setText(movie.getType());

        Picasso.get()
                .load(posterLink)
                .placeholder(android.R.drawable.ic_btn_speak_now)
                .into(holder.poster);

        holder.year.setText(movie.getYear());
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, year, type;
        ImageView poster;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = (TextView) itemView.findViewById(R.id.movieTitleID);
            poster = (ImageView) itemView.findViewById(R.id.movieImageID);
            year = (TextView) itemView.findViewById(R.id.movieReleaseID);
            type = (TextView) itemView.findViewById(R.id.movieCatID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "Row Tapped!", Toast.LENGTH_SHORT).show();

                    // New movie object based off what row was tapped
                    Movie movie = movieList.get(getAdapterPosition());

                    // New intent towards movie details activity
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    // putting movie object into extras
                    intent.putExtra("movie", movie);
                    // starting movie details activity
                    context.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View view) {

        }
    }
}
