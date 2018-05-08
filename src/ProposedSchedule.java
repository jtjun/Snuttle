import java.util.*;

public class ProposedSchedule{
    public static void setProposedSchedule(Shuttle[] shuttles, Map map, ArrayList<Guest> guests, int fixedshuttle){
        int n = map.getNumStations();

        ArrayList<StationGroupNum> sgns = new ArrayList<>();

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i!=j){
                    sgns.add(new StationGroupNum(new StationGroup(map.getStation(i),map.getStation(j)),0));
                }
            }
        }

        for(Guest guest : guests){
            StationGroup sg = new StationGroup(guest.getPlaceS(),guest.getPlcaeD());
            for(StationGroupNum sgn : sgns){
                if(sgn.sg.equals(sg)){
                    sgn.num++;
                }
            }
        }

        sgns.sort(new Comparator<StationGroupNum>(){
            @Override
            public int compare(StationGroupNum s1, StationGroupNum s2){
                return -Integer.compare(s1.num,s2.num);
            }
        });

        int[] stationorder = {0, 2, 3, 4, 20, 6, 7, 8, 9, 10, 22, 19, 18, 17, 21, 16, 15, 13, 12, 11, 14, 5, 1};

        for(int i = 0; i < fixedshuttle; i++){ // For each shuttle,
            int x = 15;
            int y = 20;
            int t = 0; // acculated time for shuttle

            // Make new schedule
            Schedule schedule = new Schedule();
            int startStation = n/fixedshuttle * i; // Start at different positions
            schedule.addSchedule(t,map.getStation(stationorder[startStation]), 0);
            if(Simulator.Wait) {
                t+=1;
                schedule.addSchedule(t,map.getStation(stationorder[startStation]), 0);
            }

            for(int j = startStation+1; /*j < startStation+n*10*/ t<=Simulator.MAX_TIME; j++){ // Visit each node for 10 cycles
                t += map.getDistance(stationorder[(j-1)%n], stationorder[j%n]);
                if(t>Simulator.MAX_TIME) break;
                schedule.addSchedule(t, map.getStation(stationorder[j%n]), 0);
                if(Simulator.Wait) {
                    t+=1;
                    schedule.addSchedule(t, map.getStation(stationorder[j%n]), 0);
                }
            }
            shuttles[i] = new Shuttle(map.getStation(stationorder[startStation]).getX(),
                                      map.getStation(stationorder[startStation]).getY(),
                                     0, schedule, i, map);
        } // set all Shuttle's circular route

        int[] ts = new int[shuttles.length-fixedshuttle];
        Schedule[] schedules = new Schedule[shuttles.length-fixedshuttle];

        for(int i = 0; i < ts.length; i++){
            ts[i] = 0;
            schedules[i] = new Schedule();
        }
        int k = 0;
        ArrayList<ArrayList<Station>> lists = new ArrayList<ArrayList<Station>>(shuttles.length-fixedshuttle);
        for(int i = 0; i < shuttles.length-fixedshuttle; i++){
            lists.add(new ArrayList<Station>());
        }
        if( shuttles.length-fixedshuttle >0) {
            for (StationGroupNum sgn : sgns) {
                if (lists.get(k).size() > map.getNumStations() / 8) break;
                lists.get(k).add(sgn.sg.start);
                lists.get(k).add(sgn.sg.dest);
                k = (k + 1) % (shuttles.length - fixedshuttle);
            }
        }
        k = 0;
        for(ArrayList<Station> list : lists){
            int t = 0;
            Station p = list.get(0);
            schedules[k].addSchedule(t, p, 0);
            if(Simulator.Wait) {
                t+=1;
                schedules[k].addSchedule(t, p, 0);
            }
            for(int i = 1;/* i < n*10+1*/ t<=Simulator.MAX_TIME; i++){
                t += map.getDistance(list.get((i-1)%list.size()).getName(), list.get(i%list.size()).getName());
                if(t>Simulator.MAX_TIME) break;
                schedules[k].addSchedule(t, list.get(i%list.size()), 0);
                if(Simulator.Wait) {
                    t+=1;
                    schedules[k].addSchedule(t, list.get(i%list.size()), 0);
                }
                p = list.get(i%list.size());
            }
            shuttles[k+fixedshuttle] = new Shuttle(list.get(0).getX(), list.get(0).getY(), 0, schedules[k], k+fixedshuttle, map);
            k++;
        }
    }

}