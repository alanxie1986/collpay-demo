package demo.newpay.util;

import java.util.Random;

public class StringUtils {


    private final static char[] randomChars = new char[]{
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N',   'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'Y', 'X',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    };

    public static String generateRandomString(int length) {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            int randomNumber = random.nextInt(randomChars.length);
            stringBuilder.append(randomChars[randomNumber]);
        }
        return stringBuilder.toString();
    }

    public static String generateRandomInteger(int length) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append((int) (1 + Math.random() * 9));
        }
        return result.toString();
    }



}
