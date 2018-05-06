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

        // All Random guest situation
        Generator generator = new Generator(userN, map, "AR"); // Generate userN guests for this map
        ArrayList<Guest> guests = new ArrayList<>();
        guests = generator.getGuests();
        Request R = new Request(guests, map);

        Shuttle[] shuttleC = new Shuttle[shutn]; // Circular
        Shuttle[] shuttleE = new Shuttle[shutn]; // Express
        Shuttle[] shuttleG = new Shuttle[shutn]; // Greedy

        //Setting schedule to shuttle
        CircularSchedule.setCircularSchedule(shuttleC, map);
        ExpressSchedule.setExpressSchedule(shuttleE, map, 5);
        GreedySchedule.setGreedySchedule(shuttleG, map, guests, 5);

        // type : Express
        System.out.println("\ntype : Express");
        R.reset();
        ActualDrive Exp = new ActualDrive(shuttleE, R, map, "Express");
        Exp.Simulate();
        System.out.println("Express done");

        // type : Cicular
        System.out.println("\ntype : Circular");
        R.reset();
        ActualDrive Cir = new ActualDrive(shuttleC, R, map, "Circular");
        Cir.Simulate();
        System.out.println("Circular done");

        // type : Greedy
        System.out.println("\ntype : Greedy");
        R.reset();
        ActualDrive Grd = new ActualDrive(shuttleC, R, map, "Greedy");
        Grd.Simulate();
        System.out.println("Greedy done");
    }

    public static void main(String[] args){
        try{ Simulator simulator = new Simulator();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
