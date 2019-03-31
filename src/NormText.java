import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

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
        map.put(20, "двадцать");
        map.put(21, "двадцать один");
        map.put(22, "двадцать два");
        map.put(23, "двадцать три");
        map.put(24, "двадцать четыре");
        map.put(25, "двадцать пять");
        map.put(26, "двадцать шесть");
        map.put(27, "двадцать семь");
        map.put(28, "двадцать восемь");
        map.put(29, "двадцать девять");
        map.put(30, "тридцать");
        map.put(31, "тридцать один");
    }

    public static void main(String[] args) {

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String fileName = reader.readLine();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) System.out.println(replace(line));
            reader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static String replace(String line) {
        StringBuilder builder = new StringBuilder();
        StringTokenizer st = new StringTokenizer(line, " ");
        while(st.hasMoreTokens()) {
            String line1 = st.nextToken();
            if (line1.equals("00")) {
                line1 = "ноль ноль";
            }
            try {
                int x = Integer.parseInt(line1);
                if (x < 30) {
                    for (Map.Entry<Integer, String> entry : map.entrySet()) {
                        if (x == entry.getKey())
                            line1 = entry.getValue();
                    }
                } else if (x > 1999){
                    for (Map.Entry<Integer, String> entry : map.entrySet()) {
                        if ((x - 2000) == entry.getKey())
                            line1 = "две тысячи " + entry.getValue();
                    }
                }
            } catch (NumberFormatException e) {
            }
            builder.append(line1);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

}
