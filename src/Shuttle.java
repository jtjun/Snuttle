public class Shuttle {
    private int x;
    private int y;
    private int time;
    private Schedule S;

    Shuttle(int xi, int yi, int timei, Schedule Si){
        x = xi;
        y = yi;
        time = timei;
        Si.removeAfterT(time);
        S = Si;
    }

    Shuttle(int timei, Schedule Si){
        time = timei;
        Si.removeAfterT(time);
        S = Si;
    }

    public int[] getXY(){
        int[] xy = {x,y};
        return xy;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public Schedule getSchedule(){
        return S;
    }
}
