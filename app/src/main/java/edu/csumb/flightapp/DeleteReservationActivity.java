package edu.csumb.flightapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;
import edu.csumb.flightapp.model.Reservation;

public class DeleteReservationActivity extends AppCompatActivity {

    public static final String TAG = "DelResActivity";

    public static DeleteReservationActivity instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        Log.d(TAG, "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView lv = findViewById(R.id.reservations);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                Log.d(TAG, selectedItem);
                String res_id = selectedItem.split("-")[0].replace(" ", "").replace("R","");
                Log.d(TAG, res_id);

                Log.d("ReservationActivity", "Deleting reservation");

                confirm_delete(Integer.parseInt(res_id));
            }
        });

        getLogin();
    }

    public void getLogin() {
        Intent intent = new Intent(DeleteReservationActivity.this, LoginActivity.class);
        intent.putExtra("type", "delete");
        startActivity(intent);
    }

    public void verify_login() {
        Log.d(TAG, "Login verified!");
        Log.d(TAG, "User > " + MainActivity.username);
        load_reservations();
    }

    public void load_reservations() {
        FlightDao dao = FlightRoom.getFlightRoom(DeleteReservationActivity.this).dao();
        List<Reservation> results = dao.getReservations(MainActivity.username);

        Log.d(TAG, "Results Length: " + results.size());

        ListView lv = findViewById(R.id.reservations);

        List<String> rows = new ArrayList<>();

        for (Reservation reservation : results) {
            rows.add("R" + reservation.getId() + " - " + reservation.getFlight_num() + " - " +
                    reservation.getTickets() + " tickets" + " - $" + reservation.getPrice());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (instance, android.R.layout.simple_list_item_1, rows);

        lv.setAdapter(arrayAdapter);
    }

    public void confirm_delete(final int id) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmation");
        alert.setMessage("Are you sure you wanna delete reservation " + id + "?");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                delete(id);
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public void delete(int id) {
        Log.d(TAG, "Deleting " + id + " reservation for " + MainActivity.username);

        FlightDao dao = FlightRoom.getFlightRoom(DeleteReservationActivity.this).dao();

        Reservation reservation = dao.getReservation(id).get(0);
        int quantity =  reservation.getTickets();

        Flight flight = dao.getFlight(reservation.getFlight_num()).get(0);
        flight.setAvailableSeats(flight.getAvailableSeats() + quantity);

        LogRecord record = new LogRecord(new Date(), LogRecord.TYPE_CANCEL, MainActivity.username,
                "canceled reservation " + id);

        dao.addLog(record);
        dao.updateFlight(flight);
        dao.deleteReservation(id);

        load_reservations();
    }

}
