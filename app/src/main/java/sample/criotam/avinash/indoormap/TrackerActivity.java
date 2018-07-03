package sample.criotam.avinash.indoormap;

import java.util.Arrays;
import java.util.HashMap;

public class TrackerActivity {

    public static int size = 33;
    public static double[] distance = new double[size];
    public static HashMap<Integer, Double> map = new HashMap();

    public TrackerActivity(double avg_criotam, double avg_espap1, double avg_espap2, double avg_aspap3, double avg_espap4,
                           double avg_espap5, double sd_criotam, double sd_espap1, double sd_espap2, double sd_espap3,
                           double sd_espap4, double sd_espap5){

        double[] avg = new double[6];
        double[] sd = new double[6];

        avg[0] = avg_criotam; avg[1] = avg_espap1; avg[2] = avg_espap2; avg[3] = avg_aspap3; avg[4] = avg_espap4; avg[5] = avg_espap5;
        sd[0] = sd_criotam; sd[1] = sd_espap1; sd[2] = sd_espap2; sd[3] = sd_espap3; sd[4] = sd_espap4; sd[5] = sd_espap5;

        euclideanDistance(avg, sd);
        sortedMap(distance);

    }

    public void euclideanDistance(double[] avg, double[] sd){

        for(int i =0; i<size; i++){

            distance[i] = 0;

            int sum = 0;

            for(int access_point = 0; access_point<6 ; access_point++){

                // !!!!Modified euclidean distance
                sum += Math.pow((Math.abs(DataSet.average[access_point][i]-avg[access_point])+
                        DataSet.standard_deviation[access_point][i] + sd[access_point]),2);

                //sum += Math.pow((Math.abs(DataSet.average[access_point][i]-avg[access_point])),2);
            }

            distance[i] = Math.sqrt(sum);
        }
    }

    public void sortedMap(double[] dist){

        //Arrays.sort(dist);

        for(int i = 0; i<size; i++){

            for(int j = 0; j<size; j++){
                if(dist[i]==distance[j]){
                    map.put(j, dist[i]);
                }
            }
        }
    }

    public HashMap getMap(){
        return map;
    }

    public double[] getSortedArray(){
        Arrays.sort(distance);
        return distance;
    }
}
