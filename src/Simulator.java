import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Simulator {
    public static int MAX_TIME = 2000;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;
    public static int shutn = 10;

    public Simulator() throws FileNotFoundException{
        int userN = 1000;
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
        
        PrintStream printer_sd = new PrintStream(new File("shuttle density.txt"));
        PrintStream printer_wt = new PrintStream(new File("guest wait time.txt"));
        PrintStream printer_dl = new PrintStream(new File("deadline missed.txt"));
        AD.Simulate(MAX_TIME, printer_sd, printer_wt, printer_dl);
    }

    public static void main(String[] args){
        try{ Simulator simulator = new Simulator();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
