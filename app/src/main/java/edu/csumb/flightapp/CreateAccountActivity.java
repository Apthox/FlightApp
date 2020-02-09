package edu.csumb.flightapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.room.Dao;

import java.util.Date;

import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;
import edu.csumb.flightapp.model.User;

public class CreateAccountActivity  extends AppCompatActivity {

    public static boolean is_valid_username(String username) {
        return (username.length() > 3);
    }

    public static boolean is_valid_password(String password) {

        boolean has_special = false, has_lower = false, has_upper = false, has_digit = false;
        for (char c : password.toCharArray()) {
            if ( Character.isDigit(c) ) {
                has_digit = true;
            } else if ( c == '!' || c == '@' || c == '#' || c == '$' ) {
                has_special = true;
            } else if ( Character.toUpperCase(c) == c ) {
                has_upper = true;
            } else if ( Character.toLowerCase(c) == c ) {
                has_lower = true;
            }
        }

        return (has_digit && has_special && has_lower && has_upper);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("CrateAccountActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button create_button = findViewById(R.id.create_account_button);
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = findViewById(R.id.username);
                EditText password = findViewById(R.id.password);

                boolean valid_user = is_valid_username(username.getText().toString());
                boolean valid_pass = is_valid_password(password.getText().toString());

                if (!valid_user) {
                    TextView msg = findViewById(R.id.message);
                    msg.setText("Username not valid.");
                    return;
                }

                if (!valid_pass) {
                    TextView msg = findViewById(R.id.message);
                    msg.setText("Password not valid.");
                    return;
                }

                if (username.getText().toString().equals("!admiM2")) {
                    // username already exists.
                    TextView msg = findViewById(R.id.message);
                    msg.setText("Username not available.");
                    return;
                }

                User user = FlightRoom.getFlightRoom(CreateAccountActivity.this).
                        dao().getUserByName(username.getText().toString());

                if (user == null) {
                    // username does not exist, so add the new account
                    String name = username.getText().toString();
                    String pw = password.getText().toString();

                    User newUser = new User(name, pw);
                    FlightDao dao = FlightRoom.getFlightRoom(CreateAccountActivity.this).dao();
                    dao.addUser(newUser);

                    LogRecord log = new LogRecord(new Date(), LogRecord.TYPE_NEW_ACCOUNT, name, name + " account has been created!");
                    dao.addLog(log);

                    // inform user that new account has been created
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateAccountActivity.this);
                    builder.setTitle("Account successfully created.");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    // username already exists.
                    TextView msg = findViewById(R.id.message);
                    msg.setText("Username not available.");
                    return;
                }

            }
        });
    }
}