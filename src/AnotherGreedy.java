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
            IGredShuts[i] = setAGreedyEach(i, greedyshuttle, map);
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

    public Shuttle setAGreedyEach(int rank, int l, Map map) {
        Schedule schedule = new Schedule();
        int t = 0;
        int th = 0;
        int idxR = indxReader[rank%l];
        int staPrev = idxR/Simulator.staN;

        while(t < Simulator.MAX_TIME) {
            int[] visited = {};
            int idxr = indxReader[rank%l+th%5];
            int staSidx = idxr/Simulator.staN;
            int staDidx = idxr%Simulator.staN;
            t += map.getDistance(staPrev, staSidx);
            visited= arrayAdd(visited, staSidx);
            visited= arrayAdd(visited, staDidx);

            schedule.addSchedule(t, map.getStation(staSidx), 0);
            t += map.getDistance(staSidx, staDidx);
            schedule.addSchedule(t, map.getStation(staDidx), 0);

            while (t < Simulator.MAX_TIME) {
                staSidx = searchDIdx(staDidx, th);
                t += map.getDistance(staDidx, staSidx);
                schedule.addSchedule(t, map.getStation(staSidx), 0);

                if (isIn(visited, staSidx)) {
                    staPrev = staSidx;
                    th++;
                    break; // WE HAVE TO ADD "T" FROM NOW TO NEXT
                } // if one roof is complete
                visited= arrayAdd(visited, staSidx);

                staDidx = searchDIdx(staSidx, th);
                t += map.getDistance(staSidx, staDidx);
                schedule.addSchedule(t, map.getStation(staDidx), 0);

                if (isIn(visited, staDidx)) {
                    staPrev = staDidx;
                    th++;
                    break; // WE HAVE TO ADD "T" FROM NOW TO NEXT
                } // if one roof is complete
                visited= arrayAdd(visited, staDidx);
            }
        }
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

    public boolean isIn(int[] ar, int n){
        int l = ar.length;
        for(int i=0; i<l; i++){
            if(ar[i] == n) return true;
        } return false;
    }
    public int[] arrayAdd(int[] ar, int n){
        int l= ar.length;
        ar = Arrays.copyOf(ar, l+1);
        ar[l] = n;
        return ar;
    }
}
