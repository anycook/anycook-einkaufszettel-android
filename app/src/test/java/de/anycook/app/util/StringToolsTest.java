package de.anycook.app.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class StringToolsTest {

    @Test
    public void testFormatAmount() throws Exception {

        assertEquals("2", StringTools.formatAmount("2"));
        assertEquals("2,3", StringTools.formatAmount("2,3"));
        assertEquals("2,3", StringTools.formatAmount("2.3"));
        assertEquals("2,3 kg", StringTools.formatAmount("2,3kg"));
        assertEquals("2,3 kg", StringTools.formatAmount("2.3kg"));
        assertEquals("kg", StringTools.formatAmount("kg "));
        assertEquals("322,332 kg", StringTools.formatAmount(" 322.332kg "));
    }

    @Test
    public void testMergeAmounts() throws Exception {

    }

    @Test
    public void testMultiplyAmount() throws Exception {

    }
}