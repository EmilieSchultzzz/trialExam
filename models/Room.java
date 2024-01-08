package models;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import interfaces.Identifiable;
import interfaces.TimeSlot;

public class Room implements Identifiable<String>{
    private String id;
    private Calendar slots;

    public Room(String id) {
        this.id = id;
        slots = new Calendar();
    }

    public String getIdentifier() {
        return id;
    }

    public String toString() {
        return "Room " + id + " with calendar:\n" + slots;
    }

    public boolean available(TimeSlot t) {
            return slots.available(t);
    }

    public void book(Reservation r) {
        slots.add(r);
    }

    public String toString(LocalDate date) {
        return slots.toString(date);
    }

    static class Calendar {
        private Map<LocalDate, List<TimeSlot>> times;

        public Calendar() {
            times = new HashMap<LocalDate, List<TimeSlot>>();
        }
        
        public String toString(LocalDate d) {
            
            List<TimeSlot> ts = times.get(d);
            String str = "";
            
            if (ts == null) {
                str = "9:00 until 17:00 Available";
            } else {
                for (TimeSlot t0 : ts) {
                    str += t0.toString()+"\n";
                }
            }
            
            return str;
        }

        public String toString() {
            Set<LocalDate> ds = times.keySet();
            String str = "";
            
            if (ds.size()==0) {
                str = "Empty calendar";
            }
            
            for (LocalDate d : ds) {
                str += d.toString()+"\n"+toString(d) + "\n\n";
            }
            
            return str;
        }

        public boolean available(TimeSlot t) {
            LocalDate d = t.getStartDate().toLocalDate();
            List<TimeSlot> ts = times.get(d);
            
            if (ts == null) {
                return true;
            }
            
            for (TimeSlot t0 : ts) {
                if (t0.overlaps(t) && !t0.isAvailable()) {
                    return false;
                }
            }
            return true;
        }

        public void add(Reservation r) {
            if (!available(r)) {
                throw new RuntimeException("The room is not available");
            }
            LocalDate d = r.getStartDate().toLocalDate();
            List<TimeSlot> ts = times.get(d);        
            if (ts == null) {
                ts = new ArrayList<TimeSlot>();
                ts.add(new FreeSlot(LocalDateTime.of(d, LocalTime.of(8,0)), 
                       LocalDateTime.of(d, LocalTime.of(17,0))));
            }    
            boolean added = false;            
            for (int i = 0; i < ts.size(); i++) {
                TimeSlot t0 = ts.get(i);
                if (t0.covers(r) && t0.isAvailable()) {
                    if (t0.getStartDate().isBefore(r.getStartDate())) {
                        ts.set(i, new FreeSlot(t0.getStartDate(), r.getStartDate()));
                    }
                    ts.add(i+1, r);
                    if (r.getEndDate().isBefore(t0.getEndDate())) {
                        ts.add(i+2, new FreeSlot(r.getEndDate(), t0.getEndDate()));
                    }
                    added = true;
                    break;
                }
            }            
            if (!added) {
                throw new RuntimeException("The room cannot be booked at those times");
            } else {
                times.put(d, ts);
            }
        }
    }
}
