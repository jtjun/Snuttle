import java.io.FileNotFoundException;
import java.util.*;

public class Simulator {
    public static int MAX_TIME = 100;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;
    public static int shutn = 10;

    public Simulator() throws FileNotFoundException{
        int userN = 1000;
        Map map = new Map("stations.csv", "distance.csv");

        Generator generator = new Generator(userN, map, "AR"); // Generate userN guests for this map
        ArrayList<Guest> guests = generator.getGuests();

        Shuttle[] shuttles = new Shuttle[shutn];
        SetCircularSchedule.setCircularSchedule(shuttles,map); // making "shuttle - schedule"

        for(int i = 0; i < shuttles.length; i++){
            System.out.println(i +" th "+ (shuttles[i].getSchedule().toString()));
        }

        ActualDrive AD = new ActualDrive(shuttles, guests, map.getNumStations());
        AD.Simulate(MAX_TIME);
    }

    public static void main(String[] args){
        try{ Simulator simulator = new Simulator();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
