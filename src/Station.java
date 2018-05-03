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

<<<<<<< HEAD
    public int[] getXY(){
        int[] position = {x, y};
        return position;
=======
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
>>>>>>> 3051cc32f53ae1878af3249a5afef173b51018e5
    }
    //public Station nearestStation(double x, double y){}
}