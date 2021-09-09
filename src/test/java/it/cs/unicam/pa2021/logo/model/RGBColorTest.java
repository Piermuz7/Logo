package it.cs.unicam.pa2021.logo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RGBColorTest {

    @Test
    public void shouldThrowIllegalArgumentException(){
        assertThrows(IllegalArgumentException.class, () -> new RGBColor(-1,2,3));
        assertThrows(IllegalArgumentException.class, () -> new RGBColor(1,-2,3));
        assertThrows(IllegalArgumentException.class, () -> new RGBColor(1,2,-3));
        assertThrows(IllegalArgumentException.class, () -> new RGBColor(-1,-2,-3));
        assertThrows(IllegalArgumentException.class, () -> new RGBColor(1111,2,3));
        assertThrows(IllegalArgumentException.class, () -> new RGBColor(1,256,3));
        assertThrows(IllegalArgumentException.class, () -> new RGBColor(1,2,378));
    }

    @Test
    public void RGBColorShouldBeCreated(){
        RGBColor color = new RGBColor(0,1,2);
        assertEquals(0,color.getRed());
        assertEquals(1,color.getGreen());
        assertEquals(2,color.getBlue());
        assertEquals(new RGBColor(0,1,2),color);
    }

}