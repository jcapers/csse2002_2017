package planner.test;

import planner.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

/**
 * Basic tests for the {@link Traffic} implementation class.
 * 
 * A more extensive test suite will be performed for assessment of your code,
 * but this should get you started writing your own unit tests.
 */
public class TrafficTest {

    // Correct line separator for executing machine (used in toString method)
    private final static String LINE_SEPARATOR = System.getProperty(
            "line.separator");
    // locations to test with
    private Location[] locations;
    // corridors to test with
    private Corridor[] corridors;

    /**
     * This method is run by JUnit before each test to initialise instance
     * variables locations and corridors.
     */
    @Before
    public void setUp() {
        // locations to test with
        locations = new Location[6];
        locations[0] = new Location("l0");
        locations[1] = new Location("l1");
        locations[2] = new Location("l2");
        locations[3] = new Location("l3");
        locations[4] = new Location("l4");
        locations[5] = new Location("l4");

        // corridors to test with
        corridors = new Corridor[7];
        corridors[0] = new Corridor(locations[0], locations[1], 100);
        corridors[1] = new Corridor(locations[1], locations[2], 200);
        corridors[2] = new Corridor(locations[2], locations[3], 300);
        corridors[3] = new Corridor(locations[3], locations[4], 400);
        corridors[4] = new Corridor(locations[3], locations[0], 400);
        corridors[5] = new Corridor(locations[3], locations[4], 100);
        corridors[6] = new Corridor(locations[0], locations[1], 100);
    }

    /**
     * Basic test of the empty constructor of the class.
     */
    @Test
    public void testEmptyConstructor() {
        // the Traffic object under test
        Traffic traffic = new Traffic();

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        String expectedString = "";

        // check the traffic on a non-null corridor
        Assert.assertEquals(0, traffic.getTraffic(corridors[0]));
        // check that there are no corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Basic test of the updateTraffic method.
     */
    @Test
    public void testUpdateTraffic() {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 50);
        traffic.updateTraffic(corridors[0], 20);
        traffic.updateTraffic(corridors[1], -10);

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        String expectedString = "Corridor l0 to l1 (100): 30" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 40" + LINE_SEPARATOR;

        // check the traffic on some corridors
        Assert.assertEquals(30, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(40, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[2]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Check that the appropriate exception is thrown if updateTraffic is called
     * with a null corridor.
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateTrafficNullPointer() {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(null, 20);
    }

    /**
     * Check that the appropriate exception is thrown if a call to updateTraffic
     * would result in a negative amount of traffic on a corridor.
     */
    @Test(expected = InvalidTrafficException.class)
    public void testUpdateTrafficInvalidTrafficException() {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], -10);
    }

    /**
     * Basic test of the addTraffic method.
     */
    @Test
    public void testAddTraffic() {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 30);

        // the extra traffic to add
        Traffic extraTraffic = new Traffic();
        extraTraffic.updateTraffic(corridors[1], 40);

        traffic.addTraffic(extraTraffic);

        // expected results, should sort for corridor[0] < corridor[1]
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[1]);
        expectedCorridorsWithTraffic.add(corridors[0]);
        String expectedString = "Corridor l0 to l1 (100): 10" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 70" + LINE_SEPARATOR;

        // check the traffic on some corridors
        Assert.assertEquals(10, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(70, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[2]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());

        // add new corridors to update and check consistency
        traffic.updateTraffic(corridors[5], 20);  // l3 to l4
        traffic.updateTraffic(corridors[4], 60);  // l3 to l0
        expectedCorridorsWithTraffic.add(corridors[4]);
        expectedCorridorsWithTraffic.add(corridors[5]);
        String expectedStr2 = "Corridor l0 to l1 (100): 10" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 70" + LINE_SEPARATOR +
                "Corridor l3 to l0 (400): 60" + LINE_SEPARATOR +
                "Corridor l3 to l4 (100): 20" + LINE_SEPARATOR;

        // Check string representation and corridors with traffic
        Assert.assertEquals(expectedStr2, traffic.toString());
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the traffic on new corridors
        Assert.assertEquals(20, traffic.getTraffic(corridors[5]));
        Assert.assertEquals(60, traffic.getTraffic(corridors[4]));
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());

        // add zero traffic corridor
        traffic.updateTraffic(corridors[2], 0);

        // Check string representation and expectedTraffic (should be no change)
        Assert.assertEquals(expectedStr2, traffic.toString());
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the traffic on new corridors
        Assert.assertEquals(0, traffic.getTraffic(corridors[2]));
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());

    }

    /**
     * Check that the appropriate exception is thrown if a call to addTraffic is
     * passed a null parameter.
     */
    @Test(expected = NullPointerException.class)
    public void testAddNullTraffic() {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.addTraffic(null);
    }

    /**
     * Basic test of the copy constructor
     */
    @Test
    public void testCopyConstructor() {
        // the Traffic object that will be copied
        Traffic initialTraffic = new Traffic();
        initialTraffic.updateTraffic(corridors[0], 10);
        initialTraffic.updateTraffic(corridors[1], 70);
        initialTraffic.updateTraffic(corridors[2], 20);
        initialTraffic.updateTraffic(corridors[3], 0);
        // the Traffic object under test
        Traffic traffic = new Traffic(initialTraffic);

        // expected results
        Set<Corridor> expectedCorridorsWithTraffic = new HashSet<>();
        expectedCorridorsWithTraffic.add(corridors[0]);
        expectedCorridorsWithTraffic.add(corridors[1]);
        expectedCorridorsWithTraffic.add(corridors[2]);
        String expectedString = "Corridor l0 to l1 (100): 10" + LINE_SEPARATOR
                + "Corridor l1 to l2 (200): 70" + LINE_SEPARATOR
                + "Corridor l2 to l3 (300): 20" + LINE_SEPARATOR;

        // Check same traffic
        Assert.assertTrue(traffic.sameTraffic(initialTraffic));
        // check the traffic on some corridors
        Assert.assertEquals(10, traffic.getTraffic(corridors[0]));
        Assert.assertEquals(70, traffic.getTraffic(corridors[1]));
        Assert.assertEquals(20, traffic.getTraffic(corridors[2]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[3]));
        Assert.assertEquals(0, traffic.getTraffic(corridors[4]));
        // check the corridors with traffic
        Assert.assertEquals(expectedCorridorsWithTraffic, traffic
                .getCorridorsWithTraffic());
        // check the string representation
        Assert.assertEquals(expectedString, traffic.toString());
        // check that the class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

    /**
     * Check that the appropriate exception is thrown when a new instance of the
     * class Traffic is constructed with a null argument.
     */
    @Test(expected = NullPointerException.class)
    public void testCopyConstructorNullArgument() {
        // the Traffic object under test
        Traffic traffic = new Traffic(null);
    }

    /** Basic test for the sameTraffic method **/
    @Test
    public void testSameTrafficMethod() {
        // the Traffic object under test, testing same after update
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 20);

        // the Traffic object to compare
        Traffic other = new Traffic();
        other.updateTraffic(corridors[0], 10);

        // check that the objects are not currently the same
        Assert.assertFalse(traffic.sameTraffic(other));

        // update other so that they are now the same and check
        other.updateTraffic(corridors[1], 20);
        Assert.assertTrue(traffic.sameTraffic(other));

        // Test same corridor should update the same corridor only
        Traffic sameTraffic = new Traffic();
        sameTraffic.updateTraffic(corridors[0], 10);
        sameTraffic.updateTraffic(corridors[6], 5);

        // check that new corridor value is 15 for the corridor
        for (Corridor c : sameTraffic.getCorridorsWithTraffic()) {
            Assert.assertEquals(15, sameTraffic.getTraffic(c));
        }
        Assert.assertEquals(1, sameTraffic.getCorridorsWithTraffic().size());

        // Test Zero Traffic Corridors are same, setup two explicit Corridors.
        Traffic zeroTrafficA = new Traffic();
        zeroTrafficA.updateTraffic(corridors[0], 0);
        Traffic zeroTrafficB = new Traffic();
        zeroTrafficB.updateTraffic(corridors[1], 0);

        // Test zeroTrafficA && zeroTrafficB are same
        Assert.assertTrue(zeroTrafficA.sameTraffic(zeroTrafficB));
    }

    /**
     * Test that the sameTraffic method throws the appropriate exception when
     * the parameter is null
     */
    @Test(expected = NullPointerException.class)
    public void testSameTrafficMethodNullParameter() {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[0], 10);
        traffic.updateTraffic(corridors[1], 20);

        traffic.sameTraffic(null);
    }

    /*
     * Test Ordering with following Corridors:
     * corridors[3] = new Corridor(locations[3], locations[4], 400);
     * corridors[4] = new Corridor(locations[3], locations[0], 400);
     * corridors[5] = new Corridor(locations[3], locations[4], 100);
     */
    @Test
    public void testOrdering() {
        // the Traffic object under test
        Traffic traffic = new Traffic();
        traffic.updateTraffic(corridors[3], 100);
        traffic.updateTraffic(corridors[4], 100);
        traffic.updateTraffic(corridors[5], 100);

        // Expected result
        String expectedString = "Corridor l3 to l0 (400): 100" + LINE_SEPARATOR
                + "Corridor l3 to l4 (100): 100" + LINE_SEPARATOR
                + "Corridor l3 to l4 (400): 100" + LINE_SEPARATOR;

        // Check string rep is in natural order and provides correct output.
        Assert.assertEquals(expectedString, traffic.toString());

        // Check that class invariant holds
        Assert.assertTrue(traffic.checkInvariant());
    }

}
