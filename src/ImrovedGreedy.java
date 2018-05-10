import java.util.*;

public class ImrovedGreedy {
    private static weight[] weights = new weight[Simulator.staN*Simulator.staN];
    // Station Source  - Station Destination

    public static void setIGreddy(Shuttle[] shuttles, Map map, ArrayList<Guest> guests, int fixedshuttle){
        for(int i=0; i<Simulator.staN; i++){
            for(int j=0; j<Simulator.staN; j++){
                int d = map.getDistance(i,j);
                weights[i*Simulator.staN+j] = new weight(i,j,d);
            }
        }
        int l = guests.size();
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
                return Integer.compare(n1, n2);
            } // sort by Nums
        }); // we can modify the norm of sort

        int greedyshuttle = shuttles.length-fixedshuttle;
        Shuttle[] IGredShuts = new Shuttle[greedyshuttle];
        for(int i=0; i<greedyshuttle; i++){
            IGredShuts[i] = shuttles[i];
        }
        for(int i=0; i<greedyshuttle; i++){
            setIGreedyEach(IGredShuts[i], i);
        }
        Shuttle[] CircShuts = new Shuttle[fixedshuttle];
        for(int i=greedyshuttle; i<shuttles.length; i++){
            CircShuts[i] = shuttles[i];
        } CircularSchedule.setCircularSchedule(CircShuts, map);

        for(int i=0; i<greedyshuttle; i++){
            shuttles[i] = IGredShuts[i];
        } for(int i=0; i<fixedshuttle; i++){
            shuttles[i+greedyshuttle] = CircShuts[i];
        } // set shuttles schedule
    }

    public static void setIGreedyEach(Shuttle shuti, int rank){

    }
}

class weight{
    private static int staS;
    private static int staD;
    private static int nums;
    private static int dist;
    private static int hotTimes;

    weight(int staSi, int staDi, int d){
        staS = staSi;
        staD = staDi;
        nums =0;
        if(d == 0) dist = -1;
        else dist = d;
        hotTimes=0;
    }

    public static void addnums(int timeS){
        nums+=1;
        hotTimes += timeS;
    }
    public static int getHotTime(){return hotTimes/nums;}
    public static int getNums(){return nums;}
    public static double getNpD(){return ((double)(nums/dist));}
}