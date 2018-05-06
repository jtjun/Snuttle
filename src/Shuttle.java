public class Shuttle {
    private int name;
    private int x, y;
    private int max = 50;
    private int nums = 0;
    private int refresh = 0;

    private Schedule S;
    private Schedule People = new Schedule();
    private int time; // Shuttle's current time
    Map map;

    Shuttle(int xi, int yi, int timei, Schedule Si, int namei, Map mapi) {
        name = namei;
        x = xi;
        y = yi;
        time = timei;
        map = mapi;
        S = Si; // we don't care sched's nums now
    }

    public int goBefore(int timeS, sched s){
        int schedn = S.getNumSched(); // timeS = shuttle's arrive time at source
        int idx = S.whatSchedIdx(timeS)+1;
        for(int i=idx; i<schedn; i++){
            sched si = S.whatIthSched(i);
            if(si.getTime() > s.getTime()) return -1;
            if(si.getStation().equals(s.getStation())) return i;
        } return -1;
    }// when call this method, shuttle's To equals to guest's source

    public void rideS(sched person){
        People.addSchedule(person);
        nums++;
    }
    public void dropS(int idx){ // sched's index
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
            if(To.getTime()<=person.getTime() && person.getStation().equals(To.getStation())){
                People.removeSchedule(i);
                dropP--;
                i--;
            } // Drop the people who want get out here
        } if (dropn != dropP) System.out.println("ERROR : Different People are get out"+dropn+", "+dropP);
    }
    public void getOutAll(){
        nums =0;
        People = new Schedule();
    }

    public void errorCheck(int time){
        if(nums<0) System.out.println("ERROR : At "+time+" Shuttle"+name+" has negative people.");
        if(nums!=People.getNumSched()) System.out.println("ERROR : Different number of people"+nums+","+People.getNumSched());
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

    public void setRefresh(int t){refresh=t;}
    public void setNums(int n){nums=n;}
    public void setTime(int timei) { time = timei;}
    public void setMax(int n){ max = n; }
    public void setName(int n){ name = n; }
    public void setXY(int xi, int yi) {
        x = xi;
        y = yi;
    } // update location

    public int getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getTime() { return time; }
    public int getMax() { return max; }
}
/* Use this class when we have to draw map or catch the cross
class Taski {
    private int time; // current time
    private sched from, to;
    private int passed, remain, require;

    Taski(int timei, Schedule S, Map map) {
        time = timei;
        int idx = S.whatSchedIdx(time);
        to = S.whatIthSched(idx);

        if(idx>0) from = S.whatIthSched(idx - 1);
        if(idx==0) from = to.copyS();

        passed = time -from.getTime();
        remain = to.getTime() -time;
        require = map.getDistance(from.getStation().getName(), to.getStation().getName());
    } // pass + remain = schedule time from-to

    public sched getFrom(){return from;}
    public sched getTo(){return to;}
    public int getPassed(){return passed;} //getStartT(){return time-passed;}
    public int getRemain(){return remain;} //getArriveT(){return time + (require - passed);}
    public int getRequire(){return require;}
} for later schedule method will use it
*/