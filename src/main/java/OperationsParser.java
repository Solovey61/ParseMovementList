import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;

class OperationsParser {
	private OperationsParser() {
	}

	static Optional<ArrayList<Operation>> parse(Path pathToOperationsFile) throws IOException {
		Optional<ArrayList<Operation>> operations = Optional.of(new ArrayList<>());
		Iterable<CSVRecord> lines = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new BufferedReader(new FileReader(pathToOperationsFile.toFile())));
		for (CSVRecord line : lines) {
			double profit = Double.parseDouble(line.get("Приход").replaceAll(",", "."));
			double expense = Double.parseDouble(line.get("Расход").replaceAll(",", "."));
			String description = getExpenseDescription(line.get("Описание операции"));
			String[] dateSplitted = line.get("Дата операции").split("\\.");
			Calendar date = Calendar.getInstance();
			date.set(Integer.parseInt(dateSplitted[2]), Integer.parseInt(dateSplitted[1]) - 1, Integer.parseInt(dateSplitted[0]));
			operations.get().add(new Operation(profit, expense, date, description));
		}
		return operations;
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
