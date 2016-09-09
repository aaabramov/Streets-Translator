package aabrasha.ua.streettranslator.regex;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Andrii Abramov on 9/8/16.
 */
public class StringSeparationTest {

    String what;
    String pattern;

    @Before
    public void setUp() {
        what = "I am the searched sequence";
        pattern = "se";
    }

    @Test
    public void testStringSplit() {
        Assert.assertEquals(9, what.indexOf(pattern));
    }

}
