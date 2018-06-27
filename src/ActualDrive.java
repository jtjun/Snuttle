import java.util.*;
import java.io.*;

public class ActualDrive {
    private int runT = Simulator.MAX_TIME;
    private int userN = Simulator.userN;
    private int shutN, staN, gred;
    private int serviced=0;
    private boolean goThere = Simulator.goThereS;

    private Shuttle[] shuttles;
    private Schedule[] S;
    private Request R;
    private Map map;
    private String type;

    private ArrayList<Integer> early = new ArrayList<>();
    private ArrayList<Integer> wait = new ArrayList<>();
    private int[][] shutsPN;
    private String[] shutlocate;


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
        shutlocate = new String[shutN-Simulator.fixedshuttle];
        for(int i=0; i<shutlocate.length; i++){
            shutlocate[i] = "";
        }
    }

    public int Simulate(boolean monit) throws FileNotFoundException {
        for(int time=0; time<runT; time++){
            for(int i=0; i<shutN; i++){
                shutsPN[i][time] = shutiDriveT(i, time, monit);
                shuttles[i].errorCheck(time);
            } // simulate time's situation
            R.makeUp(time+1);
        }

        // Printing the result!
        PrintStream shuttlemax = new PrintStream(new File("Shuttle Max "+type +".csv"));
        shuttlemax.println(type+"\tServiced: "+serviced+", Unfair: "+R.checkUnfair(runT-1)+", UserN :"+userN);
        for(int i=0; i<shutN; i++){
            shuttlemax.print("Shuttle"+i+",\t"+shutsPN[i][0]);
            for(int j=1; j<runT; j++){
                shuttlemax.print(","+shutsPN[i][j]);
            } shuttlemax.println();
        }
        shuttlemax.println("\nHow early,\t"+sumup(early)+"/"+early.size()+"("+(sumup(early)/early.size())+"),\t"+ ToString(early));
        shuttlemax.println("\nHow wait,\t"+sumup(wait)+"/"+wait.size()+"("+(sumup(wait)/wait.size())+"),\t"+ ToString(wait));
        shuttlemax.print("\n"+R.printingAtT(runT-1, 1)); // sched> W= With<
        for(int a=0; a<shutlocate.length; a++){
            shuttlemax.print("\n"+(a+Simulator.fixedshuttle)+" "+shutlocate[a]);
        }
        shuttlemax.close();

        PrintStream pTperD = new PrintStream(new File("Time Per Distance "+type+".csv"));
        double[] TperD = {};
        pTperD.print(type+" Time Per Distance ");
        for(int i=0; i < shuttles.length; i++){
            double[] TperDi = shuttles[i].getTperD();
            double[] temp = new double[TperD.length+TperDi.length];
            System.arraycopy(TperD, 0, temp, 0, TperD.length);
            System.arraycopy(TperDi, 0, temp, TperD.length, TperDi.length);
            TperD = temp;
        } Arrays.sort(TperD);
        pTperD.print(TperD.length);
        pTperD.println(" / "+getPeopleN(shuttles));

        if(TperD.length>0) pTperD.println(ToString(TperD));
        if(TperD.length!=serviced) System.out.println("ERROR : Serviced and reported T/D number are differ.");
        pTperD.close();

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
                    +" "+schedTo.printing(1)+"\nGuest number "+shuti.getNums());
            shuti.dropS(t, idxt, goThere, monit); // first, drop the people who want get down here
            if((gred!=0) && (i>=Simulator.fixedshuttle)) {
                shutlocate[i-Simulator.fixedshuttle] += "("+schedTo.getStation().getName()+",at "+t+") ";
            }
            if((gred!=0) && ((t-shuti.getRefresh()) >= Math.abs(gred))){ // if it's shuttle is greedy
                shuti.setRefresh(t); // it's time to refresh schedule
                Schedule Peopl = shuti.getPeople();
                Peopl.outTransfer(t);
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
                } shuti.getOutAll();  // After all passengers are get out,
                if(gred<0)TrampSteamerGreedy.setIGreedyEach(shuttles, i, t, R);
                if(gred>0)PredictionTSGreedy.setPTSGreedyEach(shuttles, i, t, R); // Refresh Schedule
                shuti = shuttles[i];
                shuti.setRefresh(t);
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
                    int idx =0;
                    if(goThere) idx = shuti.goThere(t, person);
                    else idx = shuti.goBefore(t, person); // guests' drop index
                    if (idx >= 0) { // shuti will go to guests's drop destination.
                        shuti.whatIthS(idx).setNums( shuti.whatIthS(idx).getNums()-1 ); //second, set drop schedule
                        person.setDropIdx(idx);// tell person where to drop
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
            if(monit) System.out.println("Leave with "+shuti.getNums()+" people\n");

        } else { // schedule to is past error
            if(!schedTo.equals(shuti.whatIthS(idxt-1))){
                System.out.println("ERROR : Schedule 'To' searching error! "+t);
            } return shuti.getNums();
        } shuti.errorCheck(t);
        shuttles[i] = shuti;
        return shuti.getNums();
    }
    public int getPeopleN(Shuttle[] shuts){
        int l = shuts.length;
        if(l<1) {
            System.out.println("ERROR : There are no serviced person. (getPeopleN");
            return 0;
        }
        int num=0;
        for(int i=0; i<l; i++){
            num += shuts[i].getPeople().getNumSched();
        } return num;
    }

    public int sumup(ArrayList<Integer> ar){
        int sum = 0;
        int l = ar.size();
        if(l<1) {
            System.out.println("ERROR : There are no serviced person. (sumup)");
            return 0;
        }
        for(int i=0; i<l; i++) sum += ar.get(i);
        return sum;
    }
    public String ToString(ArrayList<Integer> ar){
        String str="";
        int l = ar.size();
        if(l<1) {
            System.out.println("ERROR : There are no serviced person. (ToString)");
            return "";
        }
        str = ""+ar.get(0);
        for(int i=1; i<l; i++) str+=","+ar.get(i);
        return str;
    }
    public String ToString(double[] ar){
        String str="";
        int l = ar.length;
        if(l<1) {
            System.out.println("ERROR : There are no serviced person. (ToString)");
            return "";
        }
        str = ""+ar[0];
        for(int i=1; i<l; i++) str+=","+ar[i];
        return str;
    }
}