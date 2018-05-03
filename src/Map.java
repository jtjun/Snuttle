public class Map{
    private Station[] stations;

    public Map(){
        stations = new Station[Simulator.MAX_STATION];
    }
    
    public Station getStation(int idx){
        if(idx < 0) throw new IndexOutOfBoundsException();
        if(idx >= Simulator.MAX_STATION) throw new IndexOutOfBoundsException();
        return stations[idx];
    }
}