package ec.com.sofka;

import java.util.Random;

public class Utils {

    public static String generateCvvCode() {
        Random random = new Random();
        int randomNumber = 1000 + random.nextInt(9000);
        return String.valueOf(randomNumber);
    }
}
