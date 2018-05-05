import java.util.*;

public class Guest {
    private int timeS, timeD, rideT;
    private Station placeS, placeD;
    private sched[] request = new sched[2];
    private int nums = 1;
    private int shuttleName;

    public Guest(int times, Station places, int timed, Station placed){
        timeS = times;
        placeS = places;
        timeD = timed;
        placeD = placed;
        setRequest(); // input type station
    }

    public void setRideT(int t){ rideT = t; }
    public void setNums(int n){ nums = n; }
    public void setRequest(){
        request[0] = new sched(timeS, placeS, nums);
        request[1] = new sched(timeD, placeD, -nums);
    }

    private void setShuttleName(int n ){ shuttleName = n;}
    private int getShuttleName(){return shuttleName;}
    public int getRideT(){ return rideT; }
    public int getWaitT(){ return rideT - timeS; }

    public void setTS(int t){ request[0].setTime(t);} // counter request
    public void setTD(int t){ request[1].setTime(t);} // counter request

    public sched getRide(){return request[0];}
    public sched getDrop(){return request[1];}

    public int getTimeS(){ return timeS; }
    public int getTimeD(){ return timeD; }
    public Station getPlaceS(){ return placeS; }
    public Station getPlcaeD(){ return placeD; }
    public int getNums(){ return nums; }
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
        staN = map.getNumStations();
        R = new Schedule[runT][staN];
        map = mapi;

        for(int i=0; i<guests.size(); i++){
            Guest g = guests.get(i);
            Schedule s = R[g.getTimeS()][map.getIndex(g.getPlaceS().getName())];
            s.addSchedule(g.getDrop());
        }
    }
    public void makeUp(int after){
        for(int i=0; i<staN; i++){
            Schedule beforeS = R[after-1][i];
            Schedule afterS = R[after][i];
            int l = beforeS.getNumSched();
            afterS.mergeWith(beforeS);
        }
    }
    public int howMany(int ti, Station sta){
        Schedule s = R[ti][map.getIndex(sta.getName())];
        return s.getNumSched();
    } // ti, sta's number of guest
    public Schedule scheduleTS(int ti, Station sta){
        return R[ti][map.getIndex(sta.getName())];
    } // ti, sta's schedule
    public Schedule[] scheduleT(int ti){
        return R[ti];
    } // ti's whole sta's guest
    public Schedule[] scheduleS(Station sta){
        Schedule[] timel = new Schedule[runT];
        for(int i=0; i< runT; i++){
            timel[i] = R[i][map.getIndex(sta.getName())];
        } return timel;
    } // sta's guest
}