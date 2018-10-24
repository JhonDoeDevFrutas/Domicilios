package jhondoe.com.domicilios.ui.view;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import jhondoe.com.domicilios.R;
import jhondoe.com.domicilios.common.Common;
import jhondoe.com.domicilios.data.model.entities.Token;
import jhondoe.com.domicilios.data.preferences.SessionPrefs;
import jhondoe.com.domicilios.provider.FirebaseReferences;
import jhondoe.com.domicilios.service.ListenOrder;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // UI references.
    TextView txtUser, txtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentCard = new Intent(getBaseContext(), ActivityCart.class);
                startActivity(intentCard);

                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navView = navigationView.getHeaderView(0);
        prepararUI(navView);// Preparar elementos UI


        // Register Service
        Intent service = new Intent(getBaseContext(), ListenOrder.class);
        startService(service);

        //updateToken(FirebaseInstanceId.getInstance().getToken());

        // Call Notification
        if (Common.NOTIFICATION == "request"){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_order_status));
        }
/*
        if (getIntent() == null){
            onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_order_status));
        }
        */
        onBringData(); // Traer Datos
    }

    void prepararUI(View view){
        txtUser = (TextView)view.findViewById(R.id.textUser);
        txtPhone = (TextView)view.findViewById(R.id.textPhone);
    }

    private void onBringData() {
        String phone = SessionPrefs.get(getBaseContext()).getPhone();

        txtPhone.setText(phone);
    }


    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference(FirebaseReferences.TOKENS);
        String phone = SessionPrefs.get(getBaseContext()).getPhone();

        Token data = new Token(token, false); // false because this token send from Client app
        tokens.child(phone).setValue(data);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_refresh){
            return true;
        } else if (id == R.id.action_signOff){
            SessionPrefs.get(getBaseContext()).logOut();

            onClose();
        }

        return super.onOptionsItemSelected(item);
    }

    void onClose(){
        Intent intentLogin = new Intent(this, ActivityRegister.class);
        startActivity(intentLogin);
        finish();
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager manager = getFragmentManager();

        if (id == R.id.nav_store) {
            // Handle the camera action
            manager.beginTransaction().replace(R.id.content_frame, new FragmentStore()).commit();
        } /*else if (id == R.id.nav_cart) {

        } */else if (id == R.id.nav_order_status) {
            manager.beginTransaction().replace(R.id.content_frame, new FragmentOrderSatus()).commit();
        } else if (id == R.id.nav_manage) {
            manager.beginTransaction().replace(R.id.content_frame, new FragmentTools()).commit();
        } else if (id == R.id.nav_log_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
