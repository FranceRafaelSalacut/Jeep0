package com.example.myapplication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;

//import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import android.app.Service;

import org.mockito.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

// Distance values are randomized; refer to them in assets/Final_jeep.db

public class ExampleUnitTest {
    @Mock
    SuggestedRoutes s;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void calc_distance_isCorrect() {
        SuggestedRoutesService srService = new SuggestedRoutesService(s);

        // test case 1
        String expected = "23";
        when(s.calc_distance("13B", "Gaisano Grand Mall", "Gaisano Grand Mall")).thenReturn(expected);
        String tc1 = srService.calcDist("13B", "Gaisano Grand Mall", "Gaisano Grand Mall");
        assertEquals(expected, tc1, "Test 1 successful");

        // test case 2
        expected = "71";
        when(s.calc_distance("12L", "Ayala Center Cebu", "Cebu Provincial Capitol")).thenReturn(expected);
        String tc2 = srService.calcDist("12L", "Ayala Center Cebu", "Cebu Provincial Capitol");
        assertEquals(expected, tc2, "Test 2 successful");

        // test case 3
        expected = "65";
        when(s.calc_distance("04L", "JY Square Mall", "The Golden Peak Hotel")).thenReturn(expected);
        String tc3 = srService.calcDist("04L", "JY Square Mall", "The Golden Peak Hotel");
        assertEquals(expected, tc3, "Test 3 successful");

        // test case 4
        expected = "69";
        when(s.calc_distance("12D", "Labangon Elementary School", "Gaisano Main")).thenReturn(expected);
        String tc4 = srService.calcDist("12D", "Labangon Elementary School", "Gaisano Main");
        assertEquals(expected, tc4, "Test 4 successful");
    }

    @Test
    public void calculateFare_isCorrect() {
        FareService fs = new FareService(s);

        // test case 1
        float expected = 39;
        when(s.calculateFare(19, 12, (float) 1.8, 4)).thenReturn(expected);
        float tc1 = fs.calcFare(19, 12, (float) 1.8, 4);
        assertEquals(expected, tc1);

        // test case 2
        expected = (float) 13.8;
        when(s.calculateFare((float) 6.7, 12, (float) 1.8, 4)).thenReturn(expected);
        float tc2 = fs.calcFare((float) 6.7, 12, (float) 1.8, 4);
        assertEquals(expected, tc2);

        // test case 3
        expected = (float) 18.4;
        when(s.calculateFare((float) 9.4, 12, (float) 1.6, 5)).thenReturn(expected);
        float tc3 = fs.calcFare((float) 9.4, 12, (float) 1.6, 5);
        assertEquals(expected, tc3);
    }
}