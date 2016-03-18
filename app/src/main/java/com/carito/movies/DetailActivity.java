package com.carito.movies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by carito on 10/29/15.
 */
public class DetailActivity extends AppCompatActivity {

    public DetailActivity() {
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
/*
        if(savedInstanceState === null){
            Bundle bundle =  new Bundle();
            bundle.putParcelable(DetailFragment.EXTRA_MOVIE,
                    getIntent().getParcelableExtra(DetailFragment.EXTRA_MOVIE));
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detailfragment,detailFragment)
                    .commit();
        }*/
    }
}

