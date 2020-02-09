package edu.csumb.flightapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.User;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("LoginActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username);
                EditText password = findViewById(R.id.password);

                Log.d("LoginActivity", getIntent().getStringExtra("type"));

                if (username.getText().toString().equals("!admiM2") &&
                        password.getText().toString().equals("!admiM2") &&
                                getIntent().getStringExtra("type").equals("manage")) {
                    // special admin userid
                    MainActivity.username = username.getText().toString();
                    MainActivity.instance.verify_admin_login();
                    finish();
                    return;
                }

                String name = username.getText().toString();
                String pw = password.getText().toString();

                FlightDao dao = FlightRoom.getFlightRoom(LoginActivity.this).dao();
                User user = dao.login(name, pw);
                if (user == null) {
                    // unsuccessful login
                    TextView msg = findViewById(R.id.message);
                    msg.setText("User name or password is invalid.");

                } else {
                    // successful login
                    MainActivity.username = username.getText().toString();

                    Log.d("LoginActivity", getIntent().getStringExtra("type"));
                    if (getIntent().getStringExtra("type").equals("reservation")) {
                        ReservationActivity.instance.verify_login();
                    } else if (getIntent().getStringExtra("type").equals("delete")) {
                        DeleteReservationActivity.instance.verify_login();
                    } else {
                        Log.d("LoginActivity", "Logged in Success normal!");
                    }

                    finish();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        Log.d("LoginActivity", "Backed out!");
        if (getIntent().getStringExtra("type").equals("delete")) {
            finish();
            DeleteReservationActivity.instance.finish();
        }
        finish();
    }

}
