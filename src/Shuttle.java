import java.util.*;

public class Shuttle {
    private int name;
    private int x, y;
    private int max = Simulator.maxPeople;
    private int nums = 0;
    private int refresh = 0;

    private Schedule S;
    private Schedule People;
    private int timeN; // Shuttle's current time
    private double[] TperD = {};
    Map map;

    Shuttle(int xi, int yi, int timei, Schedule Si, int namei, Map mapi) {
        name = namei;
        x = xi;
        y = yi;
        timeN = timei;
        map = mapi;
        S = Si; // we don't care sched's nums now
        People  = new Schedule();
    }

    public int goBefore(int timeS, sched s){
        int schedn = S.getNumSched(); // timeS = shuttle's arrive time at source
        int idx = S.whatSchedIdx(timeS)+1;
        for(int i=idx; i<schedn; i++){
            sched si = S.whatIthSched(i);
            if(si.getTime() > s.getTime() || si.getTime()>=Simulator.MAX_TIME) return -1;
            if(si.getStation().equals(s.getStation())) return i;
        } return -1;
    }// when call this method, shuttle's To equals to guest's source

    public int goThere(int time, sched s){
        int schedn = S.getNumSched();
        int idx0 = S.whatSchedIdx(time)+1;
        for(int i=idx0; i<schedn; i++){
            sched si = S.whatIthSched(i);
            if(si.getTime()>Simulator.MAX_TIME-1) return -1;
            if(si.getStation().equals(s.getStation())) return i;
        } return -1;
    }

    public void rideS(sched person, int t, boolean monit){
        if(monit) System.out.println("A person ride shuttle at "+t);
        People.addSchedule(person);
        nums++;
    }
    public void dropS(int t, int idx, boolean goThere, boolean monit){ // sched's index
        sched To = S.whatIthSched(idx);
        int dropn = To.getNums();
        if(dropn > 0) {
            System.out.println("ERROR : Shuttle schedule nums is positive");
        } else {
            nums += dropn; // drop the people
            To.setNums(0); // change the schedule To's number
        } int dropP = 0;
        for(int i=0; i<People.getNumSched(); i++){
            sched person = People.whatIthSched(i);
            if(person.getStation().equals(To.getStation())){
                if(To.getTime() <= person.getTime() || goThere){
                    person.tempOut(t);
                    saveTperD(person);
                    People.removeSchedule(i);
                    People.sortSchedule();
                    dropP++;
                    i--;
                }
            } // Drop the people who want get out here
        } if (dropn != -dropP) System.out.println("ERROR : Different People are get out "+(-dropn)+", "+dropP);
        if(monit) System.out.println(dropP+" are get out at station "+To.getStation().getName()+", at "+To.getTime());
    }
    public void getOutAll(){
        nums =0;
        People = new Schedule();
        for(int i=0; i<S.getNumSched(); i++){
            S.whatIthSched(i).setNums(0);
        }
    }
    public void errorCheck(int time){
        if(nums<0) System.out.println("ERROR : At "+time+" Shuttle"+name+" has negative people.");
        if(nums!=People.getNumSched()) System.out.println("ERROR : Different number of people "+nums+","+People.getNumSched());
    }
    public void saveTperD(sched person){
        int l = TperD.length;
        TperD = Arrays.copyOf(TperD, l+1);
        if(Simulator.monit) System.out.println(person.getRiding()+"+"+person.getWait()+"/"+person.getDistance()+" "+
                (((person.getRiding()+person.getWait())*1.0/person.getDistance())==person.getTperD())+" TperD "+person.getTperD());
        TperD[l] = person.getTperD();
    }

    public int whereToIdx(int timei) { return S.whatSchedIdx(timei); }
    public sched whatIthS(int idx){ return S.whatIthSched(idx); }
    public sched whereTo(int timei) {
        int idx = S.whatSchedIdx(timei);
        return S.whatIthSched(idx);
    } // merge two above method

    public sched whereFrom(int timei) {
        int idx = S.whatSchedIdx(timei);
        if(idx==0) return S.whatIthSched(0);
        return S.whatIthSched(idx);
    }
    public int getRefresh(){return refresh;}
    public int getNums() {return nums; }
    public int getEmpty() {return max-nums; }
    public int getSchedN(){return S.getNumSched();}
    public Schedule getSchedule() { return S; }
    public Schedule getPeople(){return People;}
    public double[] getTperD(){return TperD;}

    public void setRefresh(int t){refresh=t;}
    public void setTperD(double[] tperd){ TperD=tperd;}
    public void setNums(int n){nums=n;}
    public void setTimeN(int timei) { timeN = timei;}
    public void setMax(int n){ max = n; }
    public void setName(int n){ name = n; }
    public void setXY(int xi, int yi) {
        x = xi;
        y = yi;
    } // update location

    public int getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getTimeN() { return timeN; }
    public int getMax() { return max; }
}