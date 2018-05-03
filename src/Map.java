public class Map{
    private Station[] stations;

    public Map(){
        stations = new Station[Simulator.MAX_STATION];
    }
    
    public Station getStation(int idx){
        return stations[idx];
    }
}