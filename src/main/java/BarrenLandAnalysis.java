import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Collections;

/**
 * You have a farm of 400m by 600m where coordinates of the field are from (0, 0) to (399, 599).
 * A portion of the farm is barren, and all the barren land is in the form of rectangles.
 * Due to these rectangles of barren land, the remaining area of fertile land is in no particular shape.
 * An area of fertile land is defined as the largest area of land that is not covered by any of the rectangles of barren land.
 * <p>
 * Read input from STDIN. Print output to STDOUT
 * Input
 * You are given a set of rectangles that contain the barren land.
 * These rectangles are defined in a string, which consists of four integers separated by single spaces,with no additional spaces in the string.
 * The first two integers are the coordinates of the bottom left corner in the given rectangle,
 * and the last two integers are the coordinates of the top right corner.
 * <p>
 * Output
 * Output all the fertile land area in square meters, sorted from smallest area to greatest, separated by a space.
 * <p>
 * <p>
 * SOLUTION
 * Farm is depicted as a 2x2 array of size 400x600
 * Each cell in this array is a unit of land which can be either 'f'-fertile or 'b'-barren
 * Additionally each 'f'-fertile cell is marked as 't'-traversed once traversed for calculating area
 */

public class BarrenLandAnalysis {

    int length = 400;
    int breadth = 600;
    char[][] farm = new char[length][breadth];

    /**
     * 'f' - fertile unit of land
     * 'b' - barren unit of land
     * 't' - fertile and traversed unit of land
     */

    /**
     * initialize farm
     * everything is by default fertile
     */
    void init() {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                farm[i][j] = 'f'; // 'f' - fertile unit of land
            }
        }
    }

    /**
     * A unit of land is valid if it is fertile and not traversed already so that its area can be added
     *
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if fertile and not traversed
     */
    private boolean isUnitValid(int x, int y) {

        if (x < 0 || x >= length || y < 0 || y >= breadth) {
            return false; // out of bounds
        }

        if (farm[x][y] == 'b' || farm[x][y] == 't') {
            return false; // this unit is either barren or already traversed, hence no need to add into the area
        }

        return true;
    }


    /**
     * Given a sequence of integers depicting coordinates of bottom-left and top-right corners of barren land rectangle
     * this will mark them as 'b'-barren
     *
     * @param barren sequence of integers x1, y1, x2, y2 -- bottom-left:x1,y1 top-right:x2,y2
     * @return 0 if no error, 1 if there is an error while specifying barren rectangle coordinates
     */
    int markBarrenLand(int... barren) {

        // Check for invalid conditions
        if (barren[2] <= barren[0] ||
                barren[3] <= barren[1] ||
                barren[0] < 0 ||
                barren[1] < 0 ||
                barren[2] < 0 ||
                barren[3] < 0 ||
                barren[1] >= breadth ||
                barren[0] >= length ||
                barren[2] >= length ||
                barren[3] >= breadth) {

            // invalid barren rectangle, currently just continuing by not marking
            System.out.println(
                    String.format("Invalid Barren Rectangle %d %d %d %d.",
                            barren[0], barren[1], barren[2], barren[3]));
            return 1; // return error
        }

        for (int i = barren[0]; i <= barren[2]; i++) {
            for (int j = barren[1]; j <= barren[3]; j++) {
                farm[i][j] = 'b'; //'b' - barren unit of land
            }
        }

        return 0;
    }


    /**
     * Calculate Fertile land area connected to given unit of land represented by its coordinates. Does not traverse
     * if the unit is not valid (barren or already traversed).
     *
     * @param x
     * @param y
     * @return area of fertile land connected to this unit of land. Returns zero if this is barren or already traversed unit
     */
    private int calculateFertileLandArea(int x, int y) {

        if (!isUnitValid(x, y)) return 0;

        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{x, y});
        int area = 0;

        while (!stack.isEmpty()) {
            int[] ele = stack.pop();
            int i = ele[0], j = ele[1];

            if (!isUnitValid(i, j)) continue;

            // it is a fertile unit of land, which is not traversed yet and is connected
            farm[i][j] = 't';
            area++; // add it to the area

            // Following sequence of pushing elements into the stack ensures optimal traversal
            if (isUnitValid(i, j - 1)) stack.push(new int[]{i, j - 1});
            if (isUnitValid(i, j + 1)) stack.push(new int[]{i, j + 1});
            if (isUnitValid(i + 1, j)) stack.push(new int[]{i + 1, j});
            if (isUnitValid(i - 1, j)) stack.push(new int[]{i - 1, j});
        }

        return area;
    }

    /**
     * Get a set containing all the fertile land area, sorted from smallest area to greatest
     *
     * @return Sorted List of fertile land area
     */
    List<Integer> getFertileLandAreaList() {
        ArrayList<Integer> fertileLandArea = new ArrayList<>();

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < breadth; j++) {
                int area = calculateFertileLandArea(i, j);
                if (area != 0) {
                    fertileLandArea.add(area);
                }
            }
        }
        Collections.sort(fertileLandArea);
        return fertileLandArea;
    }

    public static void main(String[] args) {

        BufferedReader in
                = new BufferedReader(new InputStreamReader(System.in));
        String input;

        console: while(true) {
            input = "";
            try {
                System.out.print("Input (x to exit) : ");
                input = in.readLine();
                if (input.trim().equals("x")) System.exit(0);
            } catch (IOException e) {
                System.out.println(String.format("Error while reading from console %s", e.getMessage()));
                continue console;
            }

            input = input.trim();
            if (input.isEmpty()) {
                System.out.println("No input !");
                continue console;
            }

            BarrenLandAnalysis barrenLandAnalysis = new BarrenLandAnalysis();
            barrenLandAnalysis.init();

            // Assuming input is in the format :: {"xx xx xxx xxx", "aa bbbb dd ee", ...}
            input = input.replaceAll("[{}\"]", "");
            String[] split = input.split(",");
            for (String coordinates : split) {
                String[] coordinate = coordinates.split(" ");
                if (coordinate.length != 4) {
                    System.out.println(String.format("Invalid Input - must be 4 coordinates :: %s", coordinates));
                    continue console;
                }
                int[] inputAr = new int[4];
                for (int i = 0; i < coordinate.length; i++) {
                    try {
                        inputAr[i] = Integer.parseInt(coordinate[i].trim());
                    } catch (NumberFormatException e) {
                        System.out.println(String.format("Invalid Input - must be integers :: %s", e.getMessage()));
                        continue console;
                    }
                }
                if (barrenLandAnalysis.markBarrenLand(inputAr) > 0) continue console;
            }

            System.out.println(barrenLandAnalysis.getFertileLandAreaList());
        }
    }
}
