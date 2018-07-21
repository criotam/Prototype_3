package sample.criotam.avinash.indoormap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class CustomView extends View {

    public Context context;

    public Websockets websockets;

    public CustomView(Context mContext, AttributeSet attrs){
        super(mContext, attrs);

        this.context = mContext;
        //detector = new ScaleGestureDetector(getContext(), new ScaleListener());

        getSourceNode();//get the source node : TODO: main algorithm here

        websockets = new Websockets(context, new Websockets.Callback() {
            @Override
            public void onOpen() {
                //websockets.getWebsocketClient().send("admin");
            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void onMessage(final String message) {
                if(message.contains("distance")) {
                    //context.runOnUiThread(new Runnable() {
                      //  @Override
                        //public void run() {
                            move_fork_lift(Double.parseDouble(message.split("@")[0].split(":")[1]),
                                    Double.parseDouble(message.split("@")[1].split(":")[1]));

                        //}
                    //});
                }
            }
        });
        websockets.connectWebSocket();

        scaleNode();
    }

    Paint paint;

    private float MIN_ZOOM = 1f;

    private float MAX_ZOOM = 5f;

    private float scaleFactor = 1f;

    //private ScaleGestureDetector detector;

    private int mActivePointerId = INVALID_POINTER_ID;

    private boolean flag = true;

    private float mLastTouchX = 0, mLastTouchY = 0, mPosX = 0, mPosY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //detector.onTouchEvent(event);
        final int action = MotionEventCompat.getActionMasked(event);

        Log.d("MainActivity","Motionevent");

        switch (action) {
            case MotionEvent.ACTION_DOWN: {

                Log.d("MainActivity","ACION DOWN");

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

                Log.d("MainActivity","ACION MOVE");

                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(event, mActivePointerId);

                final float x = MotionEventCompat.getX(event, pointerIndex);
                final float y = MotionEventCompat.getY(event, pointerIndex);

                //flag = false;

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                    /*
                    if (!detector.isInProgress()) {
                        // Calculate the distance moved
                        final float dx = x - mLastTouchX;
                        final float dy = y - mLastTouchY;

                        //mPosX += dx;
                        //mPosY += dy;

                        invalidate();
                    }*/
                // Remember this touch position for the next move event
                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {

                Log.d("MainActivity","ACION UP");

                if(flag){

                    double x_coordinate = (((mLastTouchX/scaleFactor)+mPosX)/Util.MAX_MAP_WIDTH)*NodesCoordinates.MAX_WIDTH;

                    double y_coordinate = ((Util.MAX_MAP_HEIGHT-((mLastTouchY/scaleFactor)+mPosY))/Util.MAX_MAP_HEIGHT)
                            *NodesCoordinates.MAX_HEIGHT;

                    //out of bound
                    if(((mLastTouchX/scaleFactor)+mPosX)>Util.MAX_MAP_WIDTH &&
                            ((mLastTouchY/scaleFactor)+mPosY)> Util.MAX_MAP_HEIGHT){
                        Toast.makeText(context, "invalid position", Toast.LENGTH_SHORT).show();
                    }

                    if(checkifValid(x_coordinate, y_coordinate)) {

                        Util.x_destination_screen = ((mLastTouchX/scaleFactor)+mPosX);

                        Util.y_destination_screen = ((mLastTouchY/scaleFactor)+mPosY);

                        Util.x_destination_coordinate = x_coordinate;

                        Util.y_destination_coordinate = y_coordinate;

                        findNearesDestinationtNode(x_coordinate, y_coordinate);
                    }
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        //canvas.scale(scaleFactor, scaleFactor, -mPosX, -mPosY);


        //displaying criotam graph
        Drawable d = getResources().getDrawable(R.drawable.ic_layoutcriotam_vector);

        Util.MAX_MAP_WIDTH = d.getIntrinsicWidth()*1;
        Util.MAX_MAP_HEIGHT =  d.getIntrinsicHeight()*1;

        d.setBounds(0, 0, (int)Util.MAX_MAP_WIDTH, (int)Util.MAX_MAP_HEIGHT);
        d.draw(canvas);


        //displaying pointer

        getCurrentCoordinates();//TODO: update current coordinates

        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(10);

        //drawPoint(Util.MAX_MAP_WIDTH, Util.MAX_MAP_HEIGHT, paint, canvas);

        //draw path from source to destination
        drawPath(NodesCoordinates.path_index, canvas, paint);


        /*context.runOnUiThread(new Runnable() {
            @Override
            public void run()*/ {

                //pointer to display bot movement
                Drawable pointer = getResources().getDrawable(R.drawable.fork_lift);
                pointer.setBounds((int)Util.x_current_screen-13, (int)Util.y_current_screen-25,
                        (int)Util.x_current_screen+13, (int)Util.y_current_screen+25);
                //pointer.draw(canvas);

                Matrix matrix = new Matrix();

                Bitmap srcBitmap = BitmapFactory.decodeResource(
                        context.getResources(),
                        R.drawable.fork_lift
                );

                matrix.postScale((float)0.25,(float)0.2);

                Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                        srcBitmap.getHeight(), matrix, true);

                matrix.setRotate(
                        Util.rotation_angle, // degrees
                        bitmap.getWidth() / 2, // px
                        bitmap.getHeight() / 2 // py
                );

                matrix.postTranslate(
                        (int)Util.x_current_screen - bitmap.getWidth() / 2,
                        (int)Util.y_current_screen - bitmap.getHeight() / 2
                );

                Paint paint1 = new Paint();
                paint1.setAntiAlias(true);
                paint1.setDither(true);
                paint1.setFilterBitmap(true);

                canvas.drawBitmap(
                        bitmap, // Bitmap
                        matrix, // Matrix
                        paint // Paint
                );

            }
        //});


        canvas.restore();

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            //scaleFactor *= detector.getScaleFactor();
            //scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            //scaleFactor = Math.min(scaleFactor, MAX_ZOOM);
            //invalidate();
            return true;
        }
    }

    //for testig
    public void drawPoint(double map_width, double map_height, Paint paint, Canvas canvas){

        float point_x = (float) ((NodesCoordinates.node26[0]/NodesCoordinates.MAX_WIDTH)*(map_width));
        float point_y = (float) (map_height - ((NodesCoordinates.node26[1]/NodesCoordinates.MAX_HEIGHT)*(map_height)));

        canvas.drawCircle(point_x, point_y, 4, paint);

    }


    public void scaleNode(){

        for(int i= 0; i<34; i++){
            for(int j = 0; j<34; j++){
                NodesCoordinates.new_graph[i][j] = (int) (NodesCoordinates.graph[i][j]*100);
            }
        }

    }

    public void getCurrentCoordinates(){

        Util.x_current_screen = (float) ((Util.x_current_coordinate/NodesCoordinates.MAX_WIDTH)*(Util.MAX_MAP_WIDTH));
        Util.y_current_screen = (float) (Util.MAX_MAP_HEIGHT -
                ((Util.y_current_coordinate/NodesCoordinates.MAX_HEIGHT)*(Util.MAX_MAP_HEIGHT)));
    }

    public void getSourceNode(){

        //update current position

        Util.x_source_coordinate = NodesCoordinates.nodes[Util.source_node][0];
        Util.y_source_coordinate = NodesCoordinates.nodes[Util.source_node][1];

        Util.x_current_coordinate = Util.x_source_coordinate;
        Util.y_current_coordinate = Util.y_source_coordinate;

    }

    public void findRoutes(){
        RouteActivity.dijkstra(NodesCoordinates.new_graph,Util.source_node);
    }


    public double[] node_distance = new double[34];
    public void findNearesDestinationtNode(double dest_x, double dest_y){

        Log.d("coordinates", dest_x+":"+dest_y);

        double min = Double.MAX_VALUE;

        for(int i = 0; i<=TrackerActivity.size; i++){
            node_distance[i] = Math.sqrt(
                    Math.pow(dest_x-NodesCoordinates.nodes[i][0],2)
                            + Math.pow(dest_y - NodesCoordinates.nodes[i][1],2));

            if(node_distance[i]<min){
                min = node_distance[i];
                Util.destination_node = i+1;
            }
        }

        Log.d("Nearest destination", ""+(Util.destination_node));

        findRoutes();

        findRoutetoDestination();

    }


    public void findRoutetoDestination(){

        int path_index = -1;

        Log.d("path array size", NodesCoordinates.path_array.length+"");

        for(int i =0; i<NodesCoordinates.path_array.length; i++){

            if(NodesCoordinates.path_array[i]!=null) {
                int counter = NodesCoordinates.path_array[i].split(":", -1).length - 1;
                //Log.d(": count", "" + counter);

                if (counter != -1) {

                    if((Util.destination_node-1) == Integer.parseInt(
                            NodesCoordinates.path_array[i].split(":")[counter-1])){
                        path_index = i;
                        break;
                    }
                }
            }
        }

        NodesCoordinates.path_index = path_index;

        getPath(path_index);

        Log.d("path index", ""+path_index+": "+NodesCoordinates.path_array[path_index]);

        invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void drawPath(int index, Canvas canvas, Paint paint){

        Log.d("draw path",index+"");
        if(index!=-1) {

            float start_x = 0, start_y = 0, end_x = 0, end_y = 0;
            Paint _paint = new Paint();
            _paint.setColor(Color.GREEN);
            _paint.setStrokeWidth(5);

            Util.path_command.clear();

            if (NodesCoordinates.path_array[index] != null) {
                int counter = NodesCoordinates.path_array[index].split(":", -1).length - 1;
                //Log.d(": count", "" + counter);

                if (counter != -1) {
                    String path = NodesCoordinates.path_array[index];
                    for(int i = 0; i<counter-1; i++){

                        start_x = (float) ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i])][0]
                                /NodesCoordinates.MAX_WIDTH)*(Util.MAX_MAP_WIDTH));
                        start_y = (float) (Util.MAX_MAP_HEIGHT -
                                ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i])][1]
                                        /NodesCoordinates.MAX_HEIGHT)*(Util.MAX_MAP_HEIGHT)));

                        end_x = (float) ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i+1])][0]
                                /NodesCoordinates.MAX_WIDTH)*(Util.MAX_MAP_WIDTH));
                        end_y = (float) (Util.MAX_MAP_HEIGHT -
                                ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i+1])][1]
                                        /NodesCoordinates.MAX_HEIGHT)*(Util.MAX_MAP_HEIGHT)));

                        create_path_command(Integer.parseInt(path.split(":")[i]),
                                Integer.parseInt(path.split(":")[i+1]));

                        Log.d("points", start_x+":"+start_y+":"+end_x+":"+end_y);
                        canvas.drawLine(start_x, start_y, end_x, end_y, _paint);

                        if(i!=0){
                            drawNodes(canvas, start_x, start_y, (float) getAngle(start_x, start_y, end_x, end_y));
                        }
                    }

                    //create_path_command();

                    canvas.drawLine(end_x, end_y,
                            (float)Util.x_destination_screen,
                            (float)Util.y_destination_screen, _paint);

                    drawNodes(canvas, end_x, end_y, (float) getAngle(end_x, end_y, Util.x_destination_screen,
                            Util.y_destination_screen));

                    //TODO
                    drawDestinationPointer(canvas);
                }
            }

            String data = "";
            for(String command :Util.path_command){
                data += command + "@";
            }
            data = data.substring(0,data.length()-2);
            //sendData(data); //send data to server
        }

    }

    public void drawDestinationPointer(Canvas canvas){

        Matrix matrix = new Matrix();

        Bitmap srcBitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.ic_action_destination
        );

        //matrix.postScale((float)0.25,(float)0.2);

        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                srcBitmap.getHeight(), matrix, true);

        matrix.setRotate(
                0, // degrees
                bitmap.getWidth() / 2, // px
                bitmap.getHeight() / 2 // py
        );

        matrix.postTranslate(
                (int)Util.x_destination_screen - bitmap.getWidth() / 2,
                (int)Util.y_destination_screen - bitmap.getHeight() + 15
        );

        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setDither(true);
        paint1.setFilterBitmap(true);

        canvas.drawBitmap(
                bitmap, // Bitmap
                matrix, // Matrix
                paint // Paint
        );
    }

    public void drawNodes(Canvas canvas, double x_screen, double y_screen, float rotation){

        Matrix matrix = new Matrix();

        Bitmap srcBitmap = BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.ic_action_navigation
        );

        matrix.postScale((float)0.5,(float)0.5);

        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                srcBitmap.getHeight(), matrix, true);

        matrix.setRotate(
                rotation, // degrees
                bitmap.getWidth() / 2, // px
                bitmap.getHeight() / 2 // py
        );

        matrix.postTranslate(
                (int)x_screen - bitmap.getWidth() / 2,
                (int)y_screen - bitmap.getHeight() / 2
        );

        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        paint1.setDither(true);
        paint1.setFilterBitmap(true);

        canvas.drawBitmap(
                bitmap, // Bitmap
                matrix, // Matrix
                paint // Paint
        );

    }

    public void getPath(int index){

        Log.d("draw path",index+"");
        if(index!=-1) {

            float start_x = 0, start_y = 0, end_x = 0, end_y = 0;
            Paint _paint = new Paint();
            _paint.setColor(Color.GREEN);
            _paint.setStrokeWidth(2);

            Util.path_command.clear();

            if (NodesCoordinates.path_array[index] != null) {
                int counter = NodesCoordinates.path_array[index].split(":", -1).length - 1;
                //Log.d(": count", "" + counter);

                if (counter != -1) {
                    String path = NodesCoordinates.path_array[index];
                    for(int i = 0; i<counter-1; i++){

                        start_x = (float) ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i])][0]
                                /NodesCoordinates.MAX_WIDTH)*(Util.MAX_MAP_WIDTH));
                        start_y = (float) (Util.MAX_MAP_HEIGHT -
                                ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i])][1]
                                        /NodesCoordinates.MAX_HEIGHT)*(Util.MAX_MAP_HEIGHT)));

                        end_x = (float) ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i+1])][0]
                                /NodesCoordinates.MAX_WIDTH)*(Util.MAX_MAP_WIDTH));
                        end_y = (float) (Util.MAX_MAP_HEIGHT -
                                ((NodesCoordinates.nodes[Integer.parseInt(path.split(":")[i+1])][1]
                                        /NodesCoordinates.MAX_HEIGHT)*(Util.MAX_MAP_HEIGHT)));

                        create_path_command(Integer.parseInt(path.split(":")[i]),
                                Integer.parseInt(path.split(":")[i+1]));

                        Log.d("points", start_x+":"+start_y+":"+end_x+":"+end_y);
                        //canvas.drawLine(start_x, start_y, end_x, end_y, _paint);
                    }

                    //create_path_command();
/*
                        canvas.drawLine(end_x, end_y,
                                (float)Util.x_destination_screen,
                                (float)Util.y_destination_screen, _paint);
  */                  }
            }

            String data = "";
            for(String command :Util.path_command){
                data += command + "@";
            }
            data = data.substring(0,data.length()-2);
            sendData(data); //send data to server
        }

    }

    public void create_path_command(int src_node, int dest_node){

        double distance = Math.sqrt(Math.pow(NodesCoordinates.nodes[src_node][0]-NodesCoordinates.nodes[dest_node][0],2)
                + Math.pow(NodesCoordinates.nodes[src_node][1]-NodesCoordinates.nodes[dest_node][1],2));

        double angle = Math.toDegrees(Math.atan((NodesCoordinates.nodes[dest_node][0]-NodesCoordinates.nodes[src_node][0])/
                (NodesCoordinates.nodes[dest_node][1]-NodesCoordinates.nodes[src_node][1])));

        Log.d("angle", angle+"");

        if(NodesCoordinates.nodes[src_node][1]>NodesCoordinates.nodes[dest_node][1] &&
                NodesCoordinates.nodes[src_node][0]>=NodesCoordinates.nodes[dest_node][0]){
            Util.path_command.add("turn:"+(180-angle));
            Util.path_command.add("move:"+distance);
        }else if(NodesCoordinates.nodes[src_node][1]>NodesCoordinates.nodes[dest_node][1] &&
                NodesCoordinates.nodes[src_node][0]<=NodesCoordinates.nodes[dest_node][0]){
            Util.path_command.add("turn:"+(-180-angle));
            Util.path_command.add("move:"+distance);
        }else {
            Util.path_command.add("turn:" + (0 - angle));
            Util.path_command.add("move:" + distance);
        }
    }

    public double getAngle(double x_src, double y_src, double x_dest, double y_dest){

        double angle = Math.toDegrees(Math.atan((x_dest-x_src)/
                (y_dest-y_src)));

        Log.d("angle", angle+"");

        if(y_src > y_dest &&
                x_src >= x_dest){
            angle = 180 - angle;
        }else if(y_src > y_dest &&
                x_src <= x_dest){
            angle = -180 - angle;
        }else {
            angle = 0 - angle;
        }

        return angle + 180;
    }

    public void move_fork_lift(double distance, double angle){

        //TODO:replace current coordinate with source coordinate
        Util.x_current_coordinate = Util.x_current_coordinate - (distance*Math.sin(Math.toRadians(angle)))/100.0;
        Util.y_current_coordinate = Util.y_current_coordinate + (distance*Math.cos(Math.toRadians(angle)))/100.0;

        //Util.x_current_coordinate = NodesCoordinates.node1[0] - (distance*Math.sin(Math.toRadians(angle)))/100.0;
        //Util.y_current_coordinate = NodesCoordinates.node1[1] + (distance*Math.cos(Math.toRadians(angle)))/100.0;

        getCurrentCoordinates();

        Util.rotation_angle = (float)(0-angle);

        invalidate();
    }

    public boolean checkifValid(double dest_x, double dest_y){

        float coordinate_x = (float) dest_x;

        float coordinate_y = (float) dest_y;

        //cupboard
        if(coordinate_x > 4.07 && coordinate_x<4.608 && coordinate_y > 1.02 && coordinate_y < 2.62){
            Toast.makeText(context, "Can't move to cupboard", Toast.LENGTH_SHORT).show();
            return false;
        }

        //wall
        if(coordinate_x > 2.278 && coordinate_x < 4.608 && coordinate_y > 2.62 && coordinate_y <2.92){
            Toast.makeText(context, "Can't move to wall", Toast.LENGTH_SHORT).show();
            return false;
        }


        if(coordinate_x > 1.96 && coordinate_x < 2.07 && coordinate_y > 0 && coordinate_y<2.62){
            Toast.makeText(context, "Can't move to wall", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 1.898 && coordinate_x < 2.278 && coordinate_y > 2.62 && coordinate_y<2.92){
            Toast.makeText(context, "Can't move to wall", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 1.118 && coordinate_y > 2.62 && coordinate_y<2.92){
            Toast.makeText(context, "Can't move to wall", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 1.96 && coordinate_y > 0 && coordinate_y<0.62){
            Toast.makeText(context, "Can't move to table", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 0.62 && coordinate_y > 0 && coordinate_y<2.62){
            Toast.makeText(context, "Can't move to table", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 2.6 && coordinate_x < 4.608 && coordinate_y > 3.51 && coordinate_y<5.31){
            Toast.makeText(context, "Can't move to table", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 1.93 && coordinate_y > 3.42 && coordinate_y < 5.22){
            Toast.makeText(context, "Can't move to table", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 1.94 && coordinate_y > 5.66 && coordinate_y < 5.96){
            Toast.makeText(context, "Can't move to table", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 2.72 && coordinate_x < 2.928 && coordinate_y > 5.75 && coordinate_y < 6.75){
            Toast.makeText(context, "Can't move to wall", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 2 && coordinate_y > 6.412 && coordinate_y < 8.192){
            Toast.makeText(context, "Can't move to table", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 2.82 && coordinate_y > 5.75 ){
            Toast.makeText(context, "Can't move to empty area", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 1.118 && coordinate_y > 2.92 && coordinate_y < 3.42){
            Toast.makeText(context, "Can't move to this area", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 0 && coordinate_x < 1.93 && coordinate_y > 5.22 && coordinate_y < 5.66){
            Toast.makeText(context, "Can't move to this area", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 2.6 && coordinate_x < 4.608 && coordinate_y > 5.31 && coordinate_y < 5.75){
            Toast.makeText(context, "Can't move to this area", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 2.6 && coordinate_x < 4.608 && coordinate_y > 2.278 && coordinate_y < 3.51){
            Toast.makeText(context, "Can't move to this area", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(coordinate_x > 4.6 || coordinate_y < 0 ){
            Toast.makeText(context, "Can't move to this area", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void sendData(String data){
        if(websockets!=null){
            if(websockets.getWebsocketClient()!=null){
                if(websockets.getWebsocketClient().isOpen()){
                    websockets.getWebsocketClient().send("admin");
                    websockets.getWebsocketClient().send(data);
                }else{
                    Toast.makeText(context, "connection closed",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "connection closed",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context, "connection closed",Toast.LENGTH_SHORT).show();
        }
    }

}
