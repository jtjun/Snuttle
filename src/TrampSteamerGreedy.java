import java.util.*;

public class TrampSteamerGreedy{
    public static ArrayList<Integer> cycle;
    public static void setIGreedyEach(Shuttle[] shuttles, int shuttlenum, int time){
        int n = Simulator.guests.size();
        int[][] requests = new int[n][n];
        int mx = 0;
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                requests[i][j] = 0;
            }
        }
        for(Guest guest : Simulator.guests){
            if(guest.getRequestT()>time || guest.getTimeD()<time) continue;
            requests[guest.getPlaceS().getIdx()][guest.getPlcaeD().getIdx()]++;
            if(mx < requests[guest.getPlaceS().getIdx()][guest.getPlcaeD().getIdx()]){
                mx = requests[guest.getPlaceS().getIdx()][guest.getPlcaeD().getIdx()];
            }
        }
        int l = -mx, r = mx;
        while(l<r){
            int u = (l+r) / 2;
            int[][] d = new int[n][n];
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    d[i][j] = -requests[i][j]-u*Simulator.map.getDistance(i, j);
                }
            }
            if(hasNegCycle(d)){
                r = u;
            }else{
                if(hasZeroCycle(d)){
                    break;
                }
                l = u+1;
            }
        }
        int t = time;
        int i = 0;
        Schedule schedule = new Schedule();
        
        while(t<=Simulator.MAX_TIME){
            schedule.addSchedule(t, Simulator.map.getStation(cycle.get(i%cycle.size())), 0);
            t += Simulator.map.getDistance(cycle.get(i%cycle.size()),cycle.get((i+1)%cycle.size()));
            i++;
        }
        shuttles[shuttlenum] = new Shuttle(Simulator.map.getStation(cycle.get(0)).getX(), Simulator.map.getStation(cycle.get(0)).getY(), time, schedule, shuttlenum, Simulator.map);
    }

    public static boolean hasNegCycle(int[][] d){
        int n = d.length;
        int[] p = new int[n];
        int[] nx = new int[n];
        int[] pp = new int[n];
        for(int i = 0; i < n; i++){
            p[i] = 0;
            pp[i] = 0;
        }
        for(int i = 0; i < n-1; i++){
            for(int j = 0; j < n; j++){
                nx[j] = p[j];
                for(int k = 0; k < n; k++){
                    if(nx[j] > p[k] + d[k][j]){
                        nx[j] = p[k] + d[k][j];
                        pp[j] = k;
                    }
                }
            }
            for(int j = 0; j < n; j++){
                p[j] = nx[j];
            }
        }
        boolean l = false;
        int v = 0;
        for(int j = 0; j < n; j++){
            for(int k = 0; k < n; k++){
                if(nx[j] > p[k] + d[k][j]){
                    l = true;
                    v = j;
                }
            }
        }
        if(!l) return false;
        int[] stk = new int[n+1];
        int tp = 0;
        boolean[] ck = new boolean[n];
        for(int i = 0; i < n; i++) ck[i] = false;
        ck[v] = true;
        stk[tp++] = v;
        while(!ck[pp[v]]){
            ck[pp[v]] = true;
            v = pp[v];
            stk[tp++] = v;
        }
        int k = pp[v];
        cycle.add(k);
        while(stk[--tp]!=k){
            cycle.add(stk[tp]);
        }
        return true;
    }

    public static boolean hasZeroCycle(int[][] d){
        int n = d.length;
        int[] p = new int[n];
        int[] nx = new int[n];
        for(int i = 0; i < n; i++) p[i] = 0;
        for(int i = 0; i < n-1; i++){
            for(int j = 0; j < n; j++){
                nx[j] = p[j];
                for(int k = 0; k < n; k++){
                    if(nx[j] > p[k] + d[k][j]){
                        nx[j] = p[k] + d[k][j];
                    }
                }
            }
        }
        for(int j = 0; j < n; j++){
            nx[j] = p[j];
            for(int k = 0; k < n; k++){
                if(nx[j] > p[k] + d[k][j]){
                    return false;
                }
            }
        }
        int[][] r = new int[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                r[i][j] = 0;
                if(i != j && nx[j] == nx[i] + d[i][j]) r[i][j] = -1;
            }
        }
        return hasNegCycle(r);
    }
}