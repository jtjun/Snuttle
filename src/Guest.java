import java.util.*;

public class Guest {
    private int timeS, timeD, requestT, dist;
    private Station placeS, placeD;
    private sched[] request = new sched[2];
    private int nums = 1;
    private int shuttleName;

    public Guest(int times, Station places, int timed, Station placed, int requestt){
        timeS = times;
        placeS = places;
        timeD = timed;
        placeD = placed;
        requestT = requestt;
        dist = Simulator.map.getDistance(placeS, placeD);
        setRequest(); // input type station
    }

    public void setRequest(){
        request[0] = new sched(timeS, placeS, nums);
        request[1] = new sched(timeD, placeD, -nums, dist, requestT);
    }
    public sched getDrop(){return new sched(timeD, placeD, -nums, dist, requestT);}
    public int getTimeS(){ return timeS; }
    public int getTimeD(){ return timeD; }
    public int getRequestT(){ return requestT; }
    public Station getPlaceS(){ return placeS; }
    public Station getPlcaeD(){ return placeD; }
}

class Request{
    private int time=0;
    private int runT = Simulator.MAX_TIME;
    private int staN;
    private Schedule[][] R;
    private ArrayList<Guest> guests;
    Map map;

    Request(ArrayList<Guest> guestsi, Map mapi){
        guests = guestsi;
        map = mapi;
        staN = map.getNumStations();
        R = new Schedule[runT][staN];

        for(int i=0; i<runT; i++){
            for(int j=0; j<staN; j++){
                R[i][j] = new Schedule();
            }
        } for(int i=0; i<guests.size(); i++){
            Guest g = guests.get(i);
            Schedule s = R[g.getTimeS()][map.getIndex(g.getPlaceS().getName())];
            s.addSchedule(g.getDrop());
        }
    }
    public void makeUp(int after){
        if(after == runT) return ;
        for(int i=0; i<staN; i++){
            Schedule beforeS = R[after-1][i];
            beforeS.Waiting();
            int l = beforeS.getNumSched();
            R[after][i].mergeWith(beforeS);
        }
    }
    // ti, sta's schedule
    public Schedule scheduleTS(int ti, Station sta){ return R[ti][map.getIndex(sta.getName())]; }
    // ti's whole sta's guest
    public Schedule[] scheduleT(int ti){ return R[ti]; }
    public Schedule[] scheduleS(Station sta){
        Schedule[] timel = new Schedule[runT];
        for(int i=0; i< runT; i++){
            timel[i] = R[i][map.getIndex(sta.getName())];
        } return timel;
    } // sta's guest
    public int howMany(int ti, Station sta){ // ti, sta's number of guest
        Schedule s = R[ti][map.getIndex(sta.getName())];
        return s.getNumSched();
    }
    public String printingAll(int pr){
        String str = "";
        for(int i =0; i<runT; i++){
            str += (i+" : ");
            for(int j=0; j< staN; j++){
                Schedule s = R[i][j];
                str += s.printing(pr);
            } str += "\n";
        } return str;
    }
    public String printingAtT(int t, int pr){ // sched> W= With<
        Schedule[] st = scheduleT(t);
        String str ="";
        if(pr>0) str += "\nMissed people's location (Destination / Demand time)";
        else if(pr==0) str += "\nMissed people's location (Waiting time / Unfair = true)";
        else str += "\nMissed people's location (Destination / Demand time / Source time)";
        str += " At "+t+"\n";
        for(int i =0; i<staN; i++){
            str += (map.getStation(i).getName()+" Station,\t");
            Schedule s = st[i];
            str += s.printing(pr)+"\n";
        } return str;
    }
    public String printingAtS(Station sta, int pr){
        Schedule[] st = scheduleS(sta);
        String str = "At "+sta.getName()+" : ";
        for(int i =0; i<runT; i++){
            str += ("When "+i+" : ");
            Schedule s = st[i];
            str += s.printing(pr)+"\n";
        } return str + "\n";
    }
    public int checkUnfair(int t){ // sched> W= With<
        Schedule[] st = scheduleT(t);
        int unfair=0;
        for(int i =0; i<staN; i++){
            Schedule StaSchedi = st[i];
            String sourceS = map.getStation(i).getName();
            for(int j=0; j<StaSchedi.getNumSched(); j++){
                sched reqstj = StaSchedi.whatIthSched(j);
                int realdt = map.getDistance(sourceS, reqstj.getStation().getName());
                if(reqstj.getDemandT() < realdt) {
                    reqstj.unfair();
                    unfair++;
                } // checking unfairness and tell it's unfair to sched.
            }
        } return unfair;
    }
}