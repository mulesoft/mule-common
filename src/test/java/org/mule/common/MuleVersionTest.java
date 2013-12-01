package org.mule.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MuleVersionTest {

    @Test
    public void testValidMuleVersions() {
        assertEquals("3.3.1", new MuleVersion("3.3.1").toString());
        assertEquals("3.4", new MuleVersion("3.4").toString());
        assertEquals("3.4-RC1", new MuleVersion("3.4-RC1").toString());
        assertEquals("3.4.1-SNAPSHOT", new MuleVersion("3.4.1-SNAPSHOT").toString());
        assertEquals("3.4-M3-SNAPSHOT", new MuleVersion("3.4-M3-SNAPSHOT").toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMuleVersion1() {
        new MuleVersion("a.b.c");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMuleVersion2() {
        new MuleVersion("1.1.SNAPSHOT");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMuleVersion3() {
        new MuleVersion("1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMuleVersion4() {
        new MuleVersion("1-RC1");
    }

    @Test
    public void testEquals() {
        assertEquals(new MuleVersion("3.3.3"), new MuleVersion("3.3.3"));
        assertEquals(new MuleVersion("3.4"), new MuleVersion("3.4"));
        assertEquals(new MuleVersion("3.4-RC1"), new MuleVersion("3.4-RC1"));
    }

    @Test
    public void testAtLeast() {
        assertTrue(new MuleVersion("3.4.1").atLeast("3.4"));
        assertTrue(new MuleVersion("3.4.1").atLeast("3.4.1"));
        assertTrue(new MuleVersion("3.4.1").atLeast("3.3.2"));
        assertTrue(new MuleVersion("3.4.1").atLeast("3.4.1-RC2"));

        // 3.4.1-RC1 is previous to the released version 3.4.1
        assertFalse(new MuleVersion("3.4.1-RC1").atLeast("3.4.1"));
    }

    @Test
    public void testAtLeastBase() {
        assertTrue(new MuleVersion("3.4.1").atLeastBase("3.4"));
        assertTrue(new MuleVersion("3.4.1-SNAPSHOT").atLeastBase("3.4"));
        assertTrue(new MuleVersion("3.3.0").atLeastBase("3.3"));
        assertTrue(new MuleVersion("3.4.0-RC1").atLeastBase("3.4"));
        assertTrue(new MuleVersion("3.5.0-M1-SNAPSHOT").atLeastBase("3.5.0"));

        assertFalse(new MuleVersion("3.4.1-RC1").atLeastBase("3.5"));
    }

    @Test
    public void testNewerThan() {
        assertTrue(new MuleVersion("3.4.1").newerThan("3.4"));
        assertTrue(new MuleVersion("3.4.1").newerThan("3.3.2"));
        assertTrue(new MuleVersion("3.4.1").newerThan("3.4.1-RC2"));
        assertTrue(new MuleVersion("3.5.0-BIGHORN").newerThan("3.5.0-ANDES"));

        assertFalse(new MuleVersion("3.4.1").newerThan("3.4.1"));
        assertFalse(new MuleVersion("3.5.0-ANDES").newerThan("3.5.0-ANDES"));
    }

    @Test
    public void testPriorTo() {
        assertTrue(new MuleVersion("3.4").priorTo("3.4.1"));
        assertTrue(new MuleVersion("3.3.2").priorTo("3.4.1"));
        assertTrue(new MuleVersion("3.4.1-RC2").priorTo("3.4.1"));
        assertTrue(new MuleVersion("3.5.0-ANDES").priorTo("3.5.0-BIGHORN"));

        assertFalse(new MuleVersion("3.4.1").priorTo("3.4.1"));
        assertFalse(new MuleVersion("3.5.0-ANDES").priorTo("3.5.0-ANDES"));
    }

}
