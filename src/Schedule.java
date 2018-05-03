import java.util.*;

public class Schedule{
    private sched[] Schedules; // {from, to, next, next, next, ...}
    // Schedules[0] is where shuttle from

    public Schedule(){
        Schedules = new sched[0];
    }

    public void removeAfterT(int time){
        int dt = whatTimeS(time) -1;
        sched[] nSched = new sched[Schedules.length - dt];
        for(int i=0; i<Schedules.length - dt; i++){
            nSched[i] = Schedules[dt+i];
        } Schedules = Arrays.copyOf(nSched, Schedules.length - dt);
    }

    public int whatTimeS(int time){
        int l = Schedules.length;
        for(int i=0; i<l; i++){
            if(Schedules[i].getTime() > time) return i;
        } return l+1; // if there are no schedule after time
    } // return after time first destination's index

    public sched whatIthSched(int i){
        return Schedules[i];
    }

    public void sortSchedule(){
        Arrays.sort(Schedules, new Comparator<sched>() {
            @Override
            public int compare(sched o1, sched o2) {
                final int t1 = o1.getTime();
                final int t2 = o2.getTime();
                return Integer.compare(t1, t2);
            }
        }); // sort schedule by compare time
    }

    public void addSchedule(int t, Station p, int n){
        sched s =  new sched(t, p, n);
        addSchedule(s);
    }
    public void addSchedule(sched s){
        int l = Schedules.length;
        Schedules = Arrays.copyOf(Schedules, l+1);
        Schedules[l] = s;
        sortSchedule(); // add new schedule s
    }

    public void removeSchedule(sched s){
        int l = Schedules.length;
        int ind = whatTimeS(s.getTime());
        Schedules[ind] = Schedules[l-1];
        Schedules = Arrays.copyOf(Schedules, l-1);
        sortSchedule();
    } // remove schedule

    public void replaceSchedule(sched pres, sched news){
        int ind = whatTimeS(pres.getTime());
        Schedules[ind] = news;
    }

    public String toString(){
        String ret = "[";
        for(int i = 0; i < Schedules.length; i++){
            ret += Schedules[i].toString()+",";
        } return ret+"]";
    }
}

class sched {
    private int time;
    private Station place;
    private int nums = 1;

    sched(int t, Station p, int n){
        time = t;
        place = p;
        nums = n;
    }

    public void setTime(int t){ time = t; }
    public void setStation(Station p){ place = p; }
    public void setNums(int n){ nums = n; }

    public int getTime(){ return time; }
    public Station getStation(){ return place; }
    public int getNums() { return nums; }

    public String toString(){
        return "time("+time+"), place("+place.getName()+"), nums("+nums+")";
    }
}