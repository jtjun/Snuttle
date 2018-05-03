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

        from = S.whatIthStation(0);
        to = S.whatIthStation(1);
    }

    public Edge whereNow(time){

    }

    public int[] getXY(){
        int[] xy = {x,y};
        return xy;
    }
}

class Edge{
    private Station from;
    private Station to;

}
