package edu.csumb.flightapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightDao;
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.LogRecord;

public class ViewLogActivity  extends AppCompatActivity {

    private Adapter adapter;
    private List<LogRecord> records;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ViewLogActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_log);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ViewLogActivity", "onClick return called");
                finish();
            }
        });

        //TODO  use a RecylerView to display LogRecords
        //   define adapter and item_holder classes

        // retrieve all log records from database
        FlightDao dao = FlightRoom.getFlightRoom(ViewLogActivity.this).dao();
        records = dao.getRecords();

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager( new LinearLayoutManager(this));
        adapter = new Adapter();
        rv.setAdapter( adapter );
    }

    private class Adapter extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(ViewLogActivity.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position){
            holder.bind(records.get(position));
        }

        @Override
        public int getItemCount() { return records.size(); }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item, parent, false));
        }

        public void bind(LogRecord r ) {
            TextView item = itemView.findViewById(R.id.item_id);
            item.setText(r.toString());
        }
    }
}
