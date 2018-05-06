import java.util.*;

public class Schedule{
    private sched[] Schedules; // {from, to, next, next, next, ...}
    // Schedules[0] is where shuttle from

    public Schedule(){
        Schedules = new sched[0];
    }

    public int whatSchedIdx(int time){
        int l = Schedules.length;
        for(int i=0; i<l; i++){
            if(Schedules[i].getTime() >= time) return i;
        } return l+1; // if there are no schedule after time
    } // return after (or at) time's destination index

    public int whatSchedIdx(sched s){
        int l = Schedules.length;
        for(int i=0; i<l; i++){
            if(s.equals(Schedules[i])) return i;
        } return l+1; // if there are no schedule after time
    } // return after (or at) time's destination index

    public sched whatIthSched(int i){
        int l = Schedules.length;
        if(i >= l) {
            sched lasts = Schedules[l-1];
            return (new sched(lasts.getTime(),lasts.getStation(),0));
        } return Schedules[i];
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
    public void mergeWith(Schedule s){
        sched[] sc = s.getScheds();
        sched[] ms = new sched[sc.length+Schedules.length];
        System.arraycopy(Schedules, 0, ms, 0, Schedules.length);
        System.arraycopy(sc, 0, ms, Schedules.length, sc.length);
        Schedules = ms;
        sortSchedule();
    }
    public void removeSchedule(int idx){
        int l = Schedules.length;
        if(idx >= l) {
            System.out.println("ERROR : Remove schedule out of range");
            return;
        } Schedules[idx] = Schedules[l-1];
        Schedules = Arrays.copyOf(Schedules, l-1);
        sortSchedule();
    } // remove schedule
    public void removeSchedule(sched s){
        int idx = whatSchedIdx(s);
        removeSchedule(idx);
        sortSchedule();
    } // remove schedule

    public void Waiting(){
        for(int i=0; i<Schedules.length; i++){
            Schedules[i].waiting();
        }
    }

    public sched[] getScheds(){ return Schedules; }
    public int getNumSched(){ return Schedules.length; }

    public void mergeScheds(sched s){
        int idx = whatSchedIdx(s);
        for(int i=idx+1; i<Schedules.length; i++){
            if(s.equals(Schedules[i])){
                int nidx = Schedules[idx].getNums();
                int ni = Schedules[i].getNums();
                Schedules[idx].setNums(nidx + ni);
                removeSchedule(i);
                i--; }
        }
    }
    public void replaceSchedule(sched pres, sched news){
        int ind = whatSchedIdx(pres);
        Schedules[ind] = news;
    }
    public Schedule copyS(){
        Schedule copy = new Schedule();
        for(int i=0; i<Schedules.length; i++){
            copy.addSchedule(Schedules[i].copyS());
        } return copy;
    }
    public String printing(int pr){
        sortSchedule();
        String str = whatIthSched(0).printing(pr);
        for(int i=1; i<Schedules.length; i++){
            sched s = whatIthSched(i);
            str += ", "+s.printing(pr);
        } str += "";
        return str;
    }
}

class sched {
    private int time;
    private Station place;
    private int nums;
    private int wait=0;

    sched(int t, Station p, int n){
        time = t;
        place = p;
        nums = n;
    }

    public boolean equals(sched s){
        if(this.time == s.getTime() && this. place.equals(s.getStation())) return true;
        else return false;
    } // same time and same station
    public sched copyS(){
        return new sched(this.getTime(), this.getStation(), this.getNums());
    }
    public boolean similar(sched s, int dt){
        if(this.place.equals(s.getStation())) {
            if ((this.getTime() - dt) <= s.getTime()
                    && s.getTime() <= (this.getTime()+dt)) return true;
        } return false;
    }

    public void setTime(int t){ time = t;}
    public void setStation(Station p){ place = p; }
    public void setNums(int n){ nums = n; }
    public void waiting(){wait += 1;}
    public int getWait(){return wait;}
    public int getSponT(){return (Simulator.MAX_TIME-wait);}

    public int getTime(){ return time; }
    public Station getStation(){ return place; }
    public int getNums() { return nums; }

    public String printing(int pr){
        if(pr>0) return ("("+place.getName()+"/"+time+")");
        else if(pr==0) return (wait+"");
        else return ("("+place.getName()+"/"+time+"/"+getSponT()+")");
    }
}