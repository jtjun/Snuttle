import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Simulator {
    public static int MAX_TIME = 1000;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;
    public static int shutn = 10 ;
    public static int userN = 1000;
    private Map map;

    public static void main(String[] args){
        try{ Simulator SimulatoR = new Simulator();
            SimulatoR.Start("AR", 10, 1000, 2);
            SimulatoR.Start("HS", 10, 1000, 2);
            // ratio is high -> fixed shuttle is low (minimum 1)
        }catch( FileNotFoundException e ){
            System.out.println(e);
        }
    }

    public Simulator() throws FileNotFoundException {
        map = new Map("stations.csv", "distance.csv");
    }
    public void Start(String type, int shutni, int userNi, int ratio) throws FileNotFoundException {
        shutn = shutni;
        userN = userNi;
        System.out.print("\nGuest Type : "+type+" ____________________________");
        // All Random guest situation
        Generator generator = new Generator(userN, map, type); // Generate userN guests for this map
        ArrayList<Guest> guests = new ArrayList<>();
        guests = generator.getGuests();
        Request R = new Request(guests, map);

        Shuttle[] shuttleC = new Shuttle[shutn]; // Circular
        Shuttle[] shuttleE = new Shuttle[shutn]; // Express
        Shuttle[] shuttleG = new Shuttle[shutn]; // Greedy

        //Setting schedule to shuttle
        CircularSchedule.setCircularSchedule(shuttleC, map);
        ExpressSchedule.setExpressSchedule(shuttleE, map, shutn/ratio);
        GreedySchedule.setGreedySchedule(shuttleG, map, guests, shutn/ratio);
        PrintShutSched(shuttleC, "Circular");
        PrintShutSched(shuttleE, "Express");
        PrintShutSched(shuttleG, "Greedy");

        // type : Greedy
        System.out.println("\ntype : Greedy");
        Request RG = new Request(guests, map);
        ActualDrive Grd = new ActualDrive(shuttleG, RG, map, ("Greedy "+type));
        Grd.Simulate();
        System.out.println("Greedy done");

        // type : Express
        System.out.println("\ntype : Express");
        Request RE = new Request(guests, map);
        ActualDrive Exp = new ActualDrive(shuttleE, RE, map, ("Express "+type));
        Exp.Simulate();
        System.out.println("Express done");

        // type : Cicular
        System.out.println("\ntype : Circular");
        Request RC = new Request(guests, map);
        ActualDrive Cir = new ActualDrive(shuttleC, RC, map, ("Circular "+type));
        Cir.Simulate();
        System.out.println("Circular done");
    }

    public void PrintShutSched(Shuttle[] shuttles, String type) throws  FileNotFoundException{
        PrintStream schedul = new PrintStream(new File(type+" Schedule.txt"));
        schedul.println("Type : "+type);
        for(int j=0; j<shuttles.length; j++){
            schedul.println("Shuttle"+j+"'s :\t"+shuttles[j].getSchedule().printing(1));
        } schedul.close();
    }
}
