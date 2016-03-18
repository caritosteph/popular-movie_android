package com.carito.movies;

import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.carito.movies.model.Movie;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieFragment.CallbackMovie {

    private boolean detailPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(findViewById(R.id.detailfragment) != null){
            detailPanel = true;
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.detailfragment,new DetailFragment(),
                                DetailFragment.LOG_TAG)
                        .commit();
            }
        }else{
            detailPanel = false;
        }
    }

    public void onItemSelected(Movie movie){
        if(detailPanel){
            Bundle bundle = new Bundle();
            bundle.putParcelable(DetailFragment.DETAIL_MOVIE, movie);

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailfragment,detailFragment,DetailFragment.LOG_TAG)
                    .commit();

        }else{
            Intent intent = new Intent(this,DetailActivity.class)
                    .putExtra(DetailFragment.DETAIL_MOVIE,movie);
            startActivity(intent);
        }
    }
}
