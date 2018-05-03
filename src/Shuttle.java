public class Shuttle {
    private int x, y;
    private int max = 6;
    private int nums = 0;

    private Schedule S;
    private int time;

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

    public sched[] whatDoAt(int timei) {
        sched toi = S.whatIthSched(S.whatTimeS(timei));
        sched fromi = S.whatIthSched(S.whatTimeS(timei) - 1);
        sched[] ft = {fromi, toi};
        return ft; // := {from schedule, to schedule}
    }

    public int[] whereAtT(int timei, Map map) {
        sched[] ft = whatDoAt(timei);
        sched fromi = ft[0];
        sched toi = ft[1];
        int needt = map.getDistance(fromi.getStation().getName(), toi.getStation().getName());
        int[] dn = {timei-fromi.getTime(), toi.getTime()-timei, needt};
        return dn; // := {passed time, remain time, from-to require time}
    } // pass + remain = schedule time from-to

    public void driveDo(int timei, Map map){
        int[] dn = whereAtT(timei, map);
        sched[] Do = whatDoAt(timei);
        int empty = getEmpty();
        int num = Do[1].getNums();

        if(dn[0]>=dn[2]) {
            if(empty >= num){ // contain num<0 (drop off)
                nums += num;
                Do[1].getStation().rideDrop(num);
                Do[1].setNums(0);
            } else if(empty > 0 && num > 0){
                nums += empty;
                Do[1].setNums(num-empty);
                Do[1].getStation().rideDrop(empty);
            } // do at timei
        } setTime(timei);
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