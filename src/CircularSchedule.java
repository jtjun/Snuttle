import java.io.*;

public class CircularSchedule {
    public static void setCircularSchedule(Shuttle[] shuttles, Map map) {
        int n = map.getNumStations();
        int[] stationorder = {0, 2, 3, 4, 20, 6, 7, 8, 9, 10, 22, 19, 18, 17, 21, 16, 15, 13, 12, 11, 14, 5, 1};
        int totald=0;
        for(int i=0; i<stationorder.length-1; i++){
            totald += map.getDistance(stationorder[i], stationorder[i+1]);
        } Simulator.setTotalD(totald);

        for(int i = 0; i < shuttles.length; i++){ // For each shuttle,
            int x = 15;
            int y = 20;
            int t = 0; // acculated time for shuttle

            // Make new schedule
            Schedule schedule = new Schedule();
            int startStation = n/shuttles.length * i; // Start at different positions
            schedule.addSchedule(t,map.getStation(stationorder[startStation]), 0);

            for(int j = startStation+1; /*j < startStation+n*10*/ t<=Simulator.MAX_TIME; j++){ // Visit each node for 10 cycles
                t += map.getDistance(stationorder[(j-1)%n], stationorder[j%n]);
                if(t>Simulator.MAX_TIME) break;
                schedule.addSchedule(t, map.getStation(stationorder[j%n]), 0);
            }
            shuttles[i] = new Shuttle(map.getStation(stationorder[startStation]).getX(),
                                      map.getStation(stationorder[startStation]).getY(),
                                     0, schedule, i, map);
        } // set all Shuttle's circular route
    }
}