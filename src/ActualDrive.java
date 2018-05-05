import java.util.*;
import java.io.*;

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

    public void Simulate() throws FileNotFoundException {
        int time=0;
        for(time=0; time<runT; time++){
            for(int i=0; i<shutN; i++){
                shutsPN[time][i] = shutiDriveT(i, time);
            } // doing time's situation
            R.makeUp(time+1);
        }
        PrintStream out = new PrintStream(new File("out.txt"));
        out.print(R.printing());
        out.close();
    }

    public int shutiDriveT(int i, int time){
        Shuttle shuti = shuttles[i];
        sched schedTo = shuti.whereTo(time);

        int toTime = schedTo.getTime();
        if(toTime > time) return shuti.getNums(); // shuttle is on road
        // below run when shuttle is on station
        Station toSta = schedTo.getStation();
        Schedule rNextSta = R.scheduleTS(toTime, toSta);

        int nOfP = rNextSta.getNumSched();

        for (int a = 0; a < nOfP; a++) {
            if(shuti.getEmpty() <=0) break;
            sched guR = rNextSta.whatIthSched(a); //guest's Request
            int idx = shuti.goBefore(toTime, guR); // toTime : arrive time of next Station
            if (idx > 0) {
                shuti.whereTo(time).setNums(schedTo.getNums() + 1); // take a person
                int dt = allocate(shuti, guR, idx);
                R.scheduleTS(toTime, toSta).removeSchedule(guR);
                early.add(dt);
            }
        } // before arrive toStation it decide schedule
        return shuti.Driving(time); // arrive at station
    }

    public int allocate(Shuttle shut,sched guR, int idx){ // goBefore is true(>0)
        int timeD = guR.getTime();
        sched shutsc = shut.getSchedule().whatIthSched(idx);
        guR.setTime(shutsc.getTime()); // guest's request is modified
        int DropT = guR.getTime();
        shut.rideGuest(guR, idx);
        return timeD - DropT;
    }
}