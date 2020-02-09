package edu.csumb.flightapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.csumb.flightapp.model.Flight;
import edu.csumb.flightapp.model.FlightRoom;

public class SearchActivity extends AppCompatActivity {

    List<Flight> flights = new ArrayList<Flight>();
    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SearchActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flight);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("SearchActivity", "onClick return called");
                finish();
            }
        });

        Button search_button = findViewById(R.id.search_button);
        search_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("SearchActivity", "onClick search called");
                EditText from = findViewById(R.id.from_city);
                EditText to = findViewById(R.id.to_city);
                flights = FlightRoom.getFlightRoom(SearchActivity.this).dao().
                        searchFlight(from.getText().toString(),
                                     to.getText().toString());
                // notify recycler view that list of flights has changed
                adapter.notifyDataSetChanged();

            }
        });

        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager( new LinearLayoutManager(this));
        adapter = new Adapter();
        rv.setAdapter( adapter );

    }

    private class Adapter  extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(SearchActivity.this);
            return new  ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position){
            holder.bind(flights.get(position));
        }

        @Override
        public int getItemCount() { return flights.size(); }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item, parent, false));
        }

        public void bind(Flight f ) {
            TextView item = itemView.findViewById(R.id.item_id);
            item.setText(f.toString());
        }
    }
}