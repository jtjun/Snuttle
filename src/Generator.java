import javax.xml.crypto.dsig.SignatureMethod;
import java.util.*;

public class Generator {
    private ArrayList<Guest> guests;
    private static int kr=2;
    public static int[] staOrd = {0, 2, 3, 4, 20, 6, 7, 8, 9, 10, 22, 19, 18, 17, 21, 16, 15, 13, 12, 11, 14, 5, 1};
    public int hours = Simulator.MAX_TIME/60;

    public Generator(int n, Map map, String type) {
        if(type.equals("HS")) GeneratorHS(n, map);
        if(type.equals("AR")) GeneratorAR(n, map);
        if(type.equals("LR")) GeneratorLR(n, map);
        if(type.equals("GG")) GeneratorGG(n, map);
        if(type.equals("PG")) GeneratorPG(n, map);
        if(type.equals("CM")) GeneratorCM(n, map);
        if(type.equals("EX")) GeneratorEX(n, map, 2);
    }

    public void GeneratorAR(int n, Map map) { // All Random
        Random generator = new Random();
        guests = new ArrayList<>();
        ArrayList<StationGroup> edgelist = new ArrayList<>();

        for(int i = 0; i < map.getNumStations(); i++){
            Station s = map.getStation(i);
            for(int j = 0; j < map.getNumStations(); j++){
                if(i!=j){
                    edgelist.add(new StationGroup(s,map.getStation(j)));
                }
            }
        }
        for (int i = 0; i < n; i++) {
            // Set stations with random excluding same station
            StationGroup sd = edgelist.get(generator.nextInt(edgelist.size()));

            Station placeS = sd.start;
            Station placeD = sd.dest;

            // Set time with random
            int timeD = generator.nextInt(100)+50;
            int timeS = generator.nextInt(Simulator.MAX_TIME-map.getDistance(sd.start.getName(), sd.dest.getName())-timeD);
            guests.add(new Guest(timeS, placeS, timeS+timeD+map.getDistance(sd.start.getName(), sd.dest.getName()), placeD, generator.nextInt(timeS+1)));

            // int timeS = generator.nextInt(Simulator.MAX_TIME);
            // int timeD = generator.nextInt(Simulator.MAX_TIME);
            // if (timeS > timeD) {
            //     int tmp = timeS;
            //     timeS = timeD;
            //     timeD = tmp;
            // }

            // guests.add(new Guest(timeS, placeS, timeD, placeD));
        }
        sortGuests();
    }

     public void GeneratorHS(int n, Map map) { // Hot Spot
        Random generator = new Random();
        int k = Simulator.MAX_STATION*Simulator.MAX_STATION/2/8; // It can be set with other value
        // Make array for random sampling
        ArrayList<StationGroup> edgelist = new ArrayList<>();
        guests = new ArrayList<>();

        for(int i = 0; i < map.getNumStations(); i++) {
            Station s = map.getStation(i);
            for (int j = 0; j < map.getNumStations(); j++) {
                if (j != i) edgelist.add(new StationGroup(s, map.getStation(j)));
            }
        } Collections.shuffle(edgelist);
        
        // for(int i = 0; i < n; i++){
        //     // Set time with random
        //     StationGroup sd = edgelist.get(getRandomStation(Simulator.K_RATIO, k, edgelist.size()));

        //     Station placeS = sd.start;
        //     Station placeD = sd.dest;

        //     // Set time with random
        //     int timeD = generator.nextInt(100)+50;
        //     int timeS = generator.nextInt(Simulator.MAX_TIME-map.getDistance(sd.start.getName(), sd.dest.getName())-timeD);

        //     guests.add(new Guest(timeS, placeS, timeD+timeS+map.getDistance(sd.start.getName(), sd.dest.getName()), placeD,generator.nextInt(timeS+1)));
        // }

        for(int i = 0; i < n*17/20; i++){
            // Set time with random
            StationGroup sd = edgelist.get(getRandomStation(Simulator.K_RATIO, k, edgelist.size()));

            Station placeS = sd.start;
            Station placeD = sd.dest;

            // Set time with random
            int timeD = generator.nextInt(100)+50;
            int timeS = generator.nextInt(Simulator.MAX_TIME-map.getDistance(sd.start.getName(), sd.dest.getName())-timeD);

            guests.add(new Guest(timeS, placeS, timeD+timeS+map.getDistance(sd.start.getName(), sd.dest.getName()), placeD,/*Math.max(generator.nextInt(200)+timeS-200,0))*/0));
        }
        StationGroup sd = edgelist.get(getRandomStation(1, k, edgelist.size()));
        int timeDD = generator.nextInt(100)+50;
        int timeSS = Math.max(generator.nextInt(Simulator.MAX_TIME-map.getDistance(sd.start.getName(), sd.dest.getName())-timeDD),100);

        for(int i=n*17/20; i < n; i++){
            Station placeS = sd.start;
            Station placeD = sd.dest;

            guests.add(new Guest(timeSS, placeS, timeDD+timeSS+map.getDistance(sd.start.getName(), sd.dest.getName()), placeD,/*timeSS-200*/0));
        }
        sortGuests();
    }

    public void GeneratorLR(int n,Map map){ // Like Real
        Random rand = new Random();
        int m = map.getNumStations();
        guests = new ArrayList<Guest>();

        ArrayList<Station> lecture_building = new ArrayList<>();
        int lecture_num = 5;
        for(int i = 0; i < m; i++){
            lecture_building.add(map.getStation(i));
        }
        Collections.shuffle(lecture_building);

        while(lecture_building.size()>lecture_num) lecture_building.remove(lecture_building.size()-1);
        
        for(int i = 0 ; i < n*8/10; i++){
            int timeS = (rand.nextInt(hours*19/24-hours*1/3)+hours*1/3)*60;
            int s = rand.nextInt(lecture_num);
            int d = rand.nextInt(lecture_num);
            while( s == d ) d = rand.nextInt(lecture_num);

            int requestT = 0;//Math.max(timeS-rand.nextInt(1),0);
            guests.add(new Guest(timeS, lecture_building.get(s),
                    timeS + map.getDistance(lecture_building.get(s), lecture_building.get(d)) + Simulator.totalD/kr ,
                    lecture_building.get(d) , requestT));
        }

        for(int i = n*8/10; i < n*9/10; i++){
            int timeS = rand.nextInt(hours*11/12*60-hours*3/8*60)+hours*3/8*60;
            int s = rand.nextInt(m);
            int d = rand.nextInt(m);
            while( s == d ) d = rand.nextInt(m);

            int requestT = 0;//Math.max(timeS-rand.nextInt(1),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));
        }

        for(int i = n*9/10; i < n; i++){
            int timeS = rand.nextInt(hours*11/12*60);
            int s = rand.nextInt(m);
            int d = rand.nextInt(m);
            while( s == d ) d = rand.nextInt(m);

            int requestT = 0;//Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));
        }
    }

    public void GeneratorGG(int n, Map map){ // Greedy Good
        int[] stationorder = {0, 2, 3, 4, 20, 6, 7, 8, 9, 10, 22, 19, 18, 17, 21, 16, 15, 13, 12, 11, 14, 5, 1};
        Random rand = new Random();
        int m = map.getNumStations();
        guests = new ArrayList<Guest>();

        ArrayList<Integer> lecture_building = new ArrayList<>();
        int lecture_num = 12;
        for(int i = 0; i < m; i++){
            lecture_building.add(i);
        }
        Collections.shuffle(lecture_building);

        while(lecture_building.size()>lecture_num) lecture_building.remove(lecture_building.size()-1);
        
        for(int i = 0 ; i < n*8/10; i++){
            int timeS = ((rand.nextInt(hours*10/12-hours*3/8)+hours*3/8)*60);
            int s = rand.nextInt(lecture_num);
            int d = rand.nextInt(lecture_num);
            while( s == d ) d = rand.nextInt(lecture_num);
            
            int k=0,ss=-1,dd=-1;
            for(k=0;k<stationorder.length;k++){
                if(stationorder[k]==s){
                    ss=k;
                    break;
                }
            }
            for(k=0;k<stationorder.length;k++){
                if(stationorder[k]==d){
                    dd=k;
                    break;
                }
            }
            if(ss<dd){
                if(dd-ss<2){
                    int tmp = ss;
                    ss = dd;
                    dd = tmp;
                }else if(dd-ss<22){
                    i--;
                    continue;
                }
            }else{
                if(ss-dd>21){
                    int tmp = ss;
                    ss = dd;
                    dd = tmp;
                }else if(ss-dd>1){
                    i--;
                    continue;
                }
            }

            int requestT = Math.max(timeS-rand.nextInt(1),0);
            guests.add(new Guest(timeS, map.getStation(lecture_building.get(s)),
                    timeS + map.getDistance(lecture_building.get(s), lecture_building.get(d)) + Simulator.totalD/kr
                    , map.getStation(lecture_building.get(d)), requestT));
        }

        for(int i = n*8/10; i < n*9/10; i++){
            int timeS = rand.nextInt(hours*23/24*60-hours*3/8*60)+hours*3/8*60;
            int s = rand.nextInt(m);
            int d = rand.nextInt(m);
            while( s == d ) d = rand.nextInt(m);

            int k=0,ss=-1,dd=-1;
            for(k=0;k<stationorder.length;k++){
                if(stationorder[k]==s){
                    ss=k;
                    break;
                }
            }
            for(k=0;k<stationorder.length;k++){
                if(stationorder[k]==d){
                    dd=k;
                    break;
                }
            }
            if(ss<dd){
                if(dd-ss<2){
                    int tmp = ss;
                    ss = dd;
                    dd = tmp;
                }else if(dd-ss<22){
                    i--;
                    continue;
                }
            }else{
                if(ss-dd>21){
                    int tmp = ss;
                    ss = dd;
                    dd = tmp;
                }else if(ss-dd>1){
                    i--;
                    continue;
                }
            }
            int requestT = Math.max(timeS-rand.nextInt(1),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));
        }

        for(int i = n*9/10; i < n; i++){
            int timeS = rand.nextInt(hours*23/24*60);
            int s = rand.nextInt(m);
            int d = rand.nextInt(m);
            while( s == d ) d = rand.nextInt(m);

            int k=0,ss=-1,dd=-1;
            for(k=0;k<stationorder.length;k++){
                if(stationorder[k]==s){
                    ss=k;
                    break;
                }
            }
            for(k=0;k<stationorder.length;k++){
                if(stationorder[k]==d){
                    dd=k;
                    break;
                }
            }
            if(ss<dd){
                if(dd-ss<2){
                    int tmp = ss;
                    ss = dd;
                    dd = tmp;
                }else if(dd-ss<22){
                    i--;
                    continue;
                }
            }else{
                if(ss-dd>21){
                    int tmp = ss;
                    ss = dd;
                    dd = tmp;
                }else if(ss-dd>1){
                    i--;
                    continue;
                }
            }
            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));
        }
    }

    public void GeneratorPG(int n,Map map){ // Power Greedy
        Random rand = new Random();
        int m = map.getNumStations();
        guests = new ArrayList<Guest>();
        int staN = Simulator.staN;

        for(int i=0; i<n/3; i++){
            int timeS = rand.nextInt(Simulator.MAX_TIME*(hours-1)/hours);
            int sp = rand.nextInt(staN);
            int dp = (sp+4+rand.nextInt(staN-4))%staN;
            while(sp==dp) dp = (sp+4+rand.nextInt(19))%staN;


            int s = staOrd[sp];
            int d = staOrd[dp];

            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));
        }
        for(int i=n/3; i< n; i++){ // explosion

            int timeS = (rand.nextInt(hours*10/12-hours*3/8)+hours*3/8)*60;
            String[][] farS = {{"C","B"},{"C","N"},{"G","N"},{"H","P"}};
            int ord = rand.nextInt(farS.length);
            String[] SD = farS[ord];

            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(SD[0]),
                    timeS + map.getDistance(map.getStation(SD[0]), map.getStation(SD[1])) + Simulator.totalD/kr
                    , map.getStation(SD[1]), requestT));
        }
    }

    public void GeneratorCM(int n,Map map){ //Cammel
        Random rand = new Random();
        guests = new ArrayList<Guest>();
        int staN = Simulator.staN;

        for(int i=0; i< n/3; i++){
            double randGaussian =  rand.nextGaussian();
            int timeS = ((int) (360 + randGaussian));
            timeS = cutT(timeS);
            int sp = rand.nextInt(staN);
            int dp = (sp+4+rand.nextInt(staN-4))%staN;
            while(sp==dp) dp = (sp+4+rand.nextInt(staN))%staN;

            int s = staOrd[sp];
            int d = staOrd[dp];

            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));
        }
        for(int i=n/3; i< 2*n/3; i++){
            double randGaussian =  rand.nextGaussian();
            int timeS = ((int) (1080 + randGaussian));
            timeS = cutT(timeS);
            int sp = rand.nextInt(staN);
            int dp = (sp+4+rand.nextInt(staN-4))%staN;
            while(sp==dp) dp = (sp+4+rand.nextInt(staN-4))%staN;

            int s = staOrd[sp];
            int d = staOrd[dp];

            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));

        }
        for(int i=2*n/3; i< n; i++){
            double par = rand.nextDouble();
            int timeS = 0;
            if(par>=0/5) timeS = 1080;
            else timeS =360;
            String[][] farS = {{"C","B"},{"C","N"},{"G","N"},{"H","P"}};
            int ord = rand.nextInt(farS.length);
            String[] SD = farS[ord];

            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(SD[0]),
                    timeS + map.getDistance(map.getStation(SD[0]), map.getStation(SD[1])) + Simulator.totalD/kr
                    , map.getStation(SD[1]), requestT));
        }
    }

    public void GeneratorEX(int n,Map map, int rk){ //Explosion
        System.out.println("IT HAS SOME PROBLEM!!! : EX");
        Random rand = new Random();
        guests = new ArrayList<Guest>();
        int staN = Simulator.staN;


        for(int i=0; i< n/rk; i++){
            //double randGaussian =  rand.nextGaussian();
            int timeS = rand.nextInt(Simulator.MAX_TIME);
            timeS = cutT(timeS);
            int sp = rand.nextInt(staN);
            int dp = (sp+4+rand.nextInt(staN-4))%staN;
            while(sp==dp) dp = (sp+4+rand.nextInt(staN-4))%staN;

            int s = staOrd[sp];
            int d = staOrd[dp];

            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(s),
                    timeS + map.getDistance(map.getStation(s), map.getStation(d)) + Simulator.totalD/kr
                    , map.getStation(d), requestT));
        } // HOT SPOT
        for(int i=n/rk; i< n; i++){
            double gaus = rand.nextGaussian();
            int timeS = (Simulator.MAX_TIME/2)+ ((int)gaus*100)%10;
            String[][] farS = {{"C","B"},{"C","N"},{"G","N"},{"H","P"}};
            int ord = rand.nextInt(farS.length);
            String[] SD = farS[ord];

            int requestT = Math.max(timeS-rand.nextInt(30),0);
            guests.add(new Guest(timeS, map.getStation(SD[0]),
                    timeS + map.getDistance(map.getStation(SD[0]), map.getStation(SD[1])) + Simulator.totalD/kr
                    , map.getStation(SD[1]), requestT));
        }
    }

    public int cutT(int t){
        if(t<0) return 0;
        if(t>Simulator.MAX_TIME-1) return Simulator.MAX_TIME-1;
        return t;
    }

    public ArrayList<Guest> getGuests(){ return guests; }
    public ArrayList<Guest> copyGuests(){
        ArrayList<Guest> guestCopy = new ArrayList<>();
        for(Guest guest : guests) {
            guestCopy.add(new Guest(guest.getTimeS(), guest.getPlaceS(), guest.getTimeD(), guest.getPlcaeD(), guest.getRequestT()));
        } return guestCopy;
    }

    private int getRandomStation(double r,int k,int m){
        // Produce random number from 0 to m-1 regarding ratio r(higher k makes more pick front items)
        Random generator = new Random();
        if(generator.nextDouble()<r){
            return generator.nextInt(k);
        }else{
            return generator.nextInt(m);
        }
    }

    public void sortGuests(){
        guests.sort( new Comparator<Guest>() {
            @Override
            public int compare(Guest o1, Guest o2) {
                final int t1 = o1.getTimeS();
                final int t2 = o2.getTimeS();
                return Integer.compare(t1, t2);
            }
        }); // sort guests by compare timeS
    }

    class StationGroup{
        public Station start,dest;
        public StationGroup(Station start, Station dest){
            this.start = start;
            this.dest = dest;
        }
    }
}
