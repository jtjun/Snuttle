import java.util.Random;

public class Generator {
    private ArrayList<Guest> guests;

    public Generator(int n){
        Random generator = new Random();
        guests = new ArrayList<>();
        for(int i = 0; i < n; i++){
            int timeS = generator.nextInt(Simulator.MAX_TIME);
            int timeD = generator.nextInt(Simulator.MAX_TIME);
            if(timeS>timeD){
                int tmp = timeS;
                timeS = timeD;
                timeD = tmp;
            }
            
            guests.add(new Guest(timeS,null,timeD,null));
        }
    }
}
