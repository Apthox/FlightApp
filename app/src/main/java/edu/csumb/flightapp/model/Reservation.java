package edu.csumb.flightapp.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Reservation {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @NonNull
    private long time;
    @NonNull
    private String username;
    @NonNull
    private String flight_num;
    @NonNull
    private int tickets;
    @NonNull
    private double price;

    public Reservation() {}

    @Ignore
    public Reservation(Date datetime, String username, String flight_num, int tickets,
                       double price) {
        this.time = datetime.getTime();
        this.username = username;
        this.flight_num = flight_num;
        this.tickets = tickets;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public java.util.Date getDatetime() {
        return new java.util.Date(time);
    }

    public long getTime() { return time;}

    public void setTime(long time) { this.time = time; }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    @NonNull
    public String getFlight_num() {
        return flight_num;
    }

    public void setFlight_num(@NonNull String flight_num) {
        this.flight_num = flight_num;
    }

    public int getTickets() {
        return tickets;
    }

    public void setTickets(int tickets) {
        this.tickets = tickets;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
