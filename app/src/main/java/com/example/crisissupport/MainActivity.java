package com.example.crisissupport;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int HOME_FRAGMENT = 0;
    private static final int CART_FRAGMENT = 1;
    private static final int REWARD_FRAGMENT=4;

    public static Boolean showCart=false;

    private FrameLayout frameLayout;
    private static int currentFragment = -1;

    private NavigationView navigationView;
    private DrawerLayout drawer;

    private ImageView actionBarLogo;
    private Toolbar toolbar;// define toolbar globally

    //To change the status Bar Color
    private Window window;

    private AppBarConfiguration mAppBarConfiguration;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner spinner1 = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence>  adapter=ArrayAdapter.createFromResource(this,R.array.spinner1,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter);

        Spinner spinner2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence>  adapter1=ArrayAdapter.createFromResource(this,R.array.spinner2,android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter1);

        Spinner spinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence>  adapter2=ArrayAdapter.createFromResource(this,R.array.spinner3,android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter2);

        //ToolBar Code
        toolbar = findViewById(R.id.toolbar);
        actionBarLogo = findViewById(R.id.action_bar_logo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        //To change reward fragment app bar color
        window=getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        //Drawer Code
        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true); //this method make as selected state

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_in, R.string.nav_out);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //after  invalidateOptionsMenu() the this method called
        if (currentFragment == HOME_FRAGMENT) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }


    //Header tab menu button customization
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.main_search_icon) {
            //todo:search
            return true;
        } else if (id == R.id.main_notification_icon) {
            //todo:notification System
            return true;
        } else if (id == R.id.main_cart_icon) {
            //todo:Cart

            //Dialog

            final Dialog signInDialog=new Dialog(MainActivity.this);
        //    signInDialog.setContentView(R.layout.sign_in_dialog);

            signInDialog.setCancelable(true); //when user click any screen except dialog button

            signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

            Button dialogSignInBtn=signInDialog.findViewById(R.id.sign_in_btn);
            Button dialogSignUpBtn=signInDialog.findViewById(R.id.sign_up_btn);

            final Intent registerIntent=new Intent(MainActivity.this,RegisterActivity.class);

            dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInDialog.dismiss();
          //          setSignUpFragment = false;
                    startActivity(registerIntent);
                }
            });

            dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signInDialog.dismiss();
            //        setSignUpFragment = true;
                    startActivity(registerIntent);
                }
            });

            signInDialog.show();

            //gotoFragment("My Cart",new MyCartFragment(),CART_FRAGMENT);
            return true;
        }
        else if(id==android.R.id.home){
            if(showCart){
                showCart=false;
                finish();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        // when click back press btn then this method called
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(currentFragment==HOME_FRAGMENT){
                currentFragment=-1; //here when press back button then come back again our mainActivity show to empty to we need reset again this current fragment
                super.onBackPressed();
            }else{
                if(showCart){
                    showCart=false;
                    finish();
                }else{
                    actionBarLogo.setVisibility(View.VISIBLE);
                    invalidateOptionsMenu(); // if again call same thing then Icon visable
              //      setFragment(new MyHomeFragment(), HOME_FRAGMENT);
                    navigationView.getMenu().getItem(0).setChecked(true);
                }
            }
        }
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Handle navigation view item clicks here
        int id = menuItem.getItemId();

        if (id == R.id.nav_my_mall) {
            actionBarLogo.setVisibility(View.VISIBLE);
            invalidateOptionsMenu(); // if again call same thing then Icon visable

        } else if (id == R.id.nav_about) {
            Intent aboutIntent=new Intent(this,AboutActivity.class);
            startActivity(aboutIntent);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}