package models;

import java.util.Set;

import interfaces.Identifiable;

public class Group implements Comparable<Group>, Identifiable<String>{
    private String ID;
    public double timeReserved;
    private int numberOfMembers;
    private Set<String> memberIDs;

    public Group(String ID, Set<String> memberIDs) {
        this.ID = ID;
        this.memberIDs = memberIDs;
        this.numberOfMembers = memberIDs.size();
        this.timeReserved = 0;
    }

    public String getIdentifier() {
        return ID;
    }

    public String toString() {
        return "Group " + ID;
    }

    public int compareTo(Group other) {
        if ((this.timeReserved < other.timeReserved)
            || (this.timeReserved == other.timeReserved
            && this.numberOfMembers > other.numberOfMembers)) {
            return 1;
        }
        else if ((this.timeReserved > other.timeReserved)
                || (this.timeReserved == other.timeReserved
                && this.numberOfMembers < other.numberOfMembers)) {
            return -1;
        }
        else {
            return 0;
        }
    }

    public void addTime(double d) {
        this.timeReserved += d;
    }

    public void clearTime() {
        this.timeReserved = 0;
    }

}
