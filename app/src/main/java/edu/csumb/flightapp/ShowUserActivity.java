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
import edu.csumb.flightapp.model.FlightRoom;
import edu.csumb.flightapp.model.User;

public class ShowUserActivity extends AppCompatActivity {

    private ShowuserAdapter adapter;

    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("ShowUserActivity", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button return_main_button = findViewById(R.id.return_button);
        return_main_button.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("ShowUserActivity", "onClick return called");
                finish();
            }
        });

        // retrieve all users from database
        users = FlightRoom.getFlightRoom(this).dao().getAllUsers();


        // create view for listing of all users
        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager( new LinearLayoutManager(this));
        adapter = new ShowuserAdapter();
        rv.setAdapter(adapter);
    }

    private class ShowuserAdapter  extends RecyclerView.Adapter<ItemHolder> {

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(ShowUserActivity.this);
            return new ItemHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position){
            holder.bind(users.get(position));
        }

        @Override
        public int getItemCount() { return users.size(); }
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item, parent, false));
        }

        public void bind(User user) {
            TextView item = itemView.findViewById(R.id.item_id);
            item.setText(user.getUsername());


        }
    }
}
