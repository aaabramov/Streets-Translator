package aabrasha.ua.streettranslator.util;

import org.junit.Test;

import static aabrasha.ua.streettranslator.util.StreetNameCleaner.clean;
import static org.junit.Assert.assertEquals;

/**
 * @author Andrii Abramov on 12/25/16.
 */
public class StreetNameCleanerTest {

    @Test()
    public void testУлица() throws Exception {
        assertEquals("Сарафимовича", clean("улица Сарафимовича"));
        assertEquals("Сарафимовича", clean("ул. Сарафимовича"));
    }

    @Test()
    public void testПлощадь() throws Exception {
        assertEquals("Сарафимовича", clean("площадь Сарафимовича"));
        assertEquals("Сарафимовича", clean("пл. Сарафимовича"));
    }

    @Test()
    public void testПроспект() throws Exception {
        assertEquals("Сарафимовича", clean("проспект Сарафимовича"));
        assertEquals("Сарафимовича", clean("пр. Сарафимовича"));
    }

    @Test()
    public void testПереулок() throws Exception {
        assertEquals("Сарафимовича", clean("переулок Сарафимовича"));
        assertEquals("Сарафимовича", clean("пер. Сарафимовича"));
    }

    @Test()
    public void testБульвар() throws Exception {
        assertEquals("Сарафимовича", clean("бульвар Сарафимовича"));
        assertEquals("Сарафимовича", clean("бул. Сарафимовича"));
    }
}