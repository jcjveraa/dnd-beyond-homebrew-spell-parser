import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class DieTest {

    @Test
    void roll() {
        var d20 = new Die(20);
        var d100 = new Die(100);

        var avgd20 = IntStream.generate(d20::roll).limit(1_000_000).average().orElseThrow();
        var avgd100 = IntStream.generate(d100::roll).limit(1_000_000).average().orElseThrow();

        assertEquals(10.5d, avgd20, 0.1d);
        assertEquals(50.5d, avgd100, 0.1d);
    }

    @Test
    void rollMultiple() {
        var d20 = new Die(2, 20);
        var d100 = new Die(4, 100);

        var avgd20 = IntStream.generate(d20::roll).limit(1_000_000).average().orElseThrow();
        var avgd100 = IntStream.generate(d100::roll).limit(1_000_000).average().orElseThrow();

        assertEquals(10.5d*2, avgd20, 0.1d);
        assertEquals(50.5d*4, avgd100, 0.1d);
    }
}