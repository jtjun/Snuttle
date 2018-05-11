import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class AnotherGreedy {
    private int[][] weights = new int[Simulator.staN][Simulator.staN];
    private int[][] invWeights = new int[Simulator.staN][Simulator.staN];
    private Integer[] indxReader = new Integer[Simulator.staN*Simulator.staN];
    private int fixed;
    // Station Source  - Station Destination
    AnotherGreedy(){
        fixed=0;
    }

    public void setAGreddy(Shuttle[] shuttles, Map map, ArrayList<Guest> guests, int fixedshuttle) {
        fixed = fixedshuttle;
        for (int i = 0; i < Simulator.staN; i++) {
            for (int j = 0; j < Simulator.staN; j++) {
                weights[i][j] = 0;
                invWeights[j][i] = 0;
                indxReader[i*Simulator.staN +j]= i*Simulator.staN +j;
            } // initialize
        }
        int l = guests.size();
        for(int i=0; i<l; i++){
            Guest person = guests.get(i);
            int staS = map.getIndex(person.getPlaceS().getName());
            int staD = map.getIndex(person.getPlcaeD().getName());
            weights[staS][staD]+=1;
            invWeights[staD][staS]+=1;
        } // give guest's all inform

        Arrays.sort(indxReader, new Comparator<Integer>() {
            @Override
            public int compare(Integer i,Integer j) {
                final int n1 = weights[i/Simulator.staN][i%Simulator.staN];
                final int n2 = weights[j/Simulator.staN][j%Simulator.staN];
                return - Integer.compare(n1, n2);
            }
        });

        int greedyshuttle = shuttles.length - fixedshuttle;
        Shuttle[] IGredShuts = new Shuttle[greedyshuttle];
        for (int i = 0; i < greedyshuttle; i++) {
            IGredShuts[i] = shuttles[i];
        }
        for (int i = 0; i < greedyshuttle; i++) {
            IGredShuts[i] = setAGreedyEach(i, map);
        }
        Shuttle[] CircShuts = new Shuttle[fixedshuttle];
        for (int i = greedyshuttle; i < shuttles.length; i++) {
            CircShuts[i - greedyshuttle] = shuttles[i];
        }
        CircularSchedule.setCircularSchedule(CircShuts, map);

        for (int i = 0; i < fixedshuttle; i++) {
            shuttles[i] = CircShuts[i];
        }
        for (int i = 0; i < greedyshuttle; i++) {
            shuttles[i + fixedshuttle] = IGredShuts[i];
        } // set shuttles schedule
    }

    public Shuttle setAGreedyEach(int rank, Map map) {
        Schedule schedule = new Schedule();
        int t = 0;
        int th = 0;

        while(t < Simulator.MAX_TIME) {
            //System.out.println("loop "+th);
            int idxr = indxReader[(rank/3)+th];
            int staSidx0 = idxr/Simulator.staN;
            int staDidx = idxr%Simulator.staN;
            //System.out.println(staSidx0 + " "+ staDidx);
            schedule.addSchedule(t, map.getStation(staSidx0), 0);
            t += map.getDistance(staSidx0, staDidx);
            schedule.addSchedule(t, map.getStation(staDidx), 0);
            int staSprev = staSidx0;
            int staDprev = staDidx;

            while (t < Simulator.MAX_TIME) {
                int staSidx = searchDIdx(staDidx, th);
                //System.out.println(staDidx + " "+ staSidx);
                if (staSidx == staSidx0 || staSidx == staSprev ) {
                    t += map.getDistance(staDidx, staSidx0);
                    th++;
                    break;
                } // if one roof is complete

                t += map.getDistance(staDidx, staSidx);
                schedule.addSchedule(t, map.getStation(staSidx), 0);

                staDprev = staDidx;
                staDidx = searchDIdx(staSidx, th);

                //System.out.println(staSidx + " "+ staDidx);
                t += map.getDistance(staSidx, staDidx);
                schedule.addSchedule(t, map.getStation(staDidx), 0);
                if (staDidx == staSidx0 || staDidx == staSprev || staDidx == staDprev) {
                    t += map.getDistance(staDidx, staSidx0);
                    th++;
                    break;
                }
                staSprev = staSidx;
            }
        }
        // can detact
        // s0 d0 s1 d1 s0 \ s0 d0 s1 d1 s0
        // s0 d0 s1 d1(s0)
        // s0 d0 s1 d1 s1 \d1 s1
        // s0 d0 s1 d1 s2 d2(s1) s3(d1) d4(s2) s5(d2) d6(s1)
        return (new Shuttle(0,0, 0, schedule,fixed+rank, map));
    }

    public int searchDIdx(int i, int th){
        int max=0;
        int l = weights[i].length;
        int[] iSweights =  Arrays.copyOf(weights[i], l);

        for(int k=0; k<=th; k++) {
            for (int j = 0; j < iSweights.length; j++) {
                if (iSweights[j] > iSweights[max]) max = j;
            } iSweights[max] =0;
        } return max;
    }

    public int searchSIdx(int j, int th){
        int l = invWeights[j].length;
        int max=0;
        int[] iDweights =  Arrays.copyOf(invWeights[j], l);

        for(int k=0; k<=th; k++) {
            for (int i = 0; i < iDweights.length; i++) {
                if (iDweights[i] > iDweights[max]) max = j;
            } iDweights[max] =0;
        } return max;
    }
}
