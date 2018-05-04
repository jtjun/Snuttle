import java.util.*;

public class ActualDrive {
    private int runT = Simulator.MAX_TIME;
    private Shuttle[] shuttles;
    private Schedule[] S;
    private Request R;


    private int shutn;
    private int stan;
    private Map map;

    ActualDrive(Shuttle[] shuttlesi, ArrayList<Guest> guests, Map mapi){
        shuttles = shuttlesi;
        shutn = shuttles.length;

        map = mapi;
        stan = map.getNumStations();
        S = new Schedule[shutn];
        for(int i=0; i<shutn; i++){
            S[i] = shuttles[i].getSchedule();
        }
    }

    public void syncRandS(Shuttle shut,sched sche){

    }
    public void Simulate(int time){

    }
}