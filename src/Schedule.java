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
        } return l+1; // if there are no schedule after current time
    } // return current time destination's index

    public Station whatIthStation(int i){
        sched next = Schedules[i];
        return next.getStation();
    }

    Schedule(int t, Station p){
        sched s =  new sched(t, p);
        int l = Schedules.length;
        Schedules = Arrays.copyOf(Schedules,  l+1);
        Schedules[l] = s;
        Arrays.sort(Schedules, new Comparator<sched>() {
            @Override
            public int compare(sched o1, sched o2) {
                final int t1 = o1.getTime();
                final int t2 = o2.getTime();
                return Integer.compare(t1, t2);
            }
        });
    }
}

class sched {
    private int time;
    private Station place;

    public int getTime(){
        return time;
    }
    public Station getStation(){
        return place;
    }

    sched(int t, Station p){
        time = t;
        place = p;
    }
}