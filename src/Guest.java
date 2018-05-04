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

    public void counterTS(int t){ request[0].setTime(t);} // counter request
    public void counterTD(int t){ request[1].setTime(t);} // counter request

    public sched getRide(){return request[0];}
    public sched getDrop(){return request[1];}

    public int getTimeS(){ return timeS; }
    public int getTimeD(){ return timeD; }
    public Station getPlaceS(){ return placeS; }
    public Station getPlcaeD(){ return placeD; }
    public int getNums(){ return nums; }
}