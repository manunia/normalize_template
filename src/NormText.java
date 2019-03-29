import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class NormText {

    public static Map<Integer, String> map = new HashMap<>();

    static {
        map.put(0, "ноль");
        map.put(1, "один");
        map.put(2, "два");
        map.put(3, "три");
        map.put(4, "четыре");
        map.put(5, "пять");
        map.put(6, "шесть");
        map.put(7, "семь");
        map.put(8, "восемь");
        map.put(9, "девять");
        map.put(10, "десять");
        map.put(11, "одиннадцать");
        map.put(12, "двенадцать");
        map.put(13, "тринадцать");
        map.put(14, "четырнадцать");
        map.put(15, "пятнадцать");
        map.put(16, "шестнадцать");
        map.put(17, "семнадцать");
        map.put(18, "восемнадцать");
        map.put(19, "девятнадцать");
    }

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String fileName = reader.readLine();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
        String line;
        while((line = bufferedReader.readLine()) != null) System.out.println(change(line));
        reader.close();
        bufferedReader.close();
    }


    private static String change(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(str, " ");
        while (tokenizer.hasMoreTokens()) {
            String s1 = tokenizer.nextToken();
            try {
                int i = Integer.parseInt(s1);
                for (Map.Entry<Integer, String> en: map.entrySet()) {
                    if (i == en.getKey()) s1 = en.getValue();
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            stringBuilder.append(s1);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }
}
