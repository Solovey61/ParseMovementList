import java.util.Calendar;

public class Operation {
	private Double profit;
	private Double expense;
	private Calendar date;
	private String description;
	private TYPE type;

	public Operation(double profit, double expense, Calendar date, String description) {
		this.profit = profit;
		this.expense = expense;
		this.date = date;
		this.description = description;
		if (profit > 0 && expense == 0)
			type = TYPE.PROFIT;
		if (profit == 0 && expense > 0)
			type = TYPE.EXPENSE;
	}

	public double getProfit() {
		return profit;
	}

	public double getExpense() {
		return expense;
	}

	public Calendar getDate() {
		return date;
	}

	public TYPE getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	enum TYPE {PROFIT, EXPENSE}
}
