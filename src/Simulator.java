import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class Simulator {
    public static int MAX_TIME = 540;
    public static int MAX_STATION = 23;
    public static double K_RATIO = 0.5;
    public static int shutn = 5;
    public static int ratio = 2;
    public static int userN = 1000;
    public static int fixedshuttle = (shutn/ratio);
    public static int maxPeople = 45;
    public static ArrayList<Guest> guests;
    public static Map map;
    public static int totalD=77;
    public static int refresh=1;

    public static int staN;
    public static boolean monit=false;
    public static boolean goThereS = true;
    public static boolean Wait=false;
    public static String[] types = {"AR", "HS", "LR", "GG", "CM", "PG", "EX"};

    public static void main(String[] args) {
        String type = "LR";
        quickStart(type);
        // Interface();
    }

    public static void quickStart(String type){
        try{ Simulator SimulatoR = new Simulator(type, shutn, userN, ratio);
            SimulatoR.Start(type);
        }catch( FileNotFoundException e ){
            System.out.println(e);
        }
    }

    public static void Interface(){
        Scanner scanner = new Scanner(System.in);
        String type;
        // type
        System.out.print("Input a generator type(AR/HS/LR/GG/CM/PG): ");
        String input = scanner.nextLine();
        if(isIn(input, types)) {
            type = input;
        }  else{
            System.out.println("Wrong Type");
            return;
        }
        // refresh time
        System.out.print("Input a refresh time(>0): ");
        refresh = Integer.parseInt(scanner.nextLine());
        // ratio
        System.out.print("Input a generating ratio(>0): ");
        ratio = Integer.parseInt(scanner.nextLine());
        // user number
        System.out.print("Input the number of people: ");
        userN = Integer.parseInt(scanner.nextLine());
        // shuttle number
        System.out.print("Input the number of shuttles: ");
        shutn = Integer.parseInt(scanner.nextLine());
        // maxpeople
        System.out.print("Input the capacity of each shuttle: ");
        maxPeople = Integer.parseInt(scanner.nextLine());

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
        System.out.println("\nGuest Type : " + type + " ____________________________");

        StartC(type, monit);
        //StartE(type, monit);
        StartP(type, monit);
        //StartG(type,refresh, monit);
        //StartAG(type, monit);
        StartTS(type, -10, monit);
        StartPTS(type, 10, monit);
    }
    public void StartC(String type, boolean monit) throws FileNotFoundException {
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
    public void StartE(String type, boolean monit) throws FileNotFoundException {
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
    public void StartP(String type, boolean monit) throws FileNotFoundException {
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
    public void StartG(String type, int gred, boolean monit) throws FileNotFoundException {
        Shuttle[] shuttleG = new Shuttle[shutn]; // Greedy
        GreedySchedule.setGreedySchedule(shuttleG, map, guests, shutn/ratio);
        // type : Greedy gred
        System.out.println("\ntype : Greedy, time period "+gred);
        Request RG = new Request(guests, map); // gredi is equal to time period of refresh
        ActualDrive Grd = new ActualDrive(shuttleG, RG, map, ("Greedy "+gred+" "+type), gred);
        int grd = Grd.Simulate(monit);
        System.out.println("Greedy done : "+grd+"/"+userN);
        //PrintShutSched(shuttleG, "Greedy "+gred);
    }
    public void StartIG(String type, boolean monit) throws FileNotFoundException {
        Shuttle[] shuttleIG = new Shuttle[shutn]; // Greedy
        ImrovedGreedy IGreed = new ImrovedGreedy();
        IGreed.setIGreddy(shuttleIG, map, guests, shutn/2);
        // type : IGreedy
        System.out.println("\ntype : Improved Greedy");
        Request RIG = new Request(guests, map); // gredi is equal to time period of refresh
        ActualDrive IGrd = new ActualDrive(shuttleIG, RIG, map, ("IGreedy "+type), 0);
        int Igrd = IGrd.Simulate(monit);
        System.out.println("IGreedy done : "+Igrd+"/"+userN);
    }
    public void StartTS(String type, int gred, boolean monit) throws FileNotFoundException {
        Shuttle[] shuttleTS = new Shuttle[shutn]; // Greedy
        TrampSteamerGreedy.setGreedySchedule(shuttleTS, map, guests, shutn/ratio);
        // type : Greedy TS
        System.out.println("\ntype : TS Greedy, time period "+gred);
        Request RTS = new Request(guests, map); // gredi is equal to time period of refresh
        ActualDrive GTS = new ActualDrive(shuttleTS, RTS, map, ("TS Greedy "+gred+" "+type), gred);
        int TSgrd = GTS.Simulate(monit);
        System.out.println("TS Greedy done : "+TSgrd+"/"+userN);
    }
    public void StartPTS(String type, int gred, boolean monit) throws FileNotFoundException {
        Shuttle[] shuttlePTS = new Shuttle[shutn]; // Greedy
        PredictionTSGreedy.setPTSGreedySchedule(shuttlePTS, map, guests, shutn/ratio);
        // type : Greedy PTS
        System.out.println("\ntype : PTS Greedy, time period "+gred);
        Request RPTS = new Request(guests, map); // gredi is equal to time period of refresh
        ActualDrive PTS = new ActualDrive(shuttlePTS, RPTS, map, ("PTS Greedy "+gred+" "+type), gred);
        int Pgrd = PTS.Simulate(monit);
        System.out.println("PTS Greedy done : "+Pgrd+"/"+userN);
    }
    public void StartAG(String type, boolean monit) throws FileNotFoundException {
        Shuttle[] shuttleAG = new Shuttle[shutn]; // Greedy
        AnotherGreedy AGreedy = new AnotherGreedy();
        AGreedy.setAGreddy(shuttleAG, map, guests, shutn/2);
        // type : AGreedy
        System.out.println("\ntype : Another Greedy");
        Request AG = new Request(guests, map); // gredi is equal to time period of refresh
        ActualDrive AGrd = new ActualDrive(shuttleAG, AG, map, ("AGreedy "+type), 0);
        int Agrd = AGrd.Simulate(monit);
        System.out.println("AGreedy done : "+Agrd+"/"+userN);
    }
    public void PrintShutSched(Shuttle[] shuttles, String type) throws  FileNotFoundException{
        PrintStream schedul = new PrintStream(new File(type+" Schedule.csv"));
        schedul.println("Type : "+type);
        for(int j=0; j<shuttles.length; j++){
            schedul.println("Shuttle"+j+"'s :,\t"+shuttles[j].getSchedule().printing(1));
        } schedul.close();
    }

    public static boolean isIn(String str, String[] strs){
        int l = strs.length;
        for(int i=0; i<l; i++){
            if(str.equals(strs[i])) return true;
        } return false;
    }
    public static void setTotalD(int dist){totalD = dist;}
}
