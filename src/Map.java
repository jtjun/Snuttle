public class Map{
    private Station[] stations;


    public Station getStation(String name){
        for(int i=0; i<stations.length; i++ ){
            if(name.equals(stations[i].getName())) return stations[i];
        } return error; // error

    public Map(){
        stations = new Station[Simulator.MAX_STATION];
    }
    
    public Station getStation(int idx){
        if(idx < 0) throw new IndexOutOfBoundsException();
        if(idx >= Simulator.MAX_STATION) throw new IndexOutOfBoundsException();
        return stations[idx];
    }
}