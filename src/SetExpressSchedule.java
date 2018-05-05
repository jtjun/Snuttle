import java.util.*;

public class SetExpressSchedule {
    public static void setExpressSchedule(Shuttle[] shuttles, Map map,int r){
        int n = map.getNumStations();
        int[] stationorder = {0, 2, 3, 4, 20, 6, 7, 8, 9, 10, 22, 19, 18, 17, 21, 16, 15, 13, 12, 11, 14, 5, 1};

        for(int i = 0; i < r; i++){ // For each shuttle,
            int x = 15;
            int y = 20;
            int t = 0; // acculated time for shuttle

            // Make new schedule
            Schedule schedule = new Schedule();
            int startStation = n/shuttles.length * i; // Start at different positions
            schedule.addSchedule(t,map.getStation(stationorder[startStation]), 0);

            for(int j = startStation+1; j < startStation+n*10; j++){ // Visit each node for 10 cycles
                t += map.getDistance(stationorder[(j-1)%n], stationorder[j%n]);
                schedule.addSchedule(t, map.getStation(stationorder[j%n]), 0);
            }
            shuttles[i] = new Shuttle(map.getStation(stationorder[startStation]).getX(),
                                      map.getStation(stationorder[startStation]).getY(),
                                     0, schedule, i, map);
        } // set all Shuttle's circular route

        ArrayList<ArrayList<Integer>> al = new ArrayList<ArrayList<Integer>>();

        for(int i = 0; i < 3; i++) al.add(new ArrayList<Integer>());

        int k = 0;

        for(int i = 0; i < n; i++){
            al.get(k).add(i);
            k = (k+1)/3;
        }

        for(int i = r; i < shuttles.length; i++){
            int x = 15;
            int y = 20;
            int t = 0; // acculated time for shuttle

            // Make new schedule
            Schedule schedule = new Schedule();
            schedule.addSchedule(t,map.getStation(stationorder[al.get(i%3).get(0)]), 0);
            for(int j = 1; j < n*20; j++){
                t += map.getDistance(stationorder[al.get(i%3).get((j-1)%n)],stationorder[al.get(i%3).get(j%n)]);
                schedule.addSchedule(t, map.getStation(stationorder[al.get(i%3).get(j%n)]),0);
            }
            shuttles[i] = new Shuttle(map.getStation(stationorder[al.get(i%3).get(0)]).getX(),
                                      map.getStation(stationorder[al.get(i%3).get(0)]).getY(),
                                     0, schedule, i, map);
        }
    }
}