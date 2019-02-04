import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class BarrenLandAnalysisTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {

        ArrayList<int[]> input1 = new ArrayList<>();
        input1.add(new int[] {0, 292, 399, 307});
        ArrayList<Integer> output1 = new ArrayList<>();
        output1.add(116800);
        output1.add(116800);

        ArrayList<int[]> input2 = new ArrayList<>();
        input2.add(new int[] {48, 192, 351, 207});
        input2.add(new int[] {48, 392, 351, 407});
        input2.add(new int[] {120, 52, 135, 547});
        input2.add(new int[] {260, 52, 275, 547});
        ArrayList<Integer> output2 = new ArrayList<>();
        output2.add(22816);
        output2.add(192608);

        return Arrays.asList(new Object[][]{
                {input1, output1},
                {input2, output2}
        });
    }

    @Parameterized.Parameter(value = 0)
    public ArrayList<int[]> barrenLandRectangles;

    @Parameterized.Parameter(value = 1)
    public ArrayList<Integer> fertileAreaList;

    @Test
    public void testGetFertileLandAreaList() throws Exception {
        BarrenLandAnalysis barrenLandAnalysis = new BarrenLandAnalysis();
        barrenLandAnalysis.init();
        for (int[] bRect : barrenLandRectangles) {
            barrenLandAnalysis.markBarrenLand(bRect);
        }
        assertEquals(barrenLandAnalysis.getFertileLandAreaList(), fertileAreaList);
    }
}