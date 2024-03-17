import java.sql.*;
import java.util.Scanner;

public class DaveCarbonara {
    // JDBC URL, username, and password of MySQL server
    public static final String DB_URL = "jdbc:mysql://localhost:3306/mysql";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "5290IMPORTANCE";

    // JDBC variables for opening, closing, and managing connection
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;

    public static void main(String[] args) {
        try {
            // Connect to the database
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create a statement
            statement = connection.createStatement();

            // Create the necessary tables if they don't exist
            createTables();

            // Scanner for user input
            Scanner scanner = new Scanner(System.in);

            // Perform CRUD operations based on user input
            while (true) {
                System.out.println("Choose an operation:");
                System.out.println("1. Add a client");
                System.out.println("2. Get client information");
                System.out.println("3. Update client information");
                System.out.println("4. Delete a client");
                System.out.println("5. View all clients");
                System.out.println("6. Add a service");
                System.out.println("7. View all services");
                System.out.println("8. Update service information");
                System.out.println("9. Delete a service");
                System.out.println("10. View total hours billed for each service");
                System.out.println("11. Create a new invoice");
                System.out.println("12. Add service to an invoice");
                System.out.println("13. Update service hours in an invoice");
                System.out.println("14. Delete an invoice");
                System.out.println("15. View all invoices for a client");
                System.out.println("16. View total amount for each invoice");
                System.out.println("17. Total income for a given period");
                System.out.println("18. Most popular service for a given period");
                System.out.println("19. Top client for a given period");
                System.out.println("20. Exit");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character

                switch (choice) {
                    case 1:
                        // Add a client
                        System.out.print("Enter client name: ");
                        String clientName = scanner.nextLine();
                        System.out.print("Enter client email: ");
                        String clientEmail = scanner.nextLine();
                        System.out.print("Enter client phone: ");
                        String clientPhone = scanner.nextLine();
                        addClient(clientName, clientEmail, clientPhone);
                        break;
                    case 2:
                        // Get client information
                        System.out.print("Enter client ID: ");
                        int clientId = scanner.nextInt();
                        getClient(clientId);
                        break;
                    case 3:
                        // Update client information
                        System.out.print("Enter client ID: ");
                        int updateClientId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        System.out.print("Enter new name: ");
                        String updatedName = scanner.nextLine();
                        System.out.print("Enter new email: ");
                        String updatedEmail = scanner.nextLine();
                        System.out.print("Enter new phone: ");
                        String updatedPhone = scanner.nextLine();
                        updateClient(updateClientId, updatedName, updatedEmail, updatedPhone);
                        break;
                    case 4:
                        // Delete a client
                        System.out.print("Enter client ID: ");
                        int deleteClientId = scanner.nextInt();
                        deleteClient(deleteClientId);
                        break;
                    case 5:
                        // View all clients
                        viewClients();
                        break;
                    case 6:
                        // Add a service
                        System.out.print("Enter service name: ");
                        String serviceName = scanner.nextLine();
                        System.out.print("Enter hourly rate: ");
                        double hourlyRate = scanner.nextDouble();
                        addService(serviceName, hourlyRate);
                        break;
                    case 7:
                        // View all services
                        viewServices();
                        break;
                    case 8:
                        // Update service information
                        System.out.print("Enter service ID: ");
                        int updateServiceId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        System.out.print("Enter updated service name: ");
                        String updatedServiceName = scanner.nextLine();
                        System.out.print("Enter updated hourly rate: ");
                        double updatedHourlyRate = scanner.nextDouble();
                        updateService(updateServiceId, updatedServiceName, updatedHourlyRate);
                        break;
                    case 9:
                        // Delete a service
                        System.out.print("Enter service ID: ");
                        int deleteServiceId = scanner.nextInt();
                        deleteService(deleteServiceId);
                        break;
                    case 10:
                        // View total hours billed for each service
                        viewTotalHoursBilled();
                        break;
                    case 11:
                        // Create a new invoice
                        createInvoice();
                        break;
                    case 12:
                        // Add service to an invoice
                        addServiceToInvoice();
                        break;
                    case 13:
                        // Update service hours in an invoice
                        updateServiceHoursInInvoice();
                        break;
                    case 14:
                        // Delete an invoice
                        deleteInvoice();
                        break;
                    case 15:
                        // View all invoices for a client
                        viewInvoicesForClient();
                        break;
                    case 16:
                        // View total amount for each invoice
                        viewTotalAmountForEachInvoice();
                        break;
                    case 17:
                        // Total income for a given period
                        totalIncomeForPeriod();
                        break;
                    case 18:
                        // Most popular service for a given period
                        mostPopularServiceForPeriod();
                        break;
                    case 19:
                        // Top client for a given period
                        topClientForPeriod();
                        break;
                    case 20:
                        // Exit
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 20.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {

            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private static void createTables() throws SQLException {

        String createClientsTableSQL = "CREATE TABLE IF NOT EXISTS clients (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "phone VARCHAR(15) NOT NULL)";


        String createServicesTableSQL = "CREATE TABLE IF NOT EXISTS services (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "hourly_rate DOUBLE NOT NULL)";

        String createInvoicesTableSQL = "CREATE TABLE IF NOT EXISTS invoices (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "client_id INT NOT NULL," +
                "invoice_date DATE NOT NULL," +
                "total_amount DOUBLE NOT NULL," +
                "FOREIGN KEY (client_id) REFERENCES clients(id))";


        String createInvoiceServicesTableSQL = "CREATE TABLE IF NOT EXISTS invoice_services (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "invoice_id INT NOT NULL," +
                "service_id INT NOT NULL," +
                "hours DOUBLE NOT NULL," +
                "FOREIGN KEY (invoice_id) REFERENCES invoices(id)," +
                "FOREIGN KEY (service_id) REFERENCES services(id))";

        statement.executeUpdate(createClientsTableSQL);
        statement.executeUpdate(createServicesTableSQL);
        statement.executeUpdate(createInvoicesTableSQL);
        statement.executeUpdate(createInvoiceServicesTableSQL);
    }

    private static void addClient(String name, String email, String phone) throws SQLException {
        String addClientSQL = "INSERT INTO clients (name, email, phone) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(addClientSQL);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);
        preparedStatement.setString(3, phone);
        preparedStatement.executeUpdate();
        System.out.println("Client added successfully.");
    }

    // Method to get client information by ID
    private static void getClient(int clientId) throws SQLException {
        // Implementation
    }

    // Method to update client information
    private static void updateClient(int clientId, String newName, String newEmail, String newPhone) throws SQLException {
        // Implementation
    }

    // Method to delete a client by ID
    private static void deleteClient(int clientId) throws SQLException {
        // Implementation
    }

    // Method to view all clients
    private static void viewClients() throws SQLException {
        // Implementation
    }

    // Method to add a new service
    private static void addService(String serviceName, double hourlyRate) throws SQLException {
        // Implementation
    }

    // Method to view all services
    private static void viewServices() throws SQLException {
        // Implementation
    }

    // Method to update service information
    private static void updateService(int serviceId, String newName, double newHourlyRate) throws SQLException {
        // Implementation
    }

    // Method to delete a service by ID
    private static void deleteService(int serviceId) throws SQLException {
        // Implementation
    }

    // Method to view total hours billed for each service
    private static void viewTotalHoursBilled() throws SQLException {
        // Implementation
    }

    // Method to create a new invoice
    private static void createInvoice() throws SQLException {
        // Implementation
    }

    // Method to add service to an invoice
    private static void addServiceToInvoice() throws SQLException {
        // Implementation
    }

    // Method to update service hours in an invoice
    private static void updateServiceHoursInInvoice() throws SQLException {
        // Implementation
    }

    // Method to delete an invoice
    private static void deleteInvoice() throws SQLException {
        // Implementation
    }

    // Method to view all invoices for a client
    private static void viewInvoicesForClient() throws SQLException {
        // Implementation
    }

    // Method to view total amount for each invoice
    private static void viewTotalAmountForEachInvoice() throws SQLException {
        // Implementation
    }

    // Method to calculate total income for a given period
    private static void totalIncomeForPeriod() throws SQLException {
        // Implementation
    }

    // Method to find the most popular service for a given period
    private static void mostPopularServiceForPeriod() throws SQLException {
        // Implementation
    }

    // Method to find the top client for a given period
    private static void topClientForPeriod() throws SQLException {
        // Implementation
    }
}