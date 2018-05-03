import java.util.*;

public class Schedule{
    private sched[] Schedules; // {from, to, next, next, next, ...}
    // Schedules[0] is where shuttle from

    public void removeAfterT(int time){
        int dt = whatAfter(time) -1;
        sched[] nSched = new sched[Schedules.length - dt];
        for(int i=0; i<Schedules.length - dt; i++){
            nSched[i] = Schedules[dt+i];
        } Schedules = Arrays.copyOf(nSched, Schedules.length - dt);
    }

    public int whatAfter(int time){
        int l = Schedules.length;
        for(int i=0; i<l; i++){
            if(Schedules[i].getTime() > time) return i;
        } return l+1; // if there are no destination (no schedule after current time)
    } // return current time destination's index

    public Station whatIthStation(int i){
        sched next = Schedules[i];
        return next.getStation();
    }
}

class sched {
    private int time;
    private Station place;

    public double getTime(){
        return time;
    }
    public Station getStation(){
        return place;
    }
}