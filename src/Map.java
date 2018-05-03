public class Map{
    private Station[] stations;

    public Station getStation(String name){
        for(int i=0; i<stations.length; i++ ){
            if(name.equals(stations[i].getName())) return stations[i];
        } return error; // error
    }
    
}