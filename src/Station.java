public class Station {
    private String name;
    private int x, y;
    private int nums = 0;
    private int idx;

    public Station(){}

    public Station(String namei, int xi, int yi, int idxi){
        name = namei;
        x = xi;
        y = yi;
        idx = idxi;
    }

    public boolean equals(Station sta){
        if(name.equals(sta.getName())
                && this.x == sta.getX() && this.y == sta.getY()) return true;
        else return false;
    }
    /*
    public void setNums(int n){ nums = n; }
    public void rideDrop(int n){
        if(n>0) nums -= n;
    }
    public void newGuest(int n){ nums += n; }
    public void getofForTransfer(int n){ nums += n; } // if we have to consider transfer use this
    */
    public String getName(){ return name; }
    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getNums(){ return nums; }
    public int getIdx(){ return idx; }
    //public Station nearestStation(int x, int y){}
}