import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    private static Map<String, Double> expensesMap = new TreeMap<>();
    private static double profitSum = 0.0;
    private static double expenseSum = 0.0;

    public static void main(String[] args) throws IOException {
        Reader fileReader = new FileReader(Main.class.getResource("movementList.csv").getFile());
        parseAndSum(fileReader);
        printExpensesAndSummary();
    }

    private static void parseAndSum(Reader fileReader) throws IOException {
        Iterable<CSVRecord> lines = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);
        for (CSVRecord line : lines) {
            profitSum += Double.parseDouble(line.get("Приход").replaceAll(",", "."));
            double recordExpense = Double.parseDouble(line.get("Расход").replaceAll(",", "."));
            expenseSum += recordExpense;
            if (recordExpense != 0) {
                String expenseDescription = getExpenseDescription(line.get("Описание операции"));
                expensesMap.putIfAbsent(expenseDescription, 0.0);
                expensesMap.put(expenseDescription, expensesMap.get(expenseDescription) + recordExpense);
            }
        }
    }

    private static void printExpensesAndSummary() {
        System.out.println(String.format("%-30s %10s", "ОПИСАНИЕ", "РАСХОДЫ"));
        System.out.println("------------------------------------------");
        expensesMap.forEach((k, v) -> System.out.println(String.format("%-30s %10.2f", k, v)));
        System.out.println("------------------------------------------");
        System.out.println(String.format("%-30s %10.2f", "ИТОГО РАСХОД", expenseSum));
        System.out.println(String.format("%-30s %10.2f", "ИТОГО ПРИХОД", profitSum));
    }

    private static String getExpenseDescription(String expenseDescriptionUnformatted) {
        if (expenseDescriptionUnformatted == null || expenseDescriptionUnformatted.isBlank() || expenseDescriptionUnformatted.isEmpty())
            return "";
        String[] expenseDescriptionSplitted = expenseDescriptionUnformatted
                .substring(20)
                .substring(0, 50)
                .trim()
                .replaceAll("/", "\\\\")
                .split("\\\\");
        return expenseDescriptionSplitted[expenseDescriptionSplitted.length - 1].trim().toUpperCase();
    }
}
