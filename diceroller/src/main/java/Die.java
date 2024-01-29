import lombok.RequiredArgsConstructor;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
public class Die {
    final int number;
    final int sides;

    public Die(int sides) {
        this.number = 1;
        this.sides = sides;
    }

    public int roll() {
        int sum = 0;
        for (int i = 0; i < number; i++) {
            sum += ThreadLocalRandom.current().nextInt(1, sides+1);
        }
        return sum;
    }
}
