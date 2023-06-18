
import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import java.util.Arrays;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class CalcolatoreUnitTest {
    @Test
    public void test1() {
        assertEquals(4, 2 + 2);
    }


    public static void main(String[] args) {

        CalcolatoreDisparities cal1 = new CalcolatoreDisparities();
        CalcolatoreDisparities cal2 = new CalcolatoreDisparities(12);

        int [] values1;
        values1 = cal1.getCurrentDisparities(cal1.maxDisparity, cal1.minDisparity);

        System.out.println(Arrays.toString(values1));

        int [] values2;
        values2 = cal2.getCurrentDisparities(cal2.maxDisparity, cal2.minDisparity);
        System.out.println(Arrays.toString(values2));

        // selezione la posizione 10
        boolean[] sol = new boolean[6];
    }

}