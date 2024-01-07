package models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import interfaces.Identifiable;
import interfaces.TimeSlot;

public class Room implements Identifiable<String>{
    private String id;
    private List<TimeSlot> calendar = new ArrayList<>();

    public Room(String id) {
        this.id = id;
    }

    public void addTimeSlot(TimeSlot timeSlot) {
        calendar.add(timeSlot);
    }

    public String getIdentifier() {
        return id;
    }

    @Override
    public String toString() {
        return "" + calendar;
    }

    public boolean available(TimeSlot t) {
        for (TimeSlot timeSlot : calendar) {
            if (timeSlot == t) {
                return false;
            }
        }
        return true;
    }

    public void book(Reservation r) {
        calendar.add(r);
    }

    public String toString(LocalDate date, Room room) {
        return "Room ID:" + room.getIdentifier() + "Room calendar:" + calendar;
    }
}
