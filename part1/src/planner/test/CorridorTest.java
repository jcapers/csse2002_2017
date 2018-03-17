package planner.test;

import org.junit.Before;
import planner.*;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic tests for the {@link Corridor} implementation class.
 * 
 * Write your own junit4 tests for the class here.
 */
public class CorridorTest {

    // locations to test with
    private Location[] locations;
    // corridors to test with
    private Corridor[] corridors;

    /**
     * Init locations for testing with Corridor class.
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
    }

    @Test
    public void testNewCorridors() {
        corridors = new Corridor[3];
        corridors[0] = new Corridor(locations[0], locations[1], 100);
        corridors[1] = new Corridor(locations[1], locations[2], 200);
        corridors[2] = new Corridor(locations[3], locations[4], 300);

        // Check corridor information is correct
        Assert.assertEquals("Corridor 0 Start Name", "l0",
                corridors[0].getStart().getName());
        Assert.assertEquals("Corridor 1 End Name", "l2",
                corridors[1].getEnd().getName());
        Assert.assertEquals("Corridor 2 Capacity", 300,
                corridors[2].getCapacity());

        // Check String Representation
        String expectC0String = "Corridor l0 to l1 (100)";
        String expectC1String = "Corridor l1 to l2 (200)";
        String expectC2String = "Corridor l3 to l4 (300)";
        Assert.assertEquals("Corridor 0 String", expectC0String,
                corridors[0].toString());
        Assert.assertEquals("Corridor 1 String", expectC1String,
                corridors[1].toString());
        Assert.assertEquals("Corridor 2 String", expectC2String,
                corridors[2].toString());

        // Check invariant
        for (Corridor c: corridors) {
            Assert.assertTrue(c.checkInvariant());
        }
    }

    @Test
    public void testCorridorEquality() {
        corridors = new Corridor[4];
        corridors[0] = new Corridor(locations[0], locations[1], 100);
        corridors[1] = new Corridor(locations[0], locations[1], 100);
        corridors[2] = new Corridor(locations[0], locations[1], 100);
        corridors[3] = new Corridor(locations[2], locations[3], 200);

        // Check Reflexivity (x = x)
        Assert.assertTrue(corridors[0].equals(corridors[0]));
        // Check Symmetry (x = y then y = x)
        Assert.assertTrue(corridors[0].equals(corridors[1]));
        Assert.assertTrue(corridors[1].equals(corridors[0]));
        // Check Transitivity (x = y && y = z, then x = z)
        Assert.assertTrue(corridors[1].equals(corridors[2]));
        Assert.assertTrue(corridors[0].equals(corridors[2]));
        // Check not equal
        Assert.assertFalse(corridors[0].equals(corridors[3]));

        // Check invariant
        for (Corridor c: corridors) {
            Assert.assertTrue(c.checkInvariant());
        }
    }

    @Test
    public void testCompare() {
        corridors = new Corridor[6];
        // Same Start, different ends
        corridors[0] = new Corridor(locations[0], locations[1], 100);
        corridors[1] = new Corridor(locations[0], locations[2], 150);
        // Different Start, same end
        corridors[2] = new Corridor(locations[1], locations[2], 100);
        // Capacities comparing corridors[2] in order of: Same, Greater, Less
        corridors[3] = new Corridor(locations[1], locations[2], 100);
        corridors[4] = new Corridor(locations[1], locations[2], 50);
        corridors[5] = new Corridor(locations[1], locations[2], 200);

        // Check Start less than (out -ve)
        System.out.println(corridors[0].compareTo(corridors[2]));
        // Check Start greater (out +ve)
        System.out.println(corridors[2].compareTo(corridors[0]));
        // Check Start equals will compare End less than (out -ve)
        System.out.println(corridors[0].compareTo(corridors[1]));
        // Check Start equals will compare End greater than (out +ve)
        System.out.println(corridors[1].compareTo(corridors[0]));
        // Check capacity: Same (0), Greater (+ve), Less (-ve)
        System.out.println(corridors[2].compareTo(corridors[3]));
        System.out.println(corridors[2].compareTo(corridors[4]));
        System.out.println(corridors[2].compareTo(corridors[5]));

        // Check invariant
        for (Corridor c: corridors) {
            Assert.assertTrue(c.checkInvariant());
        }
    }
}