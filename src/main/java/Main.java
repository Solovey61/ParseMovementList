import java.io.IOException;
import java.nio.file.Path;

public class Main {

	public static void main(String[] args) throws IOException {
		Path pathOfCSV = Path.of(Main.class.getResource("movementList.csv").getPath());
		OperationsParser.parse(pathOfCSV).ifPresent(OperationsAnalyzer::printStatistics);
	}
}
