package edu.csumb.flightapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;
import edu.csumb.flightapp.model.Reservation;

public class ReservationActivity extends AppCompatActivity {

    List<Flight> flights = new ArrayList<Flight>();
    Flight flight = null;
    int quantity = 0;
    long id = -1;

    public static ReservationActivity instance = null;

    public void verify_login() {
        addReservation();
        confirm_reservation();
    }

    public void addReservation() {
        Reservation reservation = new Reservation(new Date(), MainActivity.username,
                flight.getFlightNo(), quantity, flight.getPrice() * quantity);

        FlightDao dao = FlightRoom.getFlightRoom(ReservationActivity.this).dao();
        id = dao.addReservation(reservation);

        flight.setAvailableSeats(flight.getAvailableSeats() - quantity);
        dao.updateFlight(flight);

    }

    public void confirm_reservation() {

        String msg = "Reservation Number: " + id + "\n" +
                "\n" +
                "User: " + MainActivity.username + "\n" +
                "Flight no: " + flight.getFlightNo() + "\n" +
                "Departing: " + flight.getDeparture() + " at " + flight.getDepartureTime() + "\n" +
                "Arriving: " + flight.getArrival() + "\n" +
                "Tickets: " + quantity + "\n" +
                "Price: $" + (quantity * flight.getPrice());

        Log.d("ReservationActivity", "Successful Login!");
        new AlertDialog.Builder(this)
                .setTitle("Confirm Reservation")
                .setMessage(msg)
                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        record_reservation();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        cancel_reservation();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel_reservation();
                    }
                }).show();
    }

    public void getLogin() {
        Intent intent = new Intent(ReservationActivity.this, LoginActivity.class);
        intent.putExtra("type", "reservation");
        startActivity(intent);
    }

    public void cancel_reservation() {
        FlightDao dao = FlightRoom.getFlightRoom(ReservationActivity.this).dao();

        int id = (int) this.id;
        Reservation reservation = dao.getReservation(id).get(0);
        int quantity =  reservation.getTickets();

        Flight flight = dao.getFlight(reservation.getFlight_num()).get(0);
        flight.setAvailableSeats(flight.getAvailableSeats() + quantity);

        dao.updateFlight(flight);
        dao.deleteReservation(id);
    }

    public void record_reservation() {
        Log.d("ReservationActivity", "Successful Reservation");

        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("Reservation successfully created.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        FlightDao dao = FlightRoom.getFlightRoom(ReservationActivity.this).dao();
        LogRecord record = new LogRecord(new Date(), LogRecord.TYPE_RESERVATION,
                MainActivity.username, "Reservation for " + flight.getFlightNo());
        dao.addLog(record);

    }

    public void getQuantity() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Order Information");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setRawInputType(Configuration.KEYBOARD_12KEY);
        alert.setView(input);
        alert.setMessage("Specify Quantity.");
        alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                quantity = Integer.parseInt(input.getText().toString());
                postQuantity();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                finish();
            }
        });
        alert.show();
    }

    public void postQuantity() {
        if (quantity > 7) {
            show_alert("Error", "Cannot purchase more then 7 tickets!");
        } else if (flight.getAvailableSeats() < quantity) {
            show_alert("Error", "Cannot purchase more tickets then available!");
        } else {
            getLogin();
        }
    }

    public void show_alert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setNegativeButton(
                "Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setTitle(title);
        alert.show();
    }

    public void updateList() {

        ListView lv = findViewById(R.id.flights);

        List<String> rows = new ArrayList<>();

        for (Flight flight : flights) {
            rows.add(flight.getFlightNo() + " - " + flight.getDepartureTime() + " - $" +
                    flight.getPrice() + " - " + flight.getAvailableSeats());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (instance, android.R.layout.simple_list_item_1, rows);

        lv.setAdapter(arrayAdapter);
    }

    public void selectFlight(String flight_num) {
        flight = FlightRoom.getFlightRoom(ReservationActivity.this).dao().
                getFlight(flight_num).get(0);
        getQuantity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        instance = this;

        Log.d("ReservationActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ReservationActivity", "onClick return called");
                finish();
            }
        });

        ListView lv = findViewById(R.id.flights);

        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ReservationActivity", "onClick search called");
                EditText from = findViewById(R.id.from_city);
                EditText to = findViewById(R.id.to_city);
                flights = FlightRoom.getFlightRoom(ReservationActivity.this).dao().
                        searchFlight(from.getText().toString(),
                                to.getText().toString());

                updateList();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, new ArrayList<String>());

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item text from ListView
                String selectedItem = (String) parent.getItemAtPosition(position);

                // Display the selected item text on TextView
                Log.d("ReservationActivity", selectedItem);
                String flight_num = selectedItem.split("-")[0].replace(" ",
                        "");
                Log.d("ReservationActivity", flight_num);

                Log.d("ReservationActivity", "Selecting Flight");

                selectFlight(flight_num);
            }
        });

    }

}
