package de.anycook.app.util;


import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class StringToolsTest extends TestCase {

    @Test
    public void testFormatAmount() throws Exception {

        assertEquals("2", StringTools.formatAmount("2"));
        assertEquals("2,3", StringTools.formatAmount("2,3"));
        assertEquals("2,3", StringTools.formatAmount("2.3"));
        assertEquals("2,3 kg", StringTools.formatAmount("2,3kg"));
        assertEquals("2,3 kg", StringTools.formatAmount("2.3kg"));
    }

    @Test
    public void testMergeAmounts() throws Exception {

    }

    @Test
    public void testMultiplyAmount() throws Exception {

    }
}