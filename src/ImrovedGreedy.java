import java.util.*;

public class ImrovedGreedy {
    private weight[] weights = new weight[Simulator.staN*Simulator.staN];
    private int fixed;
    // Station Source  - Station Destination
    ImrovedGreedy(){
        fixed=0;
    }

    public void setIGreddy(Shuttle[] shuttles, Map map, ArrayList<Guest> guests, int fixedshuttle){
        fixed = fixedshuttle;
        for(int i=0; i<Simulator.staN; i++){
            for(int j=0; j<Simulator.staN; j++){
                int d = map.getDistance(i,j);
                weight weighti =  new weight(i,j,d);
                weights[i*Simulator.staN+j] = weighti;
            }
        } int l = guests.size();
        for(int i=0; i<l; i++){
            Guest person = guests.get(i);
            int staS = map.getIndex(person.getPlaceS().getName());
            int staD = map.getIndex(person.getPlcaeD().getName());
            int timeS = person.getTimeS();
            weights[staS*Simulator.staN+staD].addnums(timeS);
        } // give guest's all inform

        Arrays.sort(weights, new Comparator<weight>() {
            @Override
            public int compare(weight o1, weight o2) {
                final int n1 = o1.getNums();
                final int n2 = o2.getNums();
                return -Integer.compare(n1, n2);
            } // sort by Nums
        }); // we can modify the norm of sort

        int greedyshuttle = shuttles.length-fixedshuttle;
        Shuttle[] IGredShuts = new Shuttle[greedyshuttle];
        for(int i=0; i<greedyshuttle; i++){
            IGredShuts[i] = shuttles[i];
        }
        for(int i=0; i<greedyshuttle; i++){
            IGredShuts[i] = setIGreedyEach(i, map);
        }
        Shuttle[] CircShuts = new Shuttle[fixedshuttle];
        for(int i=greedyshuttle; i<shuttles.length; i++){
            CircShuts[i-greedyshuttle] = shuttles[i];
        } CircularSchedule.setCircularSchedule(CircShuts, map);

        for(int i=0; i<fixedshuttle; i++){
            shuttles[i] = CircShuts[i];
        } for(int i=0; i<greedyshuttle; i++){
            shuttles[i+fixedshuttle] = IGredShuts[i];
        } // set shuttles schedule
    }


    // 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, ...
    public Shuttle setIGreedyEach(int rank, Map map){
        int l = weights.length-1;
        Schedule schedule = new Schedule();
        int t = 0;
        int coveredSta = rank+2;
        if(coveredSta > Simulator.staN/2) coveredSta = (Simulator.staN/2)+1;

        for (int i = 0; i < coveredSta && (t < Simulator.MAX_TIME-1); i++) {
            if(weights[i].getNums()==0) break;
            int staSidx = weights[i].getStaS();
            int staDidx = weights[i].getStaS();
            int dist = map.getDistance(staSidx, staDidx);

            if(i!=0) { // add distance from previous station
                sched s = schedule.whatIthSched(schedule.getNumSched() - 1);
                int staPrevious = map.getIndex(s.getStation().getName());
                t += map.getDistance(staPrevious, staSidx);
            }
            schedule.addSchedule(t, map.getStation(staSidx), 0);
            t += dist;
            schedule.addSchedule(t, map.getStation(staDidx), 0);
        }
        return (new Shuttle(0,0, 0, schedule,fixed+rank, map));
    }
}

class weight{
    private int staS;
    private int staD;
    private int nums;
    private int dist;
    private int hotTimes;

    weight(int staSi, int staDi, int d){
        staS = staSi;
        staD = staDi;
        nums =0;
        if(d == 0) dist = -1;
        else dist = d;
        hotTimes=0;
    }
    public void addnums(int timeS){
        nums+=1;
        hotTimes += timeS;
    }
    public String printing(){
        return "staS "+staS+" staD "+staD+" nums "+nums;
    }

    public int getStaS(){return staS;}
    public int getStaD(){return staD;}
    public int getDist(){return dist;}
    public int getHotTime(){return hotTimes/nums;}
    public int getNums(){return nums;}
    public int getNpD(){return (nums/dist);}
}