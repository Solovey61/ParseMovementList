import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

public class Main {

	public static void main(String[] args) throws IOException {
		Reader fileReader = new FileReader(Main.class.getResource("movementList.csv").getFile());
		ArrayList<Operation> operations = getOperationsList(fileReader);
		listGroupedExpenses(operations);
		System.out.println(String.format("%-30s %10.2f", "ИТОГО РАСХОД", getExpenseSum(operations)));
		System.out.println(String.format("%-30s %10.2f", "ИТОГО ПРИХОД", getProfitSum(operations)));
	}

	private static ArrayList<Operation> getOperationsList(Reader fileReader) throws IOException {
		ArrayList<Operation> operations = new ArrayList<>();
		Iterable<CSVRecord> lines = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(fileReader);
		for (CSVRecord line : lines) {
			double profit = Double.parseDouble(line.get("Приход").replaceAll(",", "."));
			double expense = Double.parseDouble(line.get("Расход").replaceAll(",", "."));
			String description = getExpenseDescription(line.get("Описание операции"));
			String[] dateSplitted = line.get("Дата операции").split("\\.");
			Calendar date = Calendar.getInstance();
			date.set(Integer.parseInt(dateSplitted[2]), Integer.parseInt(dateSplitted[1]) - 1, Integer.parseInt(dateSplitted[0]));
			operations.add(new Operation(profit, expense, date, description));
		}
		return operations;
	}

	private static double getProfitSum(List<Operation> operations) {
		return operations.stream().mapToDouble(Operation::getProfit).sum();
	}

	private static double getExpenseSum(List<Operation> operations) {
		return operations.stream().mapToDouble(Operation::getExpense).sum();
	}

	private static void listGroupedExpenses(List<Operation> operations) {
		Map<String, Double> expensesMap = new HashMap<>();
		operations.stream().filter(o -> o.getType().equals(Operation.TYPE.EXPENSE)).forEach(o -> {
			expensesMap.putIfAbsent(o.getDescription(), 0.0);
			expensesMap.put(o.getDescription(), expensesMap.get(o.getDescription()) + o.getExpense());
		});

		System.out.println(String.format("%-30s %10s", "ОПИСАНИЕ", "РАСХОДЫ"));
		System.out.println("------------------------------------------");
		expensesMap.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).forEach((o) -> System.out.println(String.format("%-30s %10.2f", o.getKey(), o.getValue())));
		System.out.println("------------------------------------------");
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
