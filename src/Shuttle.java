public class Shuttle {
    private int x, y;
    private int max = 6;
    private int nums = 0;

    private Schedule S;
    private int time; // Shuttle's current time

    Shuttle(int xi, int yi, int timei, Schedule Si) {
        x = xi;
        y = yi;
        time = timei;
        Si.removeAfterT(time);
        S = Si;
    }
    Shuttle(int timei, Schedule Si) {
        time = timei;
        Si.removeAfterT(time);
        S = Si;
    } // when don't know shuttle's xy

    public void driving(int t, Map map){
        int toidx =  S.whatSchedAtI(t);
        for(int i=0; i<toidx; i++){
            int ti = S.whatIthSched(i).getTime();
            drive(ti, map);
        } setTime(t);
    }

    public void drive(int timei, Map map){
        Taski taski = new Taski(timei, S, map);
        int num = taski.getTo().getNums();
        int empty = getEmpty();
        sched To = taski.getTo();
        if(num == 0) return ;
        if(taski.getPassed() >= taski.getRequire()) {
            if(empty >= num){ // contain num < 0 (drop off)
                nums += num;
                To.getStation().rideDrop(num);
                To.setNums(0);
                if(num > 0) System.out.println(num+"people ride");
                if(num < 0) System.out.println(num+"people drop");
            } else if(empty > 0 && num > 0){
                nums += empty;
                To.setNums(num-empty);
                To.getStation().rideDrop(empty);
                System.out.println((num-empty)+"people ride");
            } // do at timei
        }
    }

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
    private sched from;
    private sched to;
    private int passed;
    private int remain;
    private int require;

    Taski(int timei, Schedule S, Map map) {
        time = timei;
        int idx = S.whatSchedAtI(time);
        to = S.whatIthSched(idx);
        if(idx>0) from = S.whatIthSched(idx - 1);
        if(idx==0) from = to;
        passed = time -from.getTime();
        remain = to.getTime() -time;
        require = map.getDistance(from.getStation().getName(), to.getStation().getName());
    } // pass + remain = schedule time from-to

    public sched getFrom(){return from;}
    public sched getTo(){return to;}
    public int getPassed(){return passed;}
    public int getRemain(){return remain;}
    public int getRequire(){return require;}

    public int getStartT(){return time-passed;}
    public int getArriveT(){return time + (require - passed);}
}