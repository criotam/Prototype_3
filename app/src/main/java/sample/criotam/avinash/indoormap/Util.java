package sample.criotam.avinash.indoormap;
import java.util.ArrayList;

public class Util {

    public static double MAX_MAP_HEIGHT;

    public static double MAX_MAP_WIDTH;

    public static double x_source_coordinate = 0.0;

    public static double y_source_coordinate = 0.0;

    public static double x_current_coordinate = 0.0;

    public static double y_current_coordinate = 0.0;

    public static double x_current_screen = 0.0;

    public static double y_current_screen = 0.0;

    public static double x_source_screen = 0.0;

    public static double y_source_screen = 0.0;

    public static int source_node = 0;

    public static float rotation_angle = 0;

    public static int source_key_node;

    public static double x_destination_coordinate = 0.0;

    public static double y_destination_coordinate = 0.0;

    public static double x_destination_screen = 0.0;

    public static double y_destination_screen = 0.0;

    public static int destination_node = 1;

    public static String prev_destination_path = "";

    public static String destination_path = "";

    public static int destination_key_node;

    public static int[] path_nodes = new int[33];

    public static ArrayList<String> path_command = new ArrayList<>();
}
