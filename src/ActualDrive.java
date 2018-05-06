import java.util.*;
import java.io.*;

public class ActualDrive {
    private int runT = Simulator.MAX_TIME;
    private int userN = Simulator.userN;
    private int shutN, staN;
    private int serviced=0;

    private Shuttle[] shuttles;
    private Schedule[] S;
    private Request R;
    private Map map;
    private String type;

    private ArrayList<Integer> early = new ArrayList<>();
    private ArrayList<Integer> wait = new ArrayList<>();
    private int[][] shutsPN;


    ActualDrive(Shuttle[] shuttleS, Request Ri, Map mapi, String typei){
        shuttles = shuttleS; // scheduled shuttles
        shutN = shuttles.length;
        map = mapi;
        staN = map.getNumStations();
        S = new Schedule[shutN];
        for(int i=0; i<shutN; i++){
            S[i] = shuttles[i].getSchedule();
        } R = Ri;
        shutsPN = new int[shutN][runT];
        type = typei;
    }

    public void Simulate() throws FileNotFoundException {
        //String strR = "Initial Request's state :\n"+R.printing()+"\nAfter Request's state \n";
        for(int time=0; time<runT; time++){
            for(int i=0; i<shutN; i++){
                shutsPN[i][time] = shutiDriveT(i, time);
            } // simulate time's situation
            R.makeUp(time+1);
        }
        /*PrintStream outr = new PrintStream(new File("Request Change.txt"));
        outr.print(strR + R.printing());
        outr.close();*/
        PrintStream shuttlemax = new PrintStream(new File(type +" Shuttle Max.txt"));

        for(int i=0; i<shutN; i++){
            shuttlemax.print(i);
            for(int j=0; j<runT; j++){
                shuttlemax.print(","+shutsPN[i][j]);
            } shuttlemax.println();
        } shuttlemax.println(serviced+", "+userN);
        shuttlemax.close();
    }

    public int shutiDriveT(int i, int t){
        Shuttle shuti = shuttles[i];
        int idxt = shuti.whereToIdx(t);
        sched schedTo = shuti.whatIthS(idxt); // shut's destination schedule
        if(schedTo.getTime() > t) return shuti.getNums();
        if(schedTo.getTime() == t){ // shuti arrived at next station
            shuti.dropS(idxt); // first, drop the people
            if(shuti.getEmpty()==0) return shuti.getNums();
            if(shuti.getEmpty()<=0) {
                System.out.println("ERROR : At "+t+" Shuttle"+i+" has negative people "+shuti.getNums());
                return shuti.getNums();
            } else{ // shuti's empty > 0
                Schedule guestsR =  R.scheduleTS(t, schedTo.getStation());
                for(int a=0; (a<guestsR.getNumSched()) && (shuti.getEmpty()>0); a++) {
                    shuti.errorCheck(t);
                    sched dropR = guestsR.whatIthSched(a);
                    int idx = shuti.goBefore(t, dropR); // guests' drop index
                    if (idx > 0) { // shuti will go to guests's drop destination.
                        shuti.rideS(); // second, take a person
                        shuti.whatIthS(idx).setNums( shuti.whatIthS(idx).getNums()-1 );
                        early.add(dropR.getTime()-shuti.whatIthS(idx).getTime());
                        guestsR.removeSchedule(a);
                        serviced++;
                        a--;
                    } // A person ride a shuti
                } // shuti is full or there are no guest who can ride this shuti
            } shuti.errorCheck(t);
        } else { // schedule to is past error
            if(!schedTo.equals(shuti.whatIthS(idxt-1))){
                System.out.println("ERROR : Schedule To searching error! "+t);
            } return shuti.getNums();
        } shuti.errorCheck(t);
        return shuti.getNums();
    }
}