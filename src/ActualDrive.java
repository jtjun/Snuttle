import java.util.*;
import java.io.*;

public class ActualDrive {
    private int runT = Simulator.MAX_TIME;
    private Shuttle[] shuttles;
    private Schedule[] S;
    private Request R;

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

    public void Simulate(int runT, PrintStream printer_sd, PrintStream printer_wt, PrintStream printer_dl){
        int time=0;

        for(time=0; time<runT; time++){
            for(int i=0; i<shutN; i++){


            } // doing time's situation

            // Getting data for wait time
            for(Shuttle shuttle : shuttles){

            }
            R.makeUp(time+1);
        }
        // Gettting data for number of people in shuttle
        for(int i = 0; i < shutsPN.length; i++){
            printer_sd.print(i);
            for(int j = 0; j < shutsPN[i].length; j++){
                printer_sd.print(","+shutsPN[i][j]);
            }
            printer_sd.println();
        }
    }

    public void shutiDriveT(int i, int time){
        Shuttle shuti = shuttles[i];
        sched schedt = shuti.whereTo(time);
        int toTime =schedt.getTime();
        Station toSta = schedt.getStation();
        Schedule nextSta = R.scheduleTS(toTime, toSta);

        int numOfPeop = nextSta.getNumSched();

        if(toTime == time) // ㅅㅏ람 태워야 함(R의 상황 변경)
    }

    public void allocate(Shuttle shut,sched sche, int idx){ // goBefore is true(>0)
        sched shutsc = shut.getSchedule().whatIthSched(idx);
        sche.setTime(shutsc.getTime());
        shut.takeSched(sche);
    }
}