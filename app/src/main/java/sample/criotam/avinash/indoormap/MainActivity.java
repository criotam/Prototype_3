package sample.criotam.avinash.indoormap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.TextView;

//import com.devs.vectorchildfinder.VectorChildFinder;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class MainActivity extends AppCompatActivity {

    WifiManager wifi;
    WifiScanReceiver wifiReceiver;
    private TextView resultset;

    private long split_time = 0;

    private long start_time = 0;

    private boolean flag = true;

    private boolean stop_flag = true;

    private ArrayList<Integer> avg_val1, avg_val2, avg_val3, avg_val4, avg_val5, avg_val6;

    private double avg1 = 0, avg2 = 0, avg3 = 0, avg4 = 0, avg5 = 0, avg6 = 0;

    private double sd1 = 0, sd2 = 0, sd3 = 0, sd4 = 0, sd5 = 0, sd6 = 0;

    private Integer[] index = new Integer[33];


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    resultset.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    resultset.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    resultset.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        setContentView(R.layout.activity_main);
        resultset = (TextView) findViewById(R.id.resultset);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        wifi=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiScanReceiver();

        avg_val1 = new ArrayList<>();
        avg_val2 = new ArrayList<>();
        avg_val3 = new ArrayList<>();
        avg_val4 = new ArrayList<>();
        avg_val5 = new ArrayList<>();
        avg_val6 = new ArrayList<>();

        Timer t = new Timer();

        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {

                                      wifi.startScan();
                                  }

                              },
                0,
                100);

        initialize();
        findPosition();
*/

        setContentView(new CustomView(this));
    }

    protected void onPause() {
        //unregisterReceiver(wifiReceiver);
        super.onPause();
    }

    protected void onResume() {
        /*
        registerReceiver(
                wifiReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );
        */
        super.onResume();
    }


    public double getAverage(ArrayList<Integer> avg_val){

        int sum = 0;

        for(int val: avg_val){
            sum+= val;
        }

        float val = (float)sum/avg_val.size();

        Log.d("avg_value", val+"");

        return val;


    }

    public double getStandardDeviation(ArrayList<Integer> avg_val, double val){

        double standardDeviation = 0;

        for(int value: avg_val) {
            standardDeviation += Math.pow(value - val, 2);
        }

        standardDeviation =  Math.sqrt(standardDeviation/avg_val.size());

        return standardDeviation;
    }


    private class WifiScanReceiver extends BroadcastReceiver {
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();

            for(int i = 0; i < wifiScanList.size(); i++){
                String info = ((wifiScanList.get(i)).toString());

                split_time = System.currentTimeMillis() / 1000;

                if (flag) {
                    start_time = split_time;
                    flag = false;
                }

                if(stop_flag) {

                    if ((wifiScanList.get(i).SSID).trim().toString().equalsIgnoreCase("criotam")) {
                        Log.d("criotam", "found");

                        avg_val1.add(wifiScanList.get(i).level);

                        avg1 = getAverage(avg_val1);

                        sd1 = getStandardDeviation(avg_val1, avg1);

                        Log.d("average", avg1 + "");

                    }

                    if ((wifiScanList.get(i).SSID).trim().toString().equalsIgnoreCase("ESPap1")) {
                        Log.d("Espap1", "found");

                        avg_val2.add(wifiScanList.get(i).level);

                        avg2 = getAverage(avg_val2);

                        sd2 = getStandardDeviation(avg_val2, avg2);

                        Log.d("average", avg2 + "");

                    }

                    if ((wifiScanList.get(i).SSID).trim().toString().equalsIgnoreCase("ESPap2")) {
                        Log.d("Espap", "found");

                        avg_val3.add(wifiScanList.get(i).level);

                        avg3 = getAverage(avg_val3);

                        Log.d("average", avg3 + "");

                        sd3 = getStandardDeviation(avg_val3, avg3);

                    }

                    if ((wifiScanList.get(i).SSID).trim().toString().equalsIgnoreCase("ESPap3")) {
                        Log.d("Espap3", "found");

                        avg_val4.add(wifiScanList.get(i).level);

                        avg4 = getAverage(avg_val4);

                        Log.d("average", avg4 + "");

                        sd4 = getStandardDeviation(avg_val4, avg4);

                    }

                    if ((wifiScanList.get(i).SSID).trim().toString().equalsIgnoreCase("ESPap4")) {
                        Log.d("Espap4", "found");

                        avg_val5.add(wifiScanList.get(i).level);

                        avg5 = getAverage(avg_val5);

                        Log.d("average", avg5 + "");

                        sd5 = getStandardDeviation(avg_val5, avg5);
                    }

                    if ((wifiScanList.get(i).SSID).trim().toString().equalsIgnoreCase("ESPap5")) {
                        Log.d("Espap4", "found");

                        avg_val6.add(wifiScanList.get(i).level);

                        avg6 = getAverage(avg_val6);

                        Log.d("average", avg6 + "");

                        sd6 = getStandardDeviation(avg_val6, avg6);
                    }

                    }

                if((split_time-start_time)>120 && (split_time-start_time)<130){
                    stop_flag = false;
                    findPosition();//finds the location of the car
                }
            }
        }
    }

    public void findPosition(){

        TrackerActivity trackerActivity = new TrackerActivity(avg1, avg2, avg3, avg4, avg5, avg6,
                sd1, sd2, sd3, sd4, sd5, sd6);

        resultset.setText("");

        resultset.setText(Arrays.toString(trackerActivity.getSortedArray()));

        //resultset.setText("");
        for (int i =0; i< trackerActivity.getMap().size(); i++){
            resultset.append("\n"+(i+1)+": "+trackerActivity.getMap().get(i));
        }

    }

    public void initialize(){
        avg1 = -66;
        avg2 = -75;
        avg3 = -37;
        avg4 = -62;
        avg5 = -53;
        avg6 = -73;
        sd1 = 2.5;
        sd2 = 1.6;
        sd3 = 0.7;
        sd4 = 2.4;
        sd5 = 1.06;
        sd6 = 1.79;
    }

    private class CustomView extends View {

        public CustomView(Context context){
            super(context);
            detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        }

        Paint paint;

        private float MIN_ZOOM = 1f;

        private float MAX_ZOOM = 5f;

        private float scaleFactor = 1f;

        private ScaleGestureDetector detector;

        private int mActivePointerId = INVALID_POINTER_ID;

        private boolean flag = true;

        private float mLastTouchX = 0, mLastTouchY = 0, mPosX = 0, mPosY = 0;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            detector.onTouchEvent(event);
            final int action = MotionEventCompat.getActionMasked(event);

            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    final int pointerIndex = MotionEventCompat.getActionIndex(event);
                    final float x = MotionEventCompat.getX(event, pointerIndex);
                    final float y = MotionEventCompat.getY(event, pointerIndex);

                    flag = true;//for click listener

                    // Remember where we started (for dragging)
                    mLastTouchX = x;
                    mLastTouchY = y;
                    // Save the ID of this pointer (for dragging)
                    mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                    break;
                }

                case MotionEvent.ACTION_MOVE: {
                    // Find the index of the active pointer and fetch its position
                    final int pointerIndex =
                            MotionEventCompat.findPointerIndex(event, mActivePointerId);

                    final float x = MotionEventCompat.getX(event, pointerIndex);
                    final float y = MotionEventCompat.getY(event, pointerIndex);

                    flag = false;

                    // Only move if the ScaleGestureDetector isn't processing a gesture.
                    if (!detector.isInProgress()) {
                        // Calculate the distance moved
                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        mPosX += dx;
                        mPosY += dy;

                        invalidate();
                    }
                    // Remember this touch position for the next move event
                    mLastTouchX = x;
                    mLastTouchY = y;

                    break;
                }

                case MotionEvent.ACTION_UP: {

                    if(flag){

                        double x_coordinate = (((mLastTouchX/scaleFactor)+mPosX)/Util.MAX_MAP_WIDTH)*NodesCoordinates.MAX_WIDTH;

                        double y_coordinate = ((Util.MAX_MAP_HEIGHT-((mLastTouchY/scaleFactor)+mPosY))/Util.MAX_MAP_HEIGHT)
                        *NodesCoordinates.MAX_HEIGHT;

                        //findKeyNode(x_coordinate, y_coordinate);
                    }

                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_CANCEL: {
                    mActivePointerId = INVALID_POINTER_ID;
                    break;
                }

                case MotionEvent.ACTION_POINTER_UP: {

                    final int pointerIndex = MotionEventCompat.getActionIndex(event);
                    final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);

                    if (pointerId == mActivePointerId) {
                        // This was our active pointer going up. Choose a new
                        // active pointer and adjust accordingly.
                        final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                        mLastTouchX = MotionEventCompat.getX(event, newPointerIndex);
                        mLastTouchY = MotionEventCompat.getY(event, newPointerIndex);
                        mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                    }
                    break;
                }
            }
            return true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.save();
            canvas.scale(scaleFactor, scaleFactor/*, -mPosX, -mPosY*/);

            Drawable d = getResources().getDrawable(R.drawable.ic_layoutcriotam_vector);
            d.setBounds(0, 0, d.getIntrinsicWidth()*3, d.getIntrinsicHeight()*3);
            d.draw(canvas);

            Util.MAX_MAP_WIDTH = d.getIntrinsicWidth()*3;
            Util.MAX_MAP_HEIGHT =  d.getIntrinsicHeight()*3;


            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(10);

            drawPoint(Util.MAX_MAP_WIDTH, Util.MAX_MAP_HEIGHT, paint, canvas);

            canvas.restore();

            findRoute();

        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                //scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
                scaleFactor = Math.min(scaleFactor, MAX_ZOOM);
                invalidate();
                return true;
            }
        }

        public void drawPoint(double map_width, double map_height, Paint paint, Canvas canvas){

            float point_x = (float) ((NodesCoordinates.node26[0]/NodesCoordinates.MAX_WIDTH)*(map_width));
            float point_y = (float) (map_height - ((NodesCoordinates.node26[1]/NodesCoordinates.MAX_HEIGHT)*(map_height)));

            canvas.drawCircle(point_x, point_y, 4, paint);

        }


        public void findRoute(){

            int[][] new_graph = new int[34][34];

            for(int i= 0; i<34; i++){
                for(int j = 0; j<34; j++){
                    new_graph[i][j] = (int) (NodesCoordinates.graph[i][j]*100);
                }
            }
            RouteActivity.dijkstra(new_graph,0);
        }


        public void getSourceNode(){

        }


        public double[] node_distance = new double[34];

        /*
        public void findNearesDestinationtNode(double dest_x, double dest_y){

            double x_coordinate = (dest_x/Util.MAX_MAP_WIDTH)*NodesCoordinates.MAX_WIDTH;

            double y_coordinate = ((Util.MAX_MAP_HEIGHT-dest_y)/Util.MAX_MAP_HEIGHT)*NodesCoordinates.MAX_HEIGHT;

            Util.x_destination = x_coordinate;

            Util.y_destination = y_coordinate;

            Log.d("coordinates", x_coordinate+":"+y_coordinate);

            double min = Double.MAX_VALUE;

            for(int i = 0; i<=TrackerActivity.size; i++){
                node_distance[i] = Math.sqrt(
                        Math.pow(x_coordinate-NodesCoordinates.nodes[i][0],2)
                        + Math.pow(y_coordinate - NodesCoordinates.nodes[i][1],2));

                if(node_distance[i]<min){
                    min = node_distance[i];
                    Util.destination_node = i;
                }
            }

            Log.d("Nearest destination", ""+(Util.destination_node+1));

        }
*/

        public void checkifValid(){

        }


        public int findKeyNode(double x_source_coordinate, double y_source_coordinate){

            double min = Double.MAX_VALUE;

            int node = 0;

            for(int i = 0; i<=TrackerActivity.size; i++){
                node_distance[i] = Math.sqrt(
                        Math.pow(x_source_coordinate-NodesCoordinates.nodes[i][0],2)
                                + Math.pow(y_source_coordinate - NodesCoordinates.nodes[i][1],2));

                if(node_distance[i]<min){
                    min = node_distance[i];
                    node = i;
                }
            }

            Log.d("node", (node+1)+"");

            if(node == 13 || node == 14 || node == 18 || node == 30 || node == 19 || node == 34){
                Log.d("Key node found", (node+1)+"");
                return (node+1);
            }else{
                findKeyNode(NodesCoordinates.nodes[node][0], NodesCoordinates.nodes[node][1]);
            }

            return -1;

        }
        /*
        public void findRoom(double x, double y){

            double x_coordinate = (x/Util.MAX_MAP_WIDTH)*NodesCoordinates.MAX_WIDTH;

            double y_coordinate = ((Util.MAX_MAP_HEIGHT-y)/Util.MAX_MAP_HEIGHT)*NodesCoordinates.MAX_HEIGHT;

            Log.d("coordinates", x_coordinate+":"+y_coordinate);

        }*/

    }


}
