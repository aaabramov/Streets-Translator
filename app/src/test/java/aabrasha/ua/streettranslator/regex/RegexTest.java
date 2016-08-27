package aabrasha.ua.streettranslator.regex;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by Andrii Abramov on 8/28/16.
 */
public class RegexTest {

    @Test
    public void testSplitByNewLine() {
        //given
        String text = "1\n3\n3";
        //when
        final String[] parts = text.split("\\n");
        //then
        Assert.assertTrue("Number of lines", parts.length == 3);
    }

}
