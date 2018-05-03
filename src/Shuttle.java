public class Shuttle {
    private int x, y;
    private int time;
    private Schedule S;

    private sched to;
    private sched from;

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

    public int[] whereAtT(int timei, Map map) {
        sched toi = S.whatIthSched(S.whatTimeS(timei));
        sched fromi = S.whatIthSched(S.whatTimeS(timei)-1);
        int needt = map.getDistance(fromi.getStation().getName(), toi.getStation().getName());
        int[] dn = {timei-fromi.getTime(), toi.getTime()-timei, needt};
        return dn; // := {passed time, remain time, from-to require time}
    } // pass + remain = schedule time from-to

    public int[] whereNowT(Map map){
        return whereAtT(time, map);
    }

    public Station[] whereAtP(int timei, Map map) {
        sched toi = S.whatIthSched(S.whatTimeS(timei));
        sched fromi = S.whatIthSched(S.whatTimeS(timei) - 1);
        Station[] ft = {from.getStation(), to.getStation()};
        return ft; // := {fromP, toP}
    }

    public Station[] whereNowP(Map map){
        return whereAtP(time, map);
    }

    public void setXY(int xi, int yi) {
        x = xi;
        y = yi;
    }
    public void setSchedule(Schedule Si) {
        Si.removeAfterT(time);
        S = Si;
    } // when update schedule

    public void setTime(int timei) {
        time = timei;
        S.removeAfterT(time);
    } // when update time

    public int getX() { return x; }
    public int getY() { return y; }
    public int getTime() { return time; }
    public Schedule getSchedule() { return S; }
}