package model;

import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebView;
import util.RuleBasedPosTagger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RootLayoutController {

    @FXML
    private TextArea inputText;
    @FXML
    private WebView webView = new WebView();
    @FXML
    private RadioButton men;
    @FXML
    private RadioButton women;
    @FXML
    private CheckBox replaceDate;
    @FXML
    private CheckBox replaceTime;

    /**
     * Инициализирует класс-контроллер. Этот метод вызывается автоматически
     * после того, как fxml-файл будет загружен.
     */
    @FXML
    private void initialize() {
    }

    private void replace(String line, boolean isWoomen, boolean isReplaseDate, boolean isReplaseTime) {
        StringBuilder builder = new StringBuilder();
        StringTokenizer st = new StringTokenizer(line, " ,;:\n\t");
        while (st.hasMoreTokens()) {
            String line1 = st.nextToken();
            if (isPronoun(line1)) {
                if (isWoomen) {
                    line1 = "она";
                } else {
                    line1 = "он";
                }
            }
            if (isFirstPersonVerb(line1)) {
                System.out.println(line1);
            }
            if (isReplaseTime) {
                if (isTime(line1)) {
                    line1 = transTime(line1);
                }
            }
            if (isReplaseDate) {
                if (isDate(line1)) {
                    line1 = transDate(line1);
                    line1 = prepareDate(line1);
                }

            }
            builder.append(line1);
            builder.append(" ");
        }
        webView.getEngine().loadContent(builder.toString());
    }

    private boolean isFirstPersonVerb(String line1) {
        RuleBasedPosTagger tagger = new RuleBasedPosTagger();
        System.out.println(tagger.posTag(line1));
        if (tagger.posTag(line1).equals(RuleBasedPosTagger.PosTag.FIRST_PERSON_VERB)) {
            return true;
        }
        return false;
    }

    private static boolean isPronoun(String line1) {
        if (line1.equalsIgnoreCase("я") || line1 == "я" || line1 == "Я") {
            return true;
        } else {
            return false;
        }
    }

    private static boolean isTime(String line) {
        String[] str = line.split("[:]");
        try {
            int x = Integer.parseInt(str[0]);
            if (x >= 0 && x <= 23) return true;
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
                strings[1] + " " + format.format(Integer.parseInt(strings[2]), ruleset) + " года";
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
                line += strings[i];
                continue;
            }
            line += format.format(Integer.parseInt(strings[i]), ruleset) + " ";
        }
        return line;
    }

    private boolean isWomen() {
        if ((!men.isSelected() && !women.isSelected())
                || men.isSelected() && women.isSelected()) {
            alert("Необходимо указать один пол.");
        }
        if (men.isSelected()) {
            return false;
        } else if (women.isSelected()) {
            return true;
        }
        return true;
    }

    @FXML
    public void handleReplaceAll() {
        if (inputText.getText() == null || inputText.getText().length() == 0) {
            alert("Введите текст для преобразования");
        }
        replace(inputText.getText(), isWomen(), replaceDate.isSelected(), replaceTime.isSelected());
//        outputText
//                .setText(replace(inputText.getText(), isWomen(), replaceDate.isSelected(), replaceTime.isSelected()));
    }

    // Показываем сообщение об ошибке.
    private void alert(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(alert.getOwner());
        alert.setTitle("Ошибка!");
        alert.setHeaderText("Внимание:");
        alert.setContentText(error);
        alert.showAndWait();
    }
}
