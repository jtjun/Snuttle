public class Guest {
    private int timeS, timeD;
    private Station placeS, placeD;
    
    public Guest(int timeS, Station placeS, int timeD, Station placeD){
        this.timeS = timeS;
        this.placeS = placeS;
        this.timeD = timeD;
        this.placeD = placeD;
    }
}