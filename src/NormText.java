import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NormText {

    public static Map<Integer, String> map = new HashMap<Integer, String>();

    static {
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
    }

    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String fileName = reader.readLine();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(replace(line));
            }
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
            //System.out.println(line1);
            if (isTime(line1)){
                line1 = transTime(line1);
            }
            if (isDate(line1)) {
                line1 = transDate(line1);
                line1 = prepareDate(line1);
            }
            builder.append(line1);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    private static boolean isTime(String line) {
        String[] str = line.split("[:]");
        try {
            int x = Integer.parseInt(str[0]);
            if (x>=0 && x <= 23) return true;
            else return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isDate(String line) {
        String pattern = "(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            return true;
        }
        return false;
    }

    private static String transDate(String line) {
        String[] strings = line.split("[.]");
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy");
        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(strings[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(strings[1]) - 1);
        calendar.set(Calendar.YEAR, Integer.parseInt(strings[2]));

        line = dateFormat.format(calendar.getTime());
        return line;
    }

    private static String prepareDate(String line) throws NumberFormatException {
        String[] strings = line.split(" ");
        RuleBasedNumberFormat format = new RuleBasedNumberFormat(new ULocale("ru"), RuleBasedNumberFormat.SPELLOUT);
        String ruleset = "%spellout-ordinal-neuter-genitive";
        line = format.format(Integer.parseInt(strings[0]), ruleset) + " " +
                strings[1] + " " + format.format(Integer.parseInt(strings[2]),ruleset) + " года";
        return line;
    }

    private static String transTime(String line) {
        String[] strings = line.split("[:]");
        line = "";
        for (int i = 0; i < strings.length; i++) {

            if (strings[i].equals("00")) {
                strings[i] = " ноль ноль";
                line+=strings[i];
            }
            try {
                int x = Integer.parseInt(strings[i]);

                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    if (x == entry.getKey()) {
                        strings[i] = entry.getValue();
                        line+=strings[i];
                    }
                }
            } catch (NumberFormatException e) {
            }

        }
        return line;

    }

}
