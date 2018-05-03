<<<<<<< HEAD
=======
import java.util.Random;

>>>>>>> a7a914a20f1150a1605c2442798e9fcf1d7cb64c
public class Generator {
    private ArrayList<Guest> guests;

    public Generator(int n, Map map){
        Random generator = new Random();
        guests = new ArrayList<>();

        // Case 1: All Random
        for(int i = 0; i < n; i++){
            int timeS = generator.nextInt(Simulator.MAX_TIME);
            int timeD = generator.nextInt(Simulator.MAX_TIME);
            if(timeS>timeD){
                int tmp = timeS;
                timeS = timeD;
                timeD = tmp;
            }

            int s = generator.nextInt(Simulator.MAX_STATION), d = generator.nextInt(Simulator.MAX_STATION);
            while(s == d){
                d = generator.nextInt(Simulator.MAX_STATION);
            }

            Station placeS = map.getStation(s);
            Station placeD = map.getStation(d);
            
            
            guests.add(new Guest(timeS,placeS,timeD,placeD));
        }
    }
}
