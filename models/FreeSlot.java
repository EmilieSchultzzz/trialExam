package models;
import java.time.LocalDateTime;

import interfaces.TimeSlot;

public class FreeSlot implements TimeSlot{
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    
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
}
