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
    private double totalReservationTime;
    private String groupID;
    private String roomID;
    private List<Reservation> allReservations = new ArrayList<Reservation>();

    public Reservation(LocalDateTime startDate, LocalDateTime endDate, String groupID, String roomID) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.groupID = groupID;
        this.roomID = roomID;
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
        return true;
    }

    public double getTotalReservationTime() {
        return this.totalReservationTime = this.startDate.until(this.endDate, ChronoUnit.MINUTES);
    }

    @Override
    public String toString() {
        return "Start time:" + this.startDate + "End time:" + this.endDate;
    }

    public void approve(Group group) {
        group.timeReserved = group.timeReserved + this.getTotalReservationTime();
    }

    public int compareTo(Reservation other) {
        List<Group> allGroups = new ArrayList<Group>();
        for (Group group : allGroups) {
            if (this.groupID == group.getID()) {
                for (Group group2 : allGroups) {
                    if (other.groupID == group2.getID()) {
                        if (group.compareTo(group2) == 1) {
                            return 1;
                        }
                        else if (group.compareTo(group2) == -1) {
                            return -1;
                        }
                        else if (group.compareTo(group2) == 0) {
                            return 0;
                        }
                    }
                }
            }
        }
        return 0;
    }

}