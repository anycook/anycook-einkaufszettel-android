package de.anycook.app.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@Config(emulateSdk = 18, manifest = Config.NONE)
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
        assertEquals("2 bla + 2 blub", StringTools.mergeAmounts("2 bla", "2 blub"));
        assertEquals("5", StringTools.mergeAmounts("2", "3"));
        assertEquals("250 kg", StringTools.mergeAmounts("120 kg", "130 kg"));
        assertEquals("5 Stück", StringTools.mergeAmounts("2 Stück", "3 Stück"));
        assertEquals("4,35", StringTools.mergeAmounts("4,12", "0,23"));
        assertEquals("4,35", StringTools.mergeAmounts("4,12011", "0,23"));
        assertEquals("5,76 blub", StringTools.mergeAmounts("3,4 blub", "2,36 blub"));
    }

    @Test
    public void testMultiplyAmount() throws Exception {
        //test fractions
        assertEquals("1/2", StringTools.multiplyAmount("2/4", 1, 1));
        assertEquals("1/2", StringTools.multiplyAmount("1/4", 1, 2));
        assertEquals("5/16", StringTools.multiplyAmount("1/4", 4, 5));
        assertEquals("1/5", StringTools.multiplyAmount("1/4", 5, 4));
        assertEquals("5/21", StringTools.multiplyAmount("1/3", 7, 5));
        assertEquals("1", StringTools.multiplyAmount("1/4", 1, 4));
    }
}