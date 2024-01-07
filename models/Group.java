package models;
import java.util.ArrayList;
import java.util.List;

public class Group implements Comparable<Group>{
    private String ID;
    public double timeReserved;
    private int numberOfMembers;
    private List<String> memberIDs = new ArrayList<String>(numberOfMembers);

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

    public String getID() {
        return this.ID;
    }

    public void addTime(double d) {
        this.timeReserved = this.timeReserved + d;
    }

    public void clearTime() {
        this.timeReserved = 0;
    }

}
