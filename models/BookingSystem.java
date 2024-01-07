package models;

import java.util.*;

public class BookingSystem {
    private List<Room> rooms;
    private List<Group> allGroups;
    private Map<String, List<Group>> groups;
    private Map<Room, List<Reservation>> requestedReservations;

    public void bookInAdvance() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Please write your group number:");
        String groupID = myObj.nextLine();

        for (Group group : allGroups) {
            if (groupID.equals(group.getID())) {
                
            }
        }
        
    }


}
