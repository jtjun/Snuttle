import java.io.*;
import java.util.*;

public class Map{
    private ArrayList<Station> stations;
    private int[][] dist;

    public Map(String stationfile, String distancefile) throws FileNotFoundException{
        // Load stations from file
        stations = new ArrayList<>();
        Scanner scanner = new Scanner(new File(stationfile));
        while(scanner.hasNextLine()){
            String[] parsed = scanner.nextLine().split(",");
            if(parsed.length!=3) continue;
            stations.add(new Station(parsed[0],Integer.parseInt(parsed[1]),Integer.parseInt(parsed[2])));
        }
        scanner.close();
        int n = stations.size();

        // Load distances from file
        dist = new int[n][n];
        int idx = 0;
        scanner = new Scanner(new File(distancefile));
        while(scanner.hasNextLine()){
            String[] parsed = scanner.nextLine().split(",");
            if(parsed.length!=n) continue;
            for(int j = 0; j < n; j++){
                dist[idx][j] = Integer.parseInt(parsed[j]);
            }
            idx++;
        }
        scanner.close();

        // Find shortest distance using Floyd
        for(int k = 0; k < n; k++){
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(dist[i][j] > dist[i][k] + dist[k][j]){
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }
    }
    
    public Station getStation(String name){ // Get station by name
        for(Station station : stations){
            if(name.equals(station.getName())) return station;
        }
        throw new NoSuchElementException();
    }

    public Station getStation(int idx){ // Get station by index
        if(idx < 0) throw new IndexOutOfBoundsException();
        if(idx >= stations.size()) throw new IndexOutOfBoundsException();
        return stations.get(idx);
    }
}

class Edge {
    private Station from;
    private Station to;
    int remain;
    int
}