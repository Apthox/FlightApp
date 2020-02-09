package edu.csumb.flightapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.csumb.flightapp.model.FlightRoom;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    public static String username = null;   // username if logged in
    public static MainActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        Log.d("MainActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // check database
        FlightRoom.getFlightRoom(MainActivity.this).loadData(this);

        Button create_account_button = findViewById(R.id.create_account);

        create_account_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // call the ShowUser Activity
                Log.d("MainActivity", "onClick for create account called");
                Intent intent = new Intent(MainActivity.this,
                        CreateAccountActivity.class);
                startActivity(intent);

            }
        });

        Button reservation_button = findViewById(R.id.reserve);

        reservation_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // call the ShowUser Activity
                Log.d("MainActivity", "onClick for reservation called");
                Intent intent = new Intent(MainActivity.this,
                        ReservationActivity.class);
                startActivity(intent);

            }
        });

        Button user_button = findViewById(R.id.show_users);

        user_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // call the ShowUser Activity
                Log.d("MainActivity", "onClick for show users called");
                Intent intent = new Intent(MainActivity.this,
                        ShowUserActivity.class);
                startActivity(intent);

            }
        });

        Button flight_button = findViewById(R.id.show_flights);

        flight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the ShowFlight activity
                Log.d("MainActivity", "onClick for show flights called");
                Intent intent = new Intent(MainActivity.this,
                        ShowFlightActivity.class);
                startActivity(intent);
            }
        });

        flight_button.setVisibility(View.GONE);

        Button search_flight_button = findViewById(R.id.search_flights);

        search_flight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the ShowFlight activity
                Log.d("MainActivity", "onClick for search flights called");
                Intent intent = new Intent(MainActivity.this,
                        SearchActivity.class);
                startActivity(intent);
            }
        });

        search_flight_button.setVisibility(View.GONE);

        Button view_log_button = findViewById(R.id.log);

        view_log_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the ShowFlight activity
                Log.d("MainActivity", "onClick for view logs called");
                Intent intent = new Intent(MainActivity.this,
                        ViewLogActivity.class);
                startActivity(intent);
            }
        });

        Button delete_reserve_button = findViewById(R.id.delete_reserve);

        delete_reserve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the Delete Reservation activity
                Log.d("MainActivity", "onClick for delete reservation called");
                Intent intent = new Intent(MainActivity.this,
                        DeleteReservationActivity.class);
                startActivity(intent);
            }
        });

        Button manage_systems_button = findViewById(R.id.manage);

        manage_systems_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onClick for manage systems called");
                Intent intent = new Intent(MainActivity.this,
                        LoginActivity.class);
                intent.putExtra("type", "manage");
                startActivity(intent);
            }
        });

        Button add_flight_button = findViewById(R.id.add_flight);

        add_flight_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "onClick for add flight called");
                Intent intent = new Intent(MainActivity.this,
                        AddFlightActvity.class);
                startActivity(intent);
            }
        });

        if (!(username != null && username.equals("!admiM2"))) {
            view_log_button.setVisibility(View.GONE);
            user_button.setVisibility(View.GONE);
            add_flight_button.setVisibility(View.GONE);
        }
    }

    public void verify_admin_login() {
        Button view_log_button = findViewById(R.id.log);
        Button user_button = findViewById(R.id.show_users);
        Button add_flight_button = findViewById(R.id.add_flight);
        Button manage_systems_button = findViewById(R.id.manage);

        view_log_button.setVisibility(View.VISIBLE);
        user_button.setVisibility(View.VISIBLE);
        add_flight_button.setVisibility(View.VISIBLE);
        manage_systems_button.setVisibility(View.GONE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
