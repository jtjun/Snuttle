public class Simulator {
    public static int MAX_TIME = 100;
    public static int MAX_STATION = 10;

    public Simulator(){
        int userN = 1000;

        Map map = new Map();
        Generator generator = new Generator(userN, map);
    }
}
