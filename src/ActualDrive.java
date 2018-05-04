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
        shuttles = shuttlesi; // scheduled shuttles
        shutn = shuttles.length;

        map = mapi;
        stan = map.getNumStations();
        S = new Schedule[shutn];
        for(int i=0; i<shutn; i++){
            S[i] = shuttles[i].getSchedule();
        } R = new Request(guests, map);
    }

    public void syncRS(Shuttle shut,sched sche){
        // sched 에 맞춰서 shut 의 sched의 num 을 변화시키고
        // shut에 맞춰서 sche 를 변형시키고
        // t 에 따라서 기다린 시간 갱신
    }
    public void Simulate(int time){

    }
}