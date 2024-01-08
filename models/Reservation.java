package models;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import interfaces.TimeSlot;

public class Reservation implements TimeSlot, Comparable<Reservation>{
   
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Group group;
    private double totalReservationTime;
    private List<Reservation> allReservations = new ArrayList<Reservation>();

    public Reservation(LocalDateTime startDate, LocalDateTime endDate, Group group) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.group = group;
    }


    public boolean overlaps(TimeSlot other) {
        if (startDate.isBefore(other.getStartDate())
            && endDate.isAfter(other.getStartDate())
            && endDate.isBefore(other.getEndDate())) {
            return true;
        }
        else if (startDate.isAfter(other.getStartDate())
                && startDate.isBefore(other.getEndDate())
                && endDate.isAfter(other.getEndDate())) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean covers(TimeSlot other) {
        if (startDate.isBefore(other.getStartDate())
            && endDate.isAfter(other.getEndDate())) {
            return true;
        }
        else if (startDate.isAfter(other.getStartDate())
                && endDate.isBefore(other.getEndDate())) {
            return true;
        }
        else {
            return false;
        }
    }

    public LocalDateTime getStartDate() {
        return this.startDate;
    }

    public LocalDateTime getEndDate() {
        return this.endDate;
    }

    public boolean isAvailable() {
        return false;
    }

    public double getTotalReservationTime() {
        return this.totalReservationTime = this.startDate.until(this.endDate, ChronoUnit.MINUTES);
    }

    @Override
    public String toString() {
        return "Start time:" + this.startDate + "End time:" + this.endDate;
    }

    public void approve() {
        group.addTime(startDate.until(endDate, java.time.temporal.ChronoUnit.MINUTES));
    }

    public int compareTo(Reservation other) {
        return group.compareTo(other.group);
    }

}