import java.util.*;

public class Schedule{
    private sched[] Schedules;

    public void removeAfterT(double time){
        int dt = whatNextD(time) -1; // {from, to, next, next, next, ...}
        sched[] nSched = new sched[Schedules.length - dt];
        for(int i=0; i<Schedules.length - dt; i++){
            nSched[i] = Schedules[dt+i];
        } Schedules = Arrays.copyOf(nSched, Schedules.length - dt);
    }

    public int whatNextD(double time){
        int l = Schedules.length;
        for(int i=0; i<l; i++){
            if(Schedules[i].getTime() > time) return i;
        } return -1; // if there are no destination (no schedule after current time)
    } // return current time destination's index
}

class sched {
    private double time;
    private String place;

    public double getTime(){
        return time;
    }
    public String getName(){
        return place;
    }
}