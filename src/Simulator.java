import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Simulator {
    public static int MAX_TIME = 1000;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;
    public static int shutn = 10;
    public static int userN;

    public Simulator() throws FileNotFoundException{
        userN = 1000;
        Map map = new Map("stations.csv", "distance.csv");

        Generator generator = new Generator(userN, map, "AR"); // Generate userN guests for this map
        ArrayList<Guest> guests = generator.getGuests();

        Shuttle[] shuttles = new Shuttle[shutn];
        // SetCircularSchedule.setCircularSchedule(shuttles,map); // making "shuttle - schedule"
        GreedySchedule.setGreedySchedule(shuttles, map, guests, 5);

        for(int i = 0; i < shuttles.length; i++){
            System.out.println(i +" th "+ (shuttles[i].getSchedule().toString()));
        }

        ActualDrive AD = new ActualDrive(shuttles, guests, map);
        AD.Simulate();
    }

    public static void main(String[] args){
        try{ Simulator simulator = new Simulator();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
