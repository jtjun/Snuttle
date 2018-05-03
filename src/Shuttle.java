public class Shuttle {
    private int x;
    private int y;
    private int time;
    private Schedule S;

    private Station from;
    private Station to;

    Shuttle(int xi, int yi, int timei, Schedule Si){
        x = xi;
        y = yi;
        time = timei;
        Si.removeAfterT(time);
        S = Si;

        from = S.whatIthSched(0).getStation();
        to = S.whatIthSched(1).getStation();
    }

    public int[] getXY(){
        int[] xy = {x,y};
        return xy;
    }

    public Schedule getSchedule(){
        return S;
    }
    /*
    public Edge whereNow(time){

    }*/
}
/*
class Edge{
    private Station from;
    private Station to;

}*/