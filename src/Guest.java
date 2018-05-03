public class Guest {
    private int timeS, timeD;
    private Station placeS, placeD;
    private sched[] request = new sched[2];
    private int nums = 1;

    public Guest(int times, Station places, int timed, Station placed, Map map, int n){
        timeS = times;
        placeS = places;
        timeD = timed;
        placeD = placed;
        nums = n;
        setRequest(); // input type station is different
    }
    public Guest(int times, String places, int timed, String placed, Map map, int n){
        timeS = times;
        placeS = map.getStation(places);
        timeD = timed;
        placeD = map.getStation(placed);
        nums = n;
        setRequest();
    }

    public void setRequest(){ // counter request
        request[0] = new sched(timeS, placeS, nums);
        request[1] = new sched(timeD, placeD, -nums);
    }
    public void counterTS(int t){ request[0].setTime(t);}
    public void counterTD(int t){ request[1].setTime(t);}

    public sched getRide(){return request[0];}
    public sched getDrop(){return request[1];}

    public int getTimeS(){ return timeS; }
    public int getTimeD(){ return timeD; }
    public Station getPlaceS(){ return placeS; }
    public Station getPlcaeD(){ return placeD; }
    public int getNums(){ return nums; }
}