public class Simulator {
    public static int MAX_TIME = 100;
    public static int MAX_STATION = 10;
    public static double K_RATIO = 0.5;

    public Simulator(){
        int userN = 1000;

        Map map = new Map("stations.txt");
        Generator generator = new Generator(userN, map); // Generate userN guests for this map
    }
}
