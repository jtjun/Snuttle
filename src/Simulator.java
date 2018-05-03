import java.io.FileNotFoundException;

public class Simulator {
    public static int MAX_TIME = 100;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;

    public Simulator() throws FileNotFoundException{
        int userN = 1000;

        Map map = new Map("stations.csv", "distance.csv");
        Generator generator = new Generator(userN, map); // Generate userN guests for this map
        Shuttle[] shuttles = new Shuttle[10];
        MainSystem.setCircularSchedule(shuttles,map);
        for(int i = 0; i < shuttles.length; i++){
            System.out.println((shuttles[i].getSchedule().toString()));
        }
    }

    public static void main(String[] args){
        try{ Simulator simulator = new Simulator();
        }catch(FileNotFoundException e){
            System.out.println(e);
        }
    }
}
