import java.io.*;
import java.util.*;

public class Map {
    private ArrayList<Station> stations;
    private int[][] dist;

    public Map(String stationfile, String distancefile) throws FileNotFoundException {
        // Load stations from file
        stations = new ArrayList<>();
        Scanner scanner = new Scanner(new File(stationfile));
        int idxx = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parsed = line.split(",");
            if (parsed.length != 3) continue;
            stations.add(new Station(parsed[0], Integer.parseInt(parsed[1]), Integer.parseInt(parsed[2]),idxx));
            idxx++;
        }
        scanner.close();
        int n = stations.size();

        // Load distances from file
        dist = new int[n][n];
        int idx = 0;
        scanner = new Scanner(new File(distancefile));
        while (scanner.hasNextLine()) {
            String[] parsed = scanner.nextLine().split(",");
            if (parsed.length != n) continue;
            for (int j = 0; j < n; j++) {
                dist[idx][j] = Integer.parseInt(parsed[j]);
            }
            idx++;
        } scanner.close();

        // Find shortest distance using Floyd
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k]!=-1 && dist[k][j]!=-1){
                        if(dist[i][j]==-1){
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }else if(dist[i][j] > dist[i][k] + dist[k][j]) {
                            dist[i][j] = dist[i][k] + dist[k][j];
                        }
                    }
                }
            }
        }
    }

    public Station getStation(String name) { // Get station by name
        for (Station station : stations) {
            if (name.equals(station.getName())) return station;
        } throw new NoSuchElementException();
    }

    public Station getStation(int idx) { // Get station by index
        if (idx < 0) throw new IndexOutOfBoundsException();
        if (idx >= stations.size()) throw new IndexOutOfBoundsException();
        return stations.get(idx);
    }

    public int getIndex(String name){ // Get index by name
        for (int i=0; i < stations.size(); i++) {
            if (name.equals(stations.get(i).getName())) return i;
        } throw new NoSuchElementException();
    }

    // if you want time  -> getDistatnce() * speed = time
    public int getDistance(int idx1, int idx2) { // Get distance between two stations by idx
        if (idx1 < 0) throw new IndexOutOfBoundsException();
        if (idx1 >= stations.size()) throw new IndexOutOfBoundsException();
        if (idx2 < 0) throw new IndexOutOfBoundsException();
        if (idx2 >= stations.size()) throw new IndexOutOfBoundsException();
        return dist[idx1][idx2]; // it equals to time
    }

    public int getDistance(String name1, String name2) { // Get distance between two stations by name
        int idx1 = getIndex(name1);
        int idx2 = getIndex(name2);
        return getDistance(idx1, idx2); // it equals to time
    }

    public int getDistance(Station sta1, Station sta2) { // Get distance between two stations by Station
        int idx1 = getIndex(sta1.getName());
        int idx2 = getIndex(sta2.getName());
        return getDistance(idx1, idx2); // it equals to time
    }

    public int getNumStations() {
        return stations.size();
    }
}