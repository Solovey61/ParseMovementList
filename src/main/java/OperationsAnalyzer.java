import java.util.*;

class OperationsAnalyzer {
	private OperationsAnalyzer() {

	}

	static void printStatistics(List<Operation> operations) {
		printExpensesList(operations);
		printProfitsList(operations);
	}

	private static void printExpensesList(List<Operation> operations) {
		Map<String, Double> expensesMap = new HashMap<>();
		operations.stream().filter(o -> o.getType().equals(Operation.TYPE.EXPENSE)).forEach(o -> {
			expensesMap.putIfAbsent(o.getDescription(), 0.0);
			expensesMap.put(o.getDescription(), expensesMap.get(o.getDescription()) + o.getExpense());
		});
		System.out.println();
		System.out.println(String.format("%-30s %10s", "ОПИСАНИЕ", "РАСХОДЫ"));
		System.out.println("------------------------------------------");
		expensesMap.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).forEach((o) -> System.out.println(String.format("%-30s %10.2f", o.getKey(), o.getValue())));
		System.out.println("------------------------------------------");
		System.out.println(String.format("%-30s %10.2f", "ИТОГО РАСХОД", getExpenseSum(operations)));
	}

	private static void printProfitsList(List<Operation> operations) {
		Map<String, Double> profitsMap = new HashMap<>();
		operations.stream().filter(o -> o.getType().equals(Operation.TYPE.PROFIT)).forEach(o -> {
			profitsMap.putIfAbsent(o.getDescription(), 0.0);
			profitsMap.put(o.getDescription(), profitsMap.get(o.getDescription()) + o.getProfit());
		});
		System.out.println();
		System.out.println(String.format("%-30s %10s", "ОПИСАНИЕ", "ПРИБЫЛЬ"));
		System.out.println("------------------------------------------");
		profitsMap.entrySet().stream().sorted((o1, o2) -> o2.getValue().compareTo(o1.getValue())).forEach((o) -> System.out.println(String.format("%-30s %10.2f", o.getKey(), o.getValue())));
		System.out.println("------------------------------------------");
		System.out.println(String.format("%-30s %10.2f", "ИТОГО ПРИБЫЛЬ", getProfitSum(operations)));
	}

	private static double getProfitSum(List<Operation> operations) {
		return operations.stream().mapToDouble(Operation::getProfit).sum();
	}

	private static double getExpenseSum(List<Operation> operations) {
		return operations.stream().mapToDouble(Operation::getExpense).sum();
	}
}
