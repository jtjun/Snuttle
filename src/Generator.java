import java.util.*;

public class Generator {
    private ArrayList<Guest> guests;

    public Generator(int n, Map map){
        Random generator = new Random();
        guests = new ArrayList<>();

        // Case 1: All Random
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
            int s = getRandomStation(0.0, 0, Simulator.MAX_STATION);
            int d = getRandomStation(0.0, 0, Simulator.MAX_STATION);
            while(s == d){
                d = getRandomStation(0.0, 0, Simulator.MAX_STATION);
            }

            Station placeS = map.getStation(s);
            Station placeD = map.getStation(d);
            
            guests.add(new Guest(timeS, placeS, timeD, placeD));
        }

        // Case 2: K hotspots
        int k = Simulator.MAX_STATION/3; // It can be set with other value

        // Make array for random sampling
        ArrayList<Integer> ss = new ArrayList<>();
        for(int i = 0; i < Simulator.MAX_STATION; i++) ss.add(i);
        Collections.shuffle(ss);
        
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
            int s = getRandomStation(Simulator.K_RATIO, k, Simulator.MAX_STATION);
            int d = getRandomStation(Simulator.K_RATIO, k, Simulator.MAX_STATION);
            while(s == d){
                d = getRandomStation(Simulator.K_RATIO, k, Simulator.MAX_STATION);
            }

            Station placeS = map.getStation(ss.get(s));
            Station placeD = map.getStation(ss.get(d));

            guests.add(new Guest(timeS, placeS, timeD, placeD));
        }
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
}
