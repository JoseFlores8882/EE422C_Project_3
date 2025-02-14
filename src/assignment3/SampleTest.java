package assignment3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import org.junit.Rule;
import org.junit.rules.Timeout;

/**
 * This is the sample test cases for students
 *
 * @author lisahua
 */
public class SampleTest {
    private static Set<String> dict;
    private static ByteArrayOutputStream outContent;

    private static final int SHORT_TIMEOUT = 300; // ms
    private static final int SEARCH_TIMEOUT = 30000; // ms

    @Rule// Comment this rule out when debugging to remove timeouts
    public Timeout globalTimeout = new Timeout(SEARCH_TIMEOUT);

    @Before // this method is run before every test
    public void setUp() {
        assignment3.Main.initialize();
        dict = Main.makeDictionary();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    private boolean verifyLadder(ArrayList<String> ladder, String start, String end) {
        String prev = null;
        if (ladder == null)
            return true;
        for (String word : ladder) {
            if (!dict.contains(word.toUpperCase()) && !dict.contains(word.toLowerCase())) {
                return false;
            }
            if (prev != null && !differByOne(prev, word))
                return false;
            prev = word;
        }
        return ladder.size() > 0
                && ladder.get(0).toLowerCase().equals(start)
                && ladder.get(ladder.size() - 1).toLowerCase().equals(end);
    }

    private static boolean differByOne(String s1, String s2) {
        if (s1.length() != s2.length())
            return false;

        int diff = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i) && diff++ > 1) {
                return false;
            }
        }

        return true;
    }

    /**
     * Has Word Ladder
     **/
    @Test
    public void testBFS1() {
        ArrayList<String> res = Main.getWordLadderBFS("hello", "cells");

        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "hello", "cells"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);
        assertTrue(res.size() < 6);
    }

    @Test
    public void testDFS1() {
        ArrayList<String> res = Main.getWordLadderDFS("hello", "cells");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "hello", "cells"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);

    }

    /**
     * No Word Ladder
     **/
    @Test
    public void testBFS2() {
        ArrayList<String> res = Main.getWordLadderBFS("aldol", "drawl");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(res == null || res.size() == 0 || res.size() == 2);

    }

    @Test
    public void testDFS2() {
        ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(res == null || res.size() == 0 || res.size() == 2);
    }

    @Test
    public void testPrintLadder() {
        ArrayList<String> res = Main.getWordLadderBFS("twixt", "hakus");
        outContent.reset();
        Main.printLadder(res);
        String str = outContent.toString().replace("\n", "").replace(".", "").trim();
        assertEquals("no word ladder can be found between twixt and hakus", str);
    }
}
