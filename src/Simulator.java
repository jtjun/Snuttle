import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Simulator {
    public static int MAX_TIME = 1000;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;
    public static int shutn = 10 ;
    public static int userN = 1000;
    public static int fixedshuttle = 5;
    public static ArrayList<Guest> guests;
    public static Map map;
    public static int staN;

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
        staN = map.getNumStations();
    }
    public void Start(String type, int shutni, int userNi, int ratio) throws FileNotFoundException {
        shutn = shutni;
        userN = userNi;
        System.out.print("\nUser number: "+userN+" Shuttle number: "+shutn+" Station number: "+staN);
        System.out.print("\nGuest Type : "+type+" ____________________________");
        // All Random guest situation
        Generator generator = new Generator(userN, map, type); // Generate userN guests for this map
        guests = new ArrayList<>();
        guests = generator.getGuests();
        Request R = new Request(guests, map);

        Shuttle[] shuttleC = new Shuttle[shutn]; // Circular
        Shuttle[] shuttleE = new Shuttle[shutn]; // Express
        Shuttle[] shuttleP = new Shuttle[shutn]; // Proposed
        Shuttle[] shuttleG = new Shuttle[shutn]; // Greedy
        Shuttle[] shuttleGd = new Shuttle[shutn]; // Greedy


        //Setting schedule to shuttle
        Simulator.fixedshuttle = shutn/ratio;
        CircularSchedule.setCircularSchedule(shuttleC, map);
        ExpressSchedule.setExpressSchedule(shuttleE, map, shutn/ratio);
        ProposedSchedule.setProposedSchedule(shuttleP, map, guests, shutn/ratio);
        GreedySchedule.setGreedySchedule(shuttleG, map, guests, shutn/ratio);
        GreedySchedule.setGreedySchedule(shuttleGd, map, guests, shutn/ratio);
        /*Printing Shuttle's Schedule
        PrintShutSched(shuttleC, "Circular");
        PrintShutSched(shuttleE, "Express");
        PrintShutSched(shuttleP, "Proposed");
        PrintShutSched(shuttleG, "Greedy");
        PrintShutSched(shuttleGd, "Greedy100");*/

        // type : Cicular
        System.out.println("\ntype : Circular");
        Request RC = new Request(guests, map);
        ActualDrive Cir = new ActualDrive(shuttleC, RC, map, ("Circular "+type), 0);
        int cir = Cir.Simulate();
        System.out.println("Circular done : "+cir+"/"+userN);

        // type : Express
        System.out.println("\ntype : Express");
        Request RE = new Request(guests, map);
        ActualDrive Exp = new ActualDrive(shuttleE, RE, map, ("Express "+type), 0);
        int exp = Exp.Simulate();
        System.out.println("Express done : "+exp+"/"+userN);

        // type : Proposed
        System.out.println("\ntype : Proposed");
        Request RP = new Request(guests, map);
        ActualDrive Prop = new ActualDrive(shuttleP, RP, map, ("Proposed "+type), 0);
        int prp = Prop.Simulate();
        System.out.println("Proposed done : "+prp+"/"+userN);

        // type : Greedy 1
        System.out.println("\ntype : Greedy, time period 1");
        Request RG = new Request(guests, map); // gredi is equal to time period of refresh
        ActualDrive Grd = new ActualDrive(shuttleG, RG, map, ("Greedy "+type), 1);
        int grd = Grd.Simulate();
        System.out.println("Greedy done : "+grd+"/"+userN);

        // type : Greedy 30
        System.out.println("\ntype : Greedy, time period 100");
        Request RGd = new Request(guests, map);
        ActualDrive Grdd = new ActualDrive(shuttleGd, RGd, map, ("Greedy "+type), 100);
        int grdd = Grdd.Simulate();
        System.out.println("Greedy done : "+grdd+"/"+userN);
    }

    public void PrintShutSched(Shuttle[] shuttles, String type) throws  FileNotFoundException{
        PrintStream schedul = new PrintStream(new File(type+" Schedule.txt"));
        schedul.println("Type : "+type);
        for(int j=0; j<shuttles.length; j++){
            schedul.println("Shuttle"+j+"'s :\t"+shuttles[j].getSchedule().printing(1));
        } schedul.close();
    }
}
