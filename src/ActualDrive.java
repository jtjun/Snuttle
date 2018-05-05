import java.util.*;
import java.io.*;

public class ActualDrive {
    private int runT = Simulator.MAX_TIME;
    private Shuttle[] shuttles;
    private Schedule[] S;
    private Request R;
    private ArrayList<Integer> early = new ArrayList<>();
    private ArrayList<Integer> wait = new ArrayList<>();

    private int[][] shutsPN;
    private int shutN, staN;
    private int userN = Simulator.userN;
    private int serviced=0;
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
        shutsPN = new int[shutN][runT];
    }

    public void Simulate() throws FileNotFoundException {
        /*String strR = "Initial Request's state :\n"+R.printing()+"\nAfter Request's state \n";*/
        for(int time=0; time<runT; time++){
            for(int i=0; i<shutN; i++){
                shutsPN[i][time] = shutiDriveT(i, time);
            } // doing time's situation
            R.makeUp(time+1);
        }
        /*PrintStream outr = new PrintStream(
                new File("Request Change.txt"));
        outr.print(strR + R.printing());
        outr.close();*/

        PrintStream outs = new PrintStream(
                new File("Shuttle Change.txt"));
        String strS = "";
        for(int i=0; i<shutN; i++){
            strS += "Shuttle"+i+"'s : ";
            for(int j=0; j<runT; j++){
                strS += j+"["+ shutsPN[i][j] +"]\t";
            } strS += "\n";
        } outs.println(strS +"Earlier drop : "+early.toString());
        outs.println("We serviced "+serviced+" of people");
        outs.println("We can't take "+(userN-serviced)+" of people");
        outs.close();
    }

    public int shutiDriveT(int i, int time){
        Shuttle shuti = shuttles[i];
        sched schedTo = shuti.whereTo(time);

        int toTime = schedTo.getTime();
        if(toTime > time) return shuti.getNums(); // shuttle is on road
        // below run when shuttle is on station
        Station toSta = schedTo.getStation();
        Schedule rNextSta = R.scheduleTS(toTime, toSta);

        for (int a = 0; a < rNextSta.getNumSched() && shuti.getEmpty()>0; a++) {
            sched guR = rNextSta.whatIthSched(a); //guest's Request
            int idx = shuti.goBefore(toTime, guR); // toTime : arrive time of next Station
            if (idx > 0) {
                schedTo.setNums(schedTo.getNums() + 1); // take a person
                int dt = allocate(shuti, guR, idx);
                rNextSta.removeSchedule(guR); // after allocate it should remove
                early.add(dt);
                serviced++;
            } // before arrive toStation it decide schedule
        } return shuti.Driving(time); // arrive at station
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