public class Shuttle {
    private double x;
    private double y;
    private Schedule S;

    public Station getFrom(double time){
    }

    public Station getTo(double time){

    }

    public void getSchedule(Schedule schedule, double time){
        schedule.removeAfterT(time);
        S = schedule;
    }
}
