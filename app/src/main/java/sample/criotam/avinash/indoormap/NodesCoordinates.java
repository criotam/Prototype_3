package sample.criotam.avinash.indoormap;

public class NodesCoordinates {

    public static double MAX_HEIGHT = 8.73;
    public static double MAX_WIDTH = 4.608;

    public static double[] node1 = {3.94,0.42};
    public static double[] node2 = {3.41,0.42};
    public static double[] node3 = {2.88,0.42};
    public static double[] node4 = {2.35,0.42};

    public static double[] node5 = {3.68,0.9};
    public static double[] node6 = {3.15,0.9};
    public static double[] node7 = {2.62,0.9};

    public static double[] node8 = {3.68,1.43};
    public static double[] node9 = {3.15,1.43};
    public static double[] node10 = {2.62,1.43};

    public static double[] node11 = {2.62,1.96};
    public static double[] node12 = {2.62,2.49};
    public static double[] node13 = {2.68,2.92};

    public static double[] node14 = {2.37,3.57};
    public static double[] node15 = {2.37,4.10};
    public static double[] node16 = {2.37,4.63};
    public static double[] node17 = {2.37,5.16};

    public static double[] node18 = {2.33,5.75};
    public static double[] node19 = {2.33,6.28};
    public static double[] node20 = {2.33,6.81};
    public static double[] node21 = {2.33,7.34};
    public static double[] node22 = {2.33,7.87};

    public static double[] node23 = {1.56,6.28};
    public static double[] node24 = {1.03,6.28};
    public static double[] node25 = {0.5,6.28};

    public static double[] node26 = {1.8,8.40};
    public static double[] node27 = {1.27,8.40};
    public static double[] node28 = {0.74,8.40};
    public static double[] node29 = {0.21,8.40};

    public static double[] node30 = {1.508,2.92};
    public static double[] node31 = {1.508,2.27};
    public static double[] node32 = {1.508,1.74};
    public static double[] node33 = {1.508,1.21};

    public static double[] node34 = {2.33,8.40};//Missed node

    public static double[][] nodes = {
            node1, node2, node3, node4, node5, node6, node7, node8, node9, node10, node11, node12, node13, node14, node15, node16,
            node17, node18, node19, node20, node21, node22, node23, node24, node25, node26, node27, node28, node29, node30,
            node31, node32, node33, node34
    };

    public static double[][] graph = {

            //Room2
            {0,1,2,3,1,1.74,2.65,1.97,2.42,3.13,3.83,4.64,5.25,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,0,1,2,1,1,1.74,1.97,1.97,2.42,3.26,4.19,4.91,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {2,1,0,1,1.74,1,1,2.43,1.97,1.97,2.94,3.94,4.74,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {3,2,1,0,2.65,1.74,1,3.15,2.43,1.97,3.36,3.94,4.74,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1,1,1.74,2.65,0,1,2,1,1.41,2.36,2.81,3.60,4.25,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1.74,1,1,1.74,1,0,1,1.41,1,1.41,2.24,3.17,3.94,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {2.65,1.74,1,1,2,1,0,2.24,1.41,1,2,3,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {1.97,1.97,2.43,3.15,1,1.41,2.24,0,1,2,2.24,2.83,3.39,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {2.42,1.97,1.97,2.43,1.41,1,1.41,1,0,1,1.41,2.24,2.95,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {3.13,2.42,1.97,1.97,2.36,1.41,1,2,1,0,1,2,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {3.83,3.26,2.94,2.94,2.81,2.24,2,2.24,1.14,1,0,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {4.64,4.19,3.94,3.94,3.60,3.17,3,2.83,2.24,2,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {5.25,4.91,4.74,4.74,4.25,3.94,4,3.39,2.95,3,2,1,0,1.74,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},

            //Hall
            {0,0,0,0,0,0,0,0,0,0,0,0,1.74,0,1,2,3,4,0,0,0,0,0,0,0,0,0,0,0,1.74,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,2,3,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,0,1,2,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,1,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},

            //room1
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,2,3,4,0,0,0,0,0,0,0,0,0,0,0,5},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,2,3,1,2,3,0,0,0,0,0,0,0,0,4},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,0,1,2,0,0,0,0,0,0,0,0,0,0,0,3},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,1,0,1,0,0,0,0,0,0,0,0,0,0,0,2},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,3,2,1,0,0,0,0,0,0,0,0,0,0,0,0,1},

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,1,2,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,0,0,0,1,0,1,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,0,0,0,2,1,0,0,0,0,0,0,0,0,0,0},

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,0,0,0,0,1},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,2,0,0,0,0,2},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,0,1,0,0,0,0,3},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,1,0,0,0,0,0,4},

            //kitchen
            {0,0,0,0,0,0,0,0,0,0,0,0,0,1.74,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,2,3,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,1,2,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2,1,0,1,0},
            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,3,2,1,0,0},

            {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,5,4,3,2,1,0,0,0,1,2,3,4,0,0,0,0,0}
    };

    public static String[] path_array = new String[34];
}
