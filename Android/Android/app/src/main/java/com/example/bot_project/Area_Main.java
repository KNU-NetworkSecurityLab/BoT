package com.example.bot_project;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Area_Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    private long backKeyClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sidemenu);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        switch (getIntent().getIntExtra("number",0)){
            case 1:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new Area01_Fragment()).addToBackStack(null).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new Area02_Fragment()).addToBackStack(null).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new Area03_Fragment()).addToBackStack(null).commit();
                break;
            case 4:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                        new Area04_Fragment()).addToBackStack(null).commit();
                break;

        }


    }


    @Override
    public void onBackPressed(){
      if (System.currentTimeMillis()> backKeyClickTime +2000){
          backKeyClickTime = System.currentTimeMillis();
          Toast.makeText(getApplicationContext(), "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
          return;
      }
      if(System.currentTimeMillis()<=backKeyClickTime+2000){
          ActivityCompat.finishAffinity(this);
      }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.menu_item1){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                    new AddIot_Fragment()).addToBackStack(null).commit();
        }else if(id ==R.id.menu_item2){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                    new Area01_Fragment()).addToBackStack(null).commit();
        }else if(id == R.id.menu_item3){

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                    new Area02_Fragment()).addToBackStack(null).commit();
        }else if(id == R.id.menu_item4) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                    new Area03_Fragment()).addToBackStack(null).commit();
        }else if(id == R.id.menu_item5) {

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                    new Area04_Fragment()).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
