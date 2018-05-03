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
    public void setXY(int xi, int yi){
        x = xi;
        y = yi;
    }
    public void setSchedule(Schedule Si){
        S = Si;
    }
    public void setTime(int timei){
        time = timei;
        S.removeAfterT(time);
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getTime(){
        return time;
    }
    public Schedule getSchedule(){
        return S;
    }
}
