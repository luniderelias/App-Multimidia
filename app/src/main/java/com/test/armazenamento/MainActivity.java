package com.test.armazenamento;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Fragment contentFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        contentFragment = new AnimationFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, contentFragment).commit();
        toolbar.setSubtitle(getString(R.string.animations));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        String title = toolbar.getSubtitle().toString();

        switch (id){
            case R.id.nav_animations:{
                contentFragment = new AnimationFragment();
                title = getString(R.string.animations);
                break;
            }
            case R.id.nav_graphics:{
                contentFragment = new GraphicsFragment();
                title = getString(R.string.graphics);
                break;
            }
            case R.id.nav_picture:{
                contentFragment = new PictureFragment();
                title = getString(R.string.images);
                break;
            }
            case R.id.nav_audio:{
                contentFragment = new AudioFragment();
                title = getString(R.string.audios);
                break;
            }
            case R.id.nav_video:{
                contentFragment = new VideoFragment();
                title = getString(R.string.videos);
                break;
            }
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.content, contentFragment).commit();
        toolbar.setSubtitle(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
