public class Station {
    private String name;
    private double x;
    private double y;

    Station(String namei, double xi, double yi){
        name = namei;
        x = xi;
        y = yi;
    }

    public String getName(){
        return name;
    }

    public double[] getPosition(){
        double[] position = {x, y};
        return position;
    }

    //public Station nearestStation(double x, double y){}
}