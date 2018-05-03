public class Station {
    private String name;
    private int x;
    private int y;

    Station(String namei, int xi, int yi){
        name = namei;
        x = xi;
        y = yi;
    }

    public String getName(){
        return name;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    //public Station nearestStation(double x, double y){}
}