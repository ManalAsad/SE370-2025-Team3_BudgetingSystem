package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import org.project.models.Budget;
import org.project.models.Transaction;
import org.project.repositories.JDBC_impl.jdbcBudgetRepository;
import org.project.repositories.JDBC_impl.jdbcTransactionRepository;

/*public class TestConnection {
    public static void main(String[] args){
        DatabaseConnection.connect();
    }


}*/

public class MockDataInserter {
    public static void main(String[] args) {
        try {
            // Establish connection to the database
            Connection connection = DatabaseConnection.getConnection();

            // Create the repository instance
            jdbcBudgetRepository repository = new jdbcBudgetRepository(connection);
            jdbcTransactionRepository repository2 = new jdbcTransactionRepository(connection);


            // Create a new budget object
            Budget mockBudget = new Budget(1, "Food", 500.0, LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 30));
            Budget mockBudget_2 = new Budget(4,"shopping",700.0,LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 30));

            // Save the budget to the database
            Budget savedBudget = repository.save(mockBudget);
            Budget savedBudget_2 = repository.save(mockBudget_2);


            Transaction mockTransaction = new Transaction(LocalDate.of(2025, 4, 21), 400.0, "Groceries");
            Transaction saveTransaction = repository2.save(mockTransaction);
            System.out.println("saved transaction" + saveTransaction);

            // Output the saved budget with ID
            System.out.println("Saved budget: " + savedBudget);
            System.out.print("saved Budget: " +savedBudget_2);

            // Optionally: Insert more budgets or test other CRUD operations (find, update, delete, etc.)
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
