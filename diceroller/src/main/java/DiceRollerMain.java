import java.util.stream.IntStream;

public class DiceRollerMain {
    public static void main(String[] args) {
        var d20 = new Die(20);
        var avg = IntStream.generate(d20::roll).limit(10_000).average().orElseThrow();
        System.out.println(avg);
    }
}
