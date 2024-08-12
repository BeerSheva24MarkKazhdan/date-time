
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import telran.time.NextFriday13;
import telran.time.PastTemporalDateProximity;


public class DateTimeTest {

    private Temporal[] newArray;
    private TemporalAdjuster dateProximityAdjuster;

@Test
void localDateTest (){
    LocalDate current = LocalDate.now();
    LocalDateTime currentTime = LocalDateTime.now();
    ZonedDateTime currentZonedTime = ZonedDateTime.now();
    Instant currentInstant = Instant.now();
    LocalTime currentLocalTime = LocalTime.now();
    System.out.printf("Current date is %s in ISO format \n",current);
    System.out.printf("Current date & time is %s in ISO format \n",currentTime);
    System.out.printf("Current zoned date & time is %s in ISO format \n",currentZonedTime);
    System.out.printf("Current instantr %s in ISO format \n",currentInstant);
    System.out.printf("Current time %s in ISO format \n",currentLocalTime);
    System.out.printf("Current date is %s in dd/mm/yyyy \n",current.format(DateTimeFormatter.ofPattern("dd/MMMM/yyyy", Locale.forLanguageTag("eng"))));
}
@Test
void nextFriday13Test(){
    LocalDate current = LocalDate.of(2024,8,11);
    LocalDate expected = LocalDate.of(2024,9,13);
    TemporalAdjuster adjuster = new NextFriday13();
    assertEquals(expected, current.with(adjuster));
    assertThrows(RuntimeException.class, () -> LocalTime.now().with(adjuster));
}
@BeforeEach
void setUp(){
    newArray = new Temporal[]{
    LocalDate.of(2023,8,6), 
    LocalDate.of(2020,1,16), 
    LocalDate.of(2025,5,4), 
    LocalDate.of(2008,4,9)};
    dateProximityAdjuster = new PastTemporalDateProximity(newArray);
    }

    @Test
    void findsNearestPastDateTest() {
        LocalDate current = LocalDate.of(2024,8,11);
        LocalDate expected = LocalDate.of(2023,8,6);
        assertEquals(expected, dateProximityAdjuster.adjustInto(current));
    }

    @Test
    void onlyOnePastDateTest() {
        Temporal[] lonelyArray = {LocalDate.of(2023,8,6)};
        dateProximityAdjuster = new PastTemporalDateProximity(lonelyArray);
        LocalDate current = LocalDate.of(2024,8,11);
        LocalDate expected = LocalDate.of(2023,8,6);
        assertEquals(expected, dateProximityAdjuster.adjustInto(current));
    }

    @Test
    void noPastDateTest(){
        LocalDate current = LocalDate.of(2000,4,9);
        assertThrows(IllegalStateException.class, () -> current.with(dateProximityAdjuster));
    }

    @Test
    void emptyArrayTest() {
        Temporal[] emptyArray = {};
        dateProximityAdjuster = new PastTemporalDateProximity(emptyArray);

        LocalDate current = LocalDate.of(2000,4,9);

        assertThrows(IllegalStateException.class, () -> current.with(dateProximityAdjuster));
    }

    @Test
    void nullArrayTest() {
        assertThrows(NullPointerException.class, () -> new PastTemporalDateProximity(null));
}

@Test
void wrongChronoUnitTest(){
    Temporal[] wrongArray = {LocalTime.of(12,1)};
    dateProximityAdjuster = new PastTemporalDateProximity(wrongArray);
    LocalTime current = LocalTime.of(11,0);
    assertThrows(IllegalArgumentException.class, () -> current.with(dateProximityAdjuster));
}
}