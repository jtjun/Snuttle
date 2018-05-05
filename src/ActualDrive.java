import java.util.*;

public class ActualDrive {
    private int runT = Simulator.MAX_TIME;
    private Shuttle[] shuttles;
    private Schedule[] S;
    private Request R;
    private ArrayList<Integer> early = new ArrayList<>();

    private int[][] shutsPN;
    private int shutN;
    private int staN;
    private Map map;

    ActualDrive(Shuttle[] shuttleS, ArrayList<Guest> guests, Map mapi){
        shuttles = shuttleS; // scheduled shuttles
        shutN = shuttles.length;
        map = mapi;
        staN = map.getNumStations();
        S = new Schedule[shutN];
        for(int i=0; i<shutN; i++){
            S[i] = shuttles[i].getSchedule();
        } R = new Request(guests, map);
        shutsPN = new int[runT][shutN];
    }

    public void Simulate(){
        int time=0;
        for(time=0; time<runT; time++){
            for(int i=0; i<shutN; i++){


            } // doing time's situation
            R.makeUp(time+1);
        }
    }

    public void shutiDriveT(int i, int time){
        Shuttle shuti = shuttles[i];
        sched schedTo = shuti.whereTo(time);

        int toTime = schedTo.getTime();
        Station toSta = schedTo.getStation();

        Schedule rNextSta = R.scheduleTS(toTime, toSta);

        int nOfP = rNextSta.getNumSched();
        int emptyN = shuti.getEmpty();

        for(int a=0; a<emptyN; a++){
            for(int b=0; b<nOfP; b++){
                sched guR = rNextSta.whatIthSched(b); //guest's Request
                int idx = shuti.goBefore(toTime, guR); // toTime : arrive time of next Station
                if(idx > 0) {
                    int dt = allocate(shuti, guR, idx);
                    early.add(dt);
                }
            }
        }
        if(toTime == time) shuti.Driving(time,1);
            // ㅅㅏ람 태워야 함(R의 상황 변경), driving call 해서 shut 상황도 변경
    }

    public int allocate(Shuttle shut,sched guR, int idx){ // goBefore is true(>0)
        int timeD = guR.getTime();
        sched shutsc = shut.getSchedule().whatIthSched(idx);
        guR.setTime(shutsc.getTime()); // guest's request is modified
        int DropT = guR.getTime();
        shut.takeSched(guR, idx);
        return timeD - DropT;
    }
}