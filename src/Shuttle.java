public class Shuttle {
    private int name;
    private int x, y;
    private int max = 50;
    private int nums = 0;

    private Schedule S;
    private int time; // Shuttle's current time
    Map map;

    private sched To;
    private sched From;

    Shuttle(int xi, int yi, int timei, Schedule Si, int namei, Map mapi) {
        name = namei;
        x = xi;
        y = yi;
        time = timei;
        map = mapi;
        Si.removeAfterT(time);
        S = Si; // we don't care sched's nums now
        To = S.whatIthSched(0);
        From =S.whatIthSched(1);
    }

    public int Driving(int t, int par){
        int tempn = nums; // use this method after update sched's nums
        Schedule temps = S.copyS();
        int toidx =  S.whatSchedIdx(t);
        for(int i=0; i<toidx; i++){
            int ti = S.whatIthSched(i).getTime();
            drive(ti, par);
        } if(par>0) setTime(t);
        if(par<1) {
            nums = tempn;
            S = temps.copyS();
        } return nums; // return at t's number
    }

    public void drive(int timei, int par){ // call after sync with request
        Taski taski = new Taski(timei, S, map);
        int num = taski.getTo().getNums();
        int empty = getEmpty();
        sched To = taski.getTo();
        int tim = To.getTime();

        if(num == 0) return ;
        if(taski.getPassed() >= taski.getRequire()) {
            if(empty >= num){ // contain num < 0 (drop off)
                nums += num;
                To.getStation().rideDrop(num);
                To.setNums(0);
                if(par>0) {
                    if (num > 0) System.out.println(tim+": "+name + ", " + num
                            + " people ride at " + getEmpty());
                    if (num < 0) System.out.println(tim+": "+name + ", "
                            + num + " people drop at " + getEmpty());
                    if (getEmpty() == 0) System.out.println(name + ", full!\n");
                }
            } else if(empty > 0 && num > 0){
                nums += empty;
                To.setNums(num-empty);
                To.getStation().rideDrop(empty);
                if(par>0) System.out.println(tim+": "+name + ", " + (num-empty)
                        + " people ride at " + getEmpty());
                if(getEmpty()==0 && par>0)  System.out.println(tim+": "+name+", full!\n");
            } // do at timei
        }
    }
    public int goBefore(int timeS, sched s){
        int leftT = S.getNumSched(); // timeS = shuttle's arrive time at source
        int idx = S.whatSchedIdx(timeS)+1;
        for(int i=idx; i<leftT; i++){
            sched si = S.whatIthSched(i);
            if(si.getTime() > s.getTime()) return -1;
            if(si.getStation().equals(s.getStation())) return i;
        } return -1;
    }// when call this method, shuttle's To equals to guest's source

    public void takeSched(sched s){
        int idx = S.whatSchedIdx(s);
        int numa = s.getNums();
        int numb = S.whatIthSched(idx).getNums();
        S.whatIthSched(idx).setNums(numa+numb);
    }

    public sched whereTo(int timei) {
        int idx = S.whatSchedIdx(timei);
        return S.whatIthSched(idx);
    }
    public sched whereFrom(int timei, Map map) {
        Taski taski = new Taski(timei, S, map);
        return taski.getFrom();
    }
    public int getEmptyAtT(int t, Map map){
        return Driving(t, 0);
    }
    public void setName(int n){ name = n; }
    public void setXY(int xi, int yi) {
        x = xi;
        y = yi;
    } // update location

    public void setSchedule(Schedule Si) {
        Si.removeAfterT(time);
        S = Si;
    } // update schedule

    public void setTime(int timei) {
        time = timei;
        S.removeAfterT(time);
    } // update time
    public void setMax(int n){ max = n; }

    public int getName() { return name; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getTime() { return time; }
    public int getMax() { return max; }
    public int getNums() {return nums; }
    public int getEmpty() {return max-nums; }
    public Schedule getSchedule() { return S; }
}

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
}