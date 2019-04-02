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
        RuleBasedNumberFormat format = new RuleBasedNumberFormat(new ULocale("ru"), RuleBasedNumberFormat.SPELLOUT);
        String ruleset = "%spellout-cardinal-feminine-accusative";

        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals("00")) {
                strings[i] = "ноль ноль";
                line+=strings[i];
                continue;
            }
            line+=format.format(Integer.parseInt(strings[i]), ruleset) + " ";
        }
        return line;
    }

}
