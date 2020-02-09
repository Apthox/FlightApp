package edu.csumb.flightapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;

public class AddFlightActvity extends AppCompatActivity {

    public static final String TAG = "AddFlightActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addflight);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button submit_button = findViewById(R.id.submit);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "submit clicked");
                check_inputs();

            }
        });

    }

    public void alert(String error) {
        Log.d(TAG, "alerting error");
        AlertDialog.Builder builder = new AlertDialog.Builder(AddFlightActvity.this);
        builder.setTitle("Error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setMessage(error);
        dialog.show();
    }

    public void check_inputs() {
        Log.d(TAG, "checking inputs");
        EditText flight_num = findViewById(R.id.flight_num);
        EditText departure = findViewById(R.id.departure);
        EditText arrival = findViewById(R.id.arrival);
        EditText depart_time = findViewById(R.id.departure_time);
        EditText capacity = findViewById(R.id.capacity);
        EditText price = findViewById(R.id.price);

        FlightDao dao = FlightRoom.getFlightRoom(AddFlightActvity.this).dao();

        String flight_num_s = flight_num.getText().toString();


        if (flight_num_s.equals("")) {
            alert("No flight number entered");
            return;
        } else if (dao.getFlight(flight_num_s).size() > 0) {
            alert("Flight number is taken");
            return;
        }

        String departure_s = departure.getText().toString();

        if (departure_s.equals("")) {
            alert("No depature entered");
            return;
        }

        String arrival_s = arrival.getText().toString();

        if (arrival_s.equals("")) {
            alert("No arrival entered");
            return;
        }

        String depart_time_s = depart_time.getText().toString();

        if (depart_time_s.equals("")) {
            alert("No depature time entered");
            return;
        }

        int cap = Integer.parseInt(capacity.getText().toString());
        int cost = Integer.parseInt(price.getText().toString());

        Flight flight = new Flight(flight_num_s, departure_s, arrival_s, depart_time_s,
                cap, cost);

        dao.addFlight(flight);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddFlightActvity.this);
        builder.setTitle("Success");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setMessage("Flight has been added!");
        dialog.show();

    }

}
