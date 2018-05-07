import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Simulator {
    public static int MAX_TIME = 1440;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;
    public static int shutn = 1;
    public static int ratio = 2;
    public static int userN = 1000;
    public static int fixedshuttle = (shutn/ratio);
    public static int maxPeople = 45;
    public static ArrayList<Guest> guests;
    public static Map map;

    public static int staN;
    public static boolean monit=false;

    public static void main(String[] args){
        String type = "LR";
        try{ Simulator SimulatoR = new Simulator(type, shutn, userN, ratio);
            SimulatoR.Start(type);

        }catch( FileNotFoundException e ){
            System.out.println(e);
        }
    }

    public Simulator(String type, int shutni, int userNi, int ratioi) throws FileNotFoundException {
        map = new Map("stations.csv", "distancev2.csv");
        staN = map.getNumStations();
        shutn = shutni;
        userN = userNi;
        Generator generator = new Generator(userN, map, type); // Generate userN guests for this map
        guests = new ArrayList<>();
        guests = generator.getGuests();
        ratio = ratioi;
        Simulator.fixedshuttle = (shutn/ratio);
    }

    public void Start(String type) throws FileNotFoundException {
        System.out.print("\nUser number: " + userN + " Shuttle number: " + shutn + " Station number: " + staN);
        System.out.print("\nGuest Type : " + type + " ____________________________");
        // All Random guest situation
        StartC(type);
        StartE(type);
        StartP(type);
        StartG(type,1);
        StartG(type,30);
    }
    public void StartC(String type) throws FileNotFoundException {
        Shuttle[] shuttleC = new Shuttle[shutn]; // Circular
        CircularSchedule.setCircularSchedule(shuttleC, map);
        // type : Cicular
        System.out.println("\ntype : Circular");
        Request RC = new Request(guests, map);
        ActualDrive Cir = new ActualDrive(shuttleC, RC, map, ("Circular "+type), 0);
        int cir = Cir.Simulate(monit);
        System.out.println("Circular done : "+cir+"/"+userN);
        //PrintShutSched(shuttleC, "Circular");

    }
    public void StartE(String type) throws FileNotFoundException {
        Shuttle[] shuttleE = new Shuttle[shutn]; // Express
        ExpressSchedule.setExpressSchedule(shuttleE, map, shutn/ratio);
        // type : Express
        System.out.println("\ntype : Express");
        Request RE = new Request(guests, map);
        ActualDrive Exp = new ActualDrive(shuttleE, RE, map, ("Express "+type), 0);
        int exp = Exp.Simulate(monit);
        System.out.println("Express done : "+exp+"/"+userN);
        // PrintShutSched(shuttleE, "Express");
    }
    public void StartP(String type) throws FileNotFoundException {
        Shuttle[] shuttleP = new Shuttle[shutn]; // Proposed
        ProposedSchedule.setProposedSchedule(shuttleP, map, guests, shutn/ratio);
        // type : Proposed
        System.out.println("\ntype : Proposed");
        Request RP = new Request(guests, map);
        ActualDrive Prop = new ActualDrive(shuttleP, RP, map, ("Proposed "+type), 0);
        int prp = Prop.Simulate(monit);
        System.out.println("Proposed done : "+prp+"/"+userN);
        // PrintShutSched(shuttleP, "Proposed");
    }
    public void StartG(String type, int gred) throws FileNotFoundException {
        Shuttle[] shuttleG = new Shuttle[shutn]; // Greedy
        GreedySchedule.setGreedySchedule(shuttleG, map, guests, shutn/ratio);
        // type : Greedy 1
        System.out.println("\ntype : Greedy, time period 1");
        Request RG = new Request(guests, map); // gredi is equal to time period of refresh
        ActualDrive Grd = new ActualDrive(shuttleG, RG, map, ("Greedy "+type), gred);
        int grd = Grd.Simulate(monit);
        System.out.println("Greedy done : "+grd+"/"+userN);
        //PrintShutSched(shuttleG, "Greedy "+gred);
    }
    public void PrintShutSched(Shuttle[] shuttles, String type) throws  FileNotFoundException{
        PrintStream schedul = new PrintStream(new File(type+" Schedule.txt"));
        schedul.println("Type : "+type);
        for(int j=0; j<shuttles.length; j++){
            schedul.println("Shuttle"+j+"'s :\t"+shuttles[j].getSchedule().printing(1));
        } schedul.close();
    }
}
