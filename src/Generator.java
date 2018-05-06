import java.util.*;

public class Generator {
    private ArrayList<Guest> guests;

    public Generator(int n, Map map, String type) {
        if(type.equals("HS")) GeneratorHS(n, map);
        if(type.equals("AR")) GeneratorAR(n, map);
    }

    // Case 1: All Random
    public void GeneratorAR(int n, Map map) {
        Random generator = new Random();
        guests = new ArrayList<>();
        ArrayList<StationGroup> edgelist = new ArrayList<>();

        for(int i = 0; i < map.getNumStations(); i++){
            Station s = map.getStation(i);
            for(int j = 0; j < map.getNumStations(); j++){
                if(i!=j){
                    edgelist.add(new StationGroup(s,map.getStation(j)));
                }
            }
        }
        for (int i = 0; i < n; i++) {
            // Set time with random
            int timeS = generator.nextInt(Simulator.MAX_TIME);
            int timeD = generator.nextInt(Simulator.MAX_TIME);
            if (timeS > timeD) {
                int tmp = timeS;
                timeS = timeD;
                timeD = tmp;
            }
            // Set stations with random excluding same station
            StationGroup sd = edgelist.get(generator.nextInt(edgelist.size()));

            Station placeS = sd.start;
            Station placeD = sd.dest;

            guests.add(new Guest(timeS, placeS, timeD, placeD));
        }
        sortGuests();
    }

    // Case 2: K hotspots
     public void GeneratorHS(int n, Map map) {
        Random generator = new Random();
        int k = Simulator.MAX_STATION*Simulator.MAX_STATION/2/8; // It can be set with other value
        // Make array for random sampling
        ArrayList<StationGroup> edgelist = new ArrayList<>();

        for(int i = 0; i < map.getNumStations(); i++) {
            Station s = map.getStation(i);
            for (int j = 0; j < map.getNumStations(); j++) {
                if (j != i) edgelist.add(new StationGroup(s, map.getStation(j)));
            }
        } Collections.shuffle(edgelist);
        
        for(int i = 0; i < n; i++){
            // Set time with random
            int timeS = generator.nextInt(Simulator.MAX_TIME);
            int timeD = generator.nextInt(Simulator.MAX_TIME);
            if(timeS>timeD){
                int tmp = timeS;
                timeS = timeD;
                timeD = tmp;
            }

            // Set stations with random excluding same station
            StationGroup sd = edgelist.get(getRandomStation(Simulator.K_RATIO, k, edgelist.size()));

            Station placeS = sd.start;
            Station placeD = sd.dest;

            guests.add(new Guest(timeS, placeS, timeD, placeD));
        }
        sortGuests();
    }

    public ArrayList<Guest> getGuests(){ return guests; }
    public ArrayList<Guest> copyGuests(){
        ArrayList<Guest> guestCopy = new ArrayList<>();
        for(Guest guest : guests) {
            guestCopy.add(new Guest(guest.getTimeS(), guest.getPlaceS(), guest.getTimeD(), guest.getPlcaeD()));
        } return guestCopy;
    }

    private int getRandomStation(double r,int k,int m){
        // Produce random number from 0 to m-1 regarding ratio r(higher k makes more pick front items)
        Random generator = new Random();
        if(generator.nextDouble()<r){
            return generator.nextInt(k);
        }else{
            return generator.nextInt(m);
        }
    }

    public void sortGuests(){
        guests.sort( new Comparator<Guest>() {
            @Override
            public int compare(Guest o1, Guest o2) {
                final int t1 = o1.getTimeS();
                final int t2 = o2.getTimeS();
                return Integer.compare(t1, t2);
            }
        }); // sort guests by compare timeS
    }

    class StationGroup{
        public Station start,dest;
        public StationGroup(Station start, Station dest){
            this.start = start;
            this.dest = dest;
        }
    }
}
