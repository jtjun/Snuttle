import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Map{
    private ArrayList<Station> stations;

    public Map(String filename) throws FileNotFoundException{ // Load stations from file
        stations = new ArrayList<>();
        Scanner scanner = new Scanner(new File(filename));
        while(scanner.hasNextLine()){
            String[] parsed = scanner.nextLine().split(",");
            if(parsed.length!=3) continue;
            stations.add(new Station(parsed[0],Double.parseDouble(parsed[1]),Double.parseDouble(parsed[2])));
        }
        scanner.close();
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