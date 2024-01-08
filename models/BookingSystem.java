package models;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.*;

import interfaces.Identifiable;

public class BookingSystem {
    private List<Room> rooms;
    private Map<String, List<Group>> groups;
    private Map<Room, PriorityQueue<Reservation>> requestedReservations;

    public BookingSystem() {
        rooms = new ArrayList<Room>();
        groups = new HashMap<String, List<Group>>();
        requestedReservations = new HashMap<Room, PriorityQueue<Reservation>>();
    }

    public void bookInAdvance() {
        String courseID = getId("course");
        String groupID = getId("group");
        String roomID = getId("room");
        Room r = (Room) getElement(rooms, roomID);
        Group g = (Group) getElement(groups.get(courseID), groupID);

        LocalDateTime start = getDate("start");
        LocalDateTime end = getDate("end");

        Reservation reservation = new Reservation(start, end, g);

        PriorityQueue<Reservation> pq = requestedReservations.get(r);

        if (pq == null) {
            pq = new PriorityQueue<Reservation>();
        }
        pq.add(reservation);
        requestedReservations.put(r, pq);
       
    }

    public static String getId(String type) {
        System.out.println("Enter the id of the "+type);
        Scanner input = new Scanner(System.in);
        return input.nextLine();        
    }

    public static<T> Identifiable<T> getElement(List<? extends Identifiable<T>> list, T id) {
        for (Identifiable i : list) {
            if (i.getIdentifier().equals(id)) {
                return i;
            }
        }
        return null;
    }

    public static LocalDateTime getDate(String type) {
        System.out.println("Enter the "+type+"date");
        Scanner input = new Scanner(System.in);
        return LocalDateTime.parse(input.nextLine());        
    }

    public void showCalendars() {
        for (Room r : rooms) {
            System.out.println(r);
        }
    }

    public void handleRequests() {
        for (Room r : requestedReservations.keySet()) {
            PriorityQueue<Reservation> pq = requestedReservations.get(r);
            while (pq.size()>0) {
                Reservation reservation = pq.poll();
                if (r.available(reservation)) {
                    r.book(reservation);
                    reservation.approve(); 
                }
            }
        }
    }

    public void loadState() {
        try {
            loadRooms("input\\rooms.csv");
            loadGroups("input\\groups.csv");
            loadReservations("input\\reservations.csv");
        } catch (FileNotFoundException e) {
            System.out.println("It was not possible to load file " +e.getMessage());
        } catch (NoSuchElementException e) {
            System.out.println("The system does not follow the expected format");
        }
    }

    public void loadRooms(String fileName) throws FileNotFoundException, NoSuchElementException {
        FileReader fr = new FileReader(fileName);
        Scanner s = new Scanner(fr);
        while (s.hasNextLine()) {
            String roomID = s.nextLine();
            Room r = new Room(roomID);
            rooms.add(r);
        }
    }

    public void loadGroups (String fileName) throws FileNotFoundException, NoSuchElementException {
        FileReader fr = new FileReader(fileName);
        Scanner s = new Scanner(fr);
        while (s.hasNextLine()) {
            String line = s.nextLine();
            StringTokenizer st = new StringTokenizer(line, ",");
            String courseID = st.nextToken();
            String groupID = st.nextToken();
            int numMembers = Integer.parseInt(st.nextToken());
            Set<String> set = new HashSet<String>();
            for (int i=0; i<numMembers; i++) {
                String member = s.nextLine();
                st = new StringTokenizer(member, ",");
                String memberID = st.nextToken();
                set.add(memberID);
            }

            Group g = new Group(groupID, set);
            List<Group> gs = groups.get(courseID);
            if (gs == null) {
                gs = new ArrayList<Group>();
            }
            gs.add(g);
            groups.put(courseID, gs);
        }
    }

    public void loadReservations(String fileName) throws FileNotFoundException, NoSuchElementException{
        FileReader fr = new FileReader(fileName);
        Scanner s = new Scanner(fr);        
        while(s.hasNextLine()) {
            String line = s.nextLine();
            StringTokenizer st = new StringTokenizer(line, ",");
            String roomID = st.nextToken();
            String courseID = st.nextToken();
            String groupID = st.nextToken();
            LocalDateTime start = LocalDateTime.parse(st.nextToken());
            LocalDateTime end = LocalDateTime.parse(st.nextToken());

            Room r = (Room) getElement(rooms, roomID);
            List<Group> gs = groups.get(courseID);

            Group g = (Group) getElement(gs, groupID);

            Reservation reservation = new Reservation(start, end, g);
            r.book(reservation);

        }
    }

    public void startNewReservationPeriod() {
        for (String courseID : groups.keySet()) {
            List<Group> gs = groups.get(courseID);
            for(Group group : gs) {
            group.clearTime();
            }
        }
    }

    public void addGroup() {
        String courseId = getId("course");
        String groupId = getId("group");
        int num = getInt("number of members: ");
        Set<String> memberIDs = new HashSet<String>();
        for (int i = 0; i < num; i++) {
            String id = getId("student " + (i+1));
            memberIDs.add(id);
        }
        List<Group> gs = groups.get(courseId);
        if (gs == null) {
            gs = new ArrayList<Group>();
        }
        gs.add(new Group(groupId, memberIDs));

    }

    public boolean getAndHandleOption() {
        System.out.println("Menu\n");
        String[] options = { "Load state", "Start new reservation period", 
          "Add group", "Show calendars", "Book in advance", "Handle requests", "Exit" };
        int i = 1;
        for (String str : options) {
            System.out.println((i++)+". "+str+"\n");
        }
        
        int opt = getInt("Your choice: ");
        return handle(opt);    
    }

    public boolean handle(int option) {
        boolean running = true;
        try {
            switch(option) {
                case 1:
                    loadState();
                    break;
                case 2:
                    startNewReservationPeriod();
                    break;
                case 3:
                    addGroup();
                    break;
                case 4:
                    showCalendars();
                    break;
                case 5:
                    bookInAdvance();
                    break;
                case 6:
                    handleRequests();
                    break;            
                case 7:
                    running = false;
            }        
        } catch (Exception e) {
            System.out.println("The requested operation could not be completed");
            System.out.println(e.getMessage());
        }            
        return running;
    }

    public static int getInt(String msg) {
        System.out.println(msg);
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }

    public static void main(String[] args) {
        BookingSystem bs = new BookingSystem();
        boolean interact = true;
        do {
            interact = bs.getAndHandleOption();
        } while (interact);
        
    }
}
