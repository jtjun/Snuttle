<<<<<<< HEAD
public class Map {
=======
import java.util.NoSuchElementException;

public class Map{
>>>>>>> b12d98effb0c2f0b652ededb60dfc65f1221a5f3
    private Station[] stations;

    public Map(){
        stations = new Station[Simulator.MAX_STATION];
    }
    
    public Station getStation(String name){ // Get station by name
        for(int i=0; i<stations.length; i++ ){
            if(name.equals(stations[i].getName())) return stations[i];
        }
        throw new NoSuchElementException();
    }

    public Station getStation(int idx){ // Get station by index
        if(idx < 0) throw new IndexOutOfBoundsException();
        if(idx >= Simulator.MAX_STATION) throw new IndexOutOfBoundsException();
        return stations[idx];
    }
}