package sample.criotam.avinash.indoormap;

import java.util.Arrays;
import java.util.HashMap;

public class TrackerActivity {

    public static int[] nearest_nodes = new int[13];
    public static String[] nearest_rooms = new String[13];
    public static int size = 33;
    public static double[] distance = new double[size];
    public static HashMap<Integer, Double> map = new HashMap();

    public static int[] room1 = {18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29};
    public static int[] room2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    public static int[] hall = {14, 15, 16, 17};
    public static int[] lab = {30, 31, 32, 33};

    public TrackerActivity(double avg_criotam, double avg_espap1, double avg_espap2, double avg_aspap3, double avg_espap4,
                           double avg_espap5, double sd_criotam, double sd_espap1, double sd_espap2, double sd_espap3,
                           double sd_espap4, double sd_espap5){

        double[] avg = new double[6];
        double[] sd = new double[6];

        avg[0] = avg_criotam; avg[1] = avg_espap1; avg[2] = avg_espap2; avg[3] = avg_aspap3; avg[4] = avg_espap4; avg[5] = avg_espap5;
        sd[0] = sd_criotam; sd[1] = sd_espap1; sd[2] = sd_espap2; sd[3] = sd_espap3; sd[4] = sd_espap4; sd[5] = sd_espap5;

        euclideanDistance(avg, sd);
        indexMap(distance);

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

    public void indexMap(double[] dist){

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
