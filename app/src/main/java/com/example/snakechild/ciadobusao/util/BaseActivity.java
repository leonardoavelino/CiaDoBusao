package com.example.snakechild.ciadobusao.util;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.snakechild.ciadobusao.AjudaActivity;
import com.example.snakechild.ciadobusao.EncontrosRecorrentesActivity;
import com.example.snakechild.ciadobusao.MapActivity;
import com.example.snakechild.ciadobusao.NovoEncontroActivity;
import com.example.snakechild.ciadobusao.R;
import com.example.snakechild.ciadobusao.TodosEncontrosActivity;

import java.util.ArrayList;

/**
 * Created by Pedro on 21/05/2015.
 */
public class BaseActivity extends Activity {

    private ControladorEncontroRecorrente controladorEncontroRecorrente;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mItensMenuTitles;
    private TypedArray mItensImageIcons;

    private ArrayList<NavDrawerItem> mNavDrawerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);

        controladorEncontroRecorrente = ControladorEncontroRecorrente.getInstance(getApplicationContext());

        mItensImageIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        mItensMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

    }

    public void insertItemsToMenu(){
        mNavDrawerItems = new ArrayList<NavDrawerItem>();

        for (int i = 0; i < mItensMenuTitles.length; i++){
            mNavDrawerItems.add(new NavDrawerItem(mItensMenuTitles[i], mItensImageIcons.getResourceId(i, -1)));
        }

    }

    public void set() {
        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        insertItemsToMenu();

        mDrawerList.setAdapter(new NavDrawerListAdapter(getApplicationContext(), mNavDrawerItems));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        selectItem(999);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);

        Intent i = new Intent();
        switch (position) {
            case 0:
                i.setClass(getApplicationContext(), NovoEncontroActivity.class);
                startActivity(i);
                break;
            case 1:
                i.setClass(getApplicationContext(), EncontrosRecorrentesActivity.class);
                startActivity(i);
                break;
            case 2:
                i.setClass(getApplicationContext(), MapActivity.class);
                startActivity(i);
                break;
            case 3:
                i.setClass(getApplicationContext(), TodosEncontrosActivity.class);
                startActivity(i);
                break;
            case 4:
                i.setClass(getApplicationContext(), AjudaActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerList.getCheckedItemPosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int oldSelected = mDrawerList.getCheckedItemPosition();
        mDrawerList.setItemChecked(oldSelected, false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


}