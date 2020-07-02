
// Allows us to store the previously searched keywords and display the most recent as the search hint

package com.nathankruiz3.moviedatabase.Util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    // Initializing sharedPreferences
    SharedPreferences sharedPreferences;

    // Create a constructor that takes an activity as the argument
    public Prefs(Activity activity) {
        sharedPreferences = activity.getPreferences(activity.MODE_PRIVATE);

    }

    public void setSearch(String search) {
        sharedPreferences.edit().putString("search", search).commit();
    }

    public String getSearch() {
        return sharedPreferences.getString("search", "Batman");
    }
}
