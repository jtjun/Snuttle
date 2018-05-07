import java.util.*;
import java.io.*;

public class ActualDrive {
    private int runT = Simulator.MAX_TIME;
    private int userN = Simulator.userN;
    private int shutN, staN, gred;
    private int serviced=0;
    private boolean goThere = true;

    private Shuttle[] shuttles;
    private Schedule[] S;
    private Request R;
    private Map map;
    private String type;

    private ArrayList<Integer> early = new ArrayList<>();
    private ArrayList<Integer> wait = new ArrayList<>();
    private int[][] shutsPN;


    ActualDrive(Shuttle[] shuttleS, Request Ri, Map mapi, String typei, int gredi){
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
        gred = gredi;
    }

    public int Simulate(boolean monit) throws FileNotFoundException {
        for(int time=0; time<runT; time++){
            for(int i=0; i<shutN; i++){
                shutsPN[i][time] = shutiDriveT(i, time, monit);
            } // simulate time's situation
            R.makeUp(time+1);
        }

        //Printing the result!
        PrintStream shuttlemax = new PrintStream(new File(type +" Shuttle Max.txt"));
        shuttlemax.println(type+"\tServiced: "+serviced+", Unfair: "+R.checkUnfair(runT-1)+", UserN :"+userN);
        for(int i=0; i<shutN; i++){
            shuttlemax.print("Shuttle"+i+"\t"+shutsPN[i][0]);
            for(int j=1; j<runT; j++){
                shuttlemax.print(","+shutsPN[i][j]);
            } shuttlemax.println();
        }
        shuttlemax.println("How early\t"+sumup(early)+"/"+early.size()+"\t"+ ToString(early));
        shuttlemax.println("How wait\t"+sumup(wait)+"/"+wait.size()+"\t"+ ToString(wait));
        //shuttlemax.print(R.printingAtT(runT-1, 0)); // sched> W= With<
        shuttlemax.close();

        PrintStream pTperD = new PrintStream(new File("Time Per Distance "+type+".txt"));
        pTperD.println(type+" Time Per Distance");
        for(int i=0; i < shuttles.length; i++){
            double[] TperDi = shuttles[i].getTperD();
            pTperD.print("Shuttle"+i+"\t");
            pTperD.println(ToString(TperDi));
        } pTperD.close();

        return serviced;
    }

    public int shutiDriveT(int i, int t, boolean monit){
        Shuttle shuti = shuttles[i];
        shuti.errorCheck(t);
        int idxt = shuti.whereToIdx(t);
        sched schedTo = shuti.whatIthS(idxt); // shut's destination schedule
        if(schedTo.getTime() > t) return shuti.getNums();

        if(schedTo.getTime() == t){// shuti arrived at next station
            if(monit) System.out.println("Shuttle arrived at station "+schedTo.getStation().getName()+", at " +t
                    +"\nGuest number "+shuti.getNums());
            shuti.dropS(t, idxt, goThere, monit); // first, drop the people who want get down here

            if((gred>0) && ((t-shuti.getRefresh()) >= gred)){ // if it's shuttle is greedy
                shuti.setRefresh(t); // it's time to refresh schedule
                Schedule Peopl = shuti.getPeople();
                Peopl.getOutt(t);
                R.scheduleTS(t, schedTo.getStation()).mergeWith(Peopl);
                // all people getting out from shuti, and stay station
                if(monit) System.out.println("\nIt's time to refresh "+t
                        +"\n"+Peopl.getNumSched()+" get out for transfer.");
                serviced -= Peopl.getNumSched();

                for(int k=0; k<Peopl.getNumSched(); k++){
                    sched person = Peopl.whatIthSched(k);
                    Integer rem = person.getEarly();
                    Integer wat = person.getWait();
                    early.remove(rem); // early and wait 's information are modified
                    wait.remove(wat);
                } shuti.getOutAll();  // After all passengers are get out,\
                // if(i>=Simulator.fixedshuttle) System.out.println(shuttles[i].getSchedule().printing(1));
                GreedySchedule.setGreedyScheduleForEach(shuttles, i, t); // Refresh Schedule
                // if(i>=Simulator.fixedshuttle) System.out.println(shuttles[i].getSchedule().printing(1));
                // System.out.println();
                //System.out.println("Schedule refreshed!\n");
                shuti = shuttles[i];
                if(monit) System.out.println("Schedule refreshed!\n");
            }
            if(shuti.getEmpty()==0) return shuti.getNums();
            else if(shuti.getEmpty()<0) {
                System.out.println("ERROR : At "+t+" Shuttle"+i+" has negative people "+shuti.getNums());
                return shuti.getNums();
            } else{ // shuti's empty > 0
                Schedule guestsR =  R.scheduleTS(t, schedTo.getStation());
                for(int a=0; (a<guestsR.getNumSched()) && (shuti.getEmpty()>0); a++) {
                    shuti.errorCheck(t);
                    sched person = guestsR.whatIthSched(a);
                    //int idx = shuti.goBefore(t, dropR); // guests' drop index
                    int idx = shuti.goThere(t, person);
                    if (idx >= 0) { // shuti will go to guests's drop destination.
                        shuti.whatIthS(idx).setNums( shuti.whatIthS(idx).getNums()-1 ); //second, set drop schedule
                        person.setRideT(t);// tell person ride time
                        // third save information on person
                        wait.add(person.getWait()); // save wait time
                        person.setEarly( person.getTime()-shuti.whatIthS(idx).getTime() );
                        early.add(person.getEarly()); // save how early

                        shuti.rideS(person, t, monit); // fourth, take a person
                        guestsR.removeSchedule(a);
                        serviced++;
                        a--; // A person ride a shuti
                    }
                } // shuti is full or there are no guest who can ride this shuti
            } shuti.errorCheck(t);
            if(monit) System.out.println("Leave with "+shuti.getNums()+"people\n");

        } else { // schedule to is past error
            if(!schedTo.equals(shuti.whatIthS(idxt-1))){
                System.out.println("ERROR : Schedule 'To' searching error! "+t);
            } return shuti.getNums();
        } shuti.errorCheck(t);
        shuttles[i] = shuti;
        return shuti.getNums();
    }

    public int sumup(ArrayList<Integer> ar){
        int sum = 0;
        int l = ar.size();
        for(int i=0; i<l; i++) sum += ar.get(i);
        return sum;
    }
    public String ToString(ArrayList<Integer> ar){
        String str="";
        int l = ar.size();
        str = ""+ar.get(0);
        for(int i=1; i<l; i++) str+=","+ar.get(i);
        return str;
    }
    public String ToString(double[] ar){
        String str="";
        int l = ar.length;
        str = ""+ar[0];
        for(int i=1; i<l; i++) str+=","+ar[i];
        return str;
    }
}