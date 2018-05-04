import java.io.FileNotFoundException;

public class Simulator {
    public static int MAX_TIME = 100;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;
    public static int shutn = 10;

    public Simulator() throws FileNotFoundException{
        int userN = 1000;
        Map map = new Map("stations.csv", "distance.csv");

        Generator generator = new Generator(userN, map, "AR"); // Generate userN guests for this map
        Shuttle[] shuttles = new Shuttle[shutn];
        MainSystem.setCircularSchedule(shuttles,map, generator.getGuests()); // making "shuttle - schedule"

        for(int i = 0; i < shuttles.length; i++){
            System.out.println(i +" th "+ (shuttles[i].getSchedule().toString()));
        }
        for(int i = 0; i < shutn; i++){
            shuttles[i].driving(5000, map);
            // have to add drawing graph method
        } // actual driving
    }

    public static void main(String[] args){
        try{ Simulator simulator = new Simulator();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
