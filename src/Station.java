public class Station {
    private String name;
    private int x, y;
    private int nums = 0;

    Station(String namei, int xi, int yi){
        name = namei;
        x = xi;
        y = yi;
    }

    public void setNums(int n){ nums = n; }

    public void ride(){ nums--; }
    public void riden(int n){
        nums -= n;
    }

    public void newGuest(){ nums++; }
    public void newGuestn(int n){
        nums += n;
    }

    public void getoff_for_transfer(){ nums++; }
    public void getoff_for_transfer(int n){
        nums += n;
    } // if we have to consider transfer use this

    public String getName(){ return name; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getNums(){ return nums; }
    //public Station nearestStation(int x, int y){}
}