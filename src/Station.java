public class Station {
    public static void main(String[] ar){
        a();
    }
    public static int a(){
        return "abc";
    }

    private String name;
    private double x;
    private double y;

    Station(String namei, double xi, double, yi){
        name = namei;
        x = xi;
        y = yi;
    }

    public String getName(){
        return name;
    }

    public double[] getPosition(){
        return {x, y};
    }

    //public Station nearestStation(double x, double y){}
}