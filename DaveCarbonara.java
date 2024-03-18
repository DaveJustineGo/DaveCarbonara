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
        private static void getClient(int clientId) throws SQLException {
            String getClientSQL = "SELECT * FROM clients WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(getClientSQL);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
        
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                
                System.out.println("Client ID: " + clientId);
                System.out.println("Name: " + name);
                System.out.println("Email: " + email);
                System.out.println("Phone: " + phone);
            } else {
                System.out.println("Client with ID " + clientId + " does not exist.");
            }
        }
    }

    // Method to update client information
    private static void updateClient(int clientId, String newName, String newEmail, String newPhone) throws SQLException {
        private static void updateClient(int clientId, String newName, String newEmail, String newPhone) throws SQLException {
            String updateClientSQL = "UPDATE clients SET name = ?, email = ?, phone = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateClientSQL);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newEmail);
            preparedStatement.setString(3, newPhone);
            preparedStatement.setInt(4, clientId);
            int rowsAffected = preparedStatement.executeUpdate();
        
            if (rowsAffected > 0) {
                System.out.println("Client with ID " + clientId + " updated successfully.");
            } else {
                System.out.println("No client found with ID " + clientId + ". Update operation failed.");
            }
        }
    }

    // Method to delete a client by ID
    private static void deleteClient(int clientId) throws SQLException {
        private static void deleteClient(int clientId) throws SQLException {
            String deleteClientSQL = "DELETE FROM clients WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteClientSQL);
            preparedStatement.setInt(1, clientId);
            int rowsAffected = preparedStatement.executeUpdate();
        
            if (rowsAffected > 0) {
                System.out.println("Client with ID " + clientId + " deleted successfully.");
            } else {
                System.out.println("No client found with ID " + clientId + ". Deletion operation failed.");
            }
        }
    }

    // Method to view all clients
    private static void viewClients() throws SQLException {
        private static void viewClients() throws SQLException {
            String viewClientsSQL = "SELECT * FROM clients";
            ResultSet resultSet = statement.executeQuery(viewClientsSQL);
        
            System.out.println("List of Clients:");
            System.out.println("ID\tName\t\tEmail\t\t\tPhone");
            System.out.println("---------------------------------------------");
        
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                
                System.out.printf("%d\t%-15s%-25s%s\n", id, name, email, phone);
            }
        }
    }

    // Method to add a new service
    private static void addService(String serviceName, double hourlyRate) throws SQLException {
        private static void addService(String serviceName, double hourlyRate) throws SQLException {
            String addServiceSQL = "INSERT INTO services (name, hourly_rate) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(addServiceSQL);
            preparedStatement.setString(1, serviceName);
            preparedStatement.setDouble(2, hourlyRate);
            preparedStatement.executeUpdate();
            System.out.println("Service added successfully.");
        }
    }

    // Method to view all services
    private static void viewServices() throws SQLException {
        private static void viewServices() throws SQLException {
            String viewServicesSQL = "SELECT * FROM services";
            ResultSet resultSet = statement.executeQuery(viewServicesSQL);
        
            System.out.println("List of Services:");
            System.out.println("ID\tName\t\tHourly Rate");
            System.out.println("--------------------------------");
        
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double hourlyRate = resultSet.getDouble("hourly_rate");
                
                System.out.printf("%d\t%-15s%.2f\n", id, name, hourlyRate);
            }
        }
    }

    // Method to update service information
    private static void updateService(int serviceId, String newName, double newHourlyRate) throws SQLException {
        // Method to update service information
    private static void updateService(int serviceId, String newName, double newHourlyRate) throws SQLException {
    String updateServiceSQL = "UPDATE services SET name = ?, hourly_rate = ? WHERE id = ?";
    PreparedStatement preparedStatement = connection.prepareStatement(updateServiceSQL);
    preparedStatement.setString(1, newName);
    preparedStatement.setDouble(2, newHourlyRate);
    preparedStatement.setInt(3, serviceId);
    int rowsAffected = preparedStatement.executeUpdate();

    if (rowsAffected > 0) {
        System.out.println("Service with ID " + serviceId + " updated successfully.");
    } else {
        System.out.println("No service found with ID " + serviceId + ". Update operation failed.");
    }
}

    }

    // Method to delete a service by ID
    private static void deleteService(int serviceId) throws SQLException {
        private static void deleteService(int serviceId) throws SQLException {
            String deleteServiceSQL = "DELETE FROM services WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteServiceSQL);
            preparedStatement.setInt(1, serviceId);
            int rowsAffected = preparedStatement.executeUpdate();
        
            if (rowsAffected > 0) {
                System.out.println("Service with ID " + serviceId + " deleted successfully.");
            } else {
                System.out.println("No service found with ID " + serviceId + ". Deletion operation failed.");
            }
        }
    }

    // Method to view total hours billed for each service
    private static void viewTotalHoursBilled() throws SQLException {
        private static void viewTotalHoursBilled() throws SQLException {
            String viewTotalHoursBilledSQL = "SELECT s.name AS service_name, SUM(is.hours) AS total_hours_billed " +
                                             "FROM services s " +
                                             "INNER JOIN invoice_services is ON s.id = is.service_id " +
                                             "GROUP BY s.id";
            ResultSet resultSet = statement.executeQuery(viewTotalHoursBilledSQL);
        
            System.out.println("Total Hours Billed for Each Service:");
            System.out.println("Service Name\tTotal Hours Billed");
            System.out.println("-----------------------------------");
        
            while (resultSet.next()) {
                String serviceName = resultSet.getString("service_name");
                double totalHoursBilled = resultSet.getDouble("total_hours_billed");
                
                System.out.printf("%-15s%.2f\n", serviceName, totalHoursBilled);
            }
        }
    }

    // Method to create a new invoice
    private static void createInvoice() throws SQLException {
        private static void createInvoice() throws SQLException {
            // Get client ID for the invoice
            System.out.print("Enter client ID for the invoice: ");
            int clientId = scanner.nextInt();
            scanner.nextLine(); // Consume newline character
        
            // Get current date as the invoice date
            Date invoiceDate = Date.valueOf(LocalDate.now());
        
            // Calculate total amount for the invoice (you may implement this logic)
            double totalAmount = calculateTotalAmountForInvoice(); // Implement this method
        
            // Insert the new invoice into the database
            String createInvoiceSQL = "INSERT INTO invoices (client_id, invoice_date, total_amount) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(createInvoiceSQL);
            preparedStatement.setInt(1, clientId);
            preparedStatement.setDate(2, invoiceDate);
            preparedStatement.setDouble(3, totalAmount);
            preparedStatement.executeUpdate();
            
            System.out.println("Invoice created successfully.");
        }
    }

    // Method to add service to an invoice
    private static void addServiceToInvoice() throws SQLException {
        private static void addServiceToInvoice() throws SQLException {
            try {
                // Get invoice ID
                System.out.print("Enter invoice ID: ");
                int invoiceId = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
        
                // Get service ID
                System.out.print("Enter service ID: ");
                int serviceId = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
        
                // Get number of hours for the service
                System.out.print("Enter number of hours: ");
                double hours = scanner.nextDouble();
                scanner.nextLine(); // Consume newline character
        
                // Insert the service into the invoice
                String addServiceToInvoiceSQL = "INSERT INTO invoice_services (invoice_id, service_id, hours) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(addServiceToInvoiceSQL);
                preparedStatement.setInt(1, invoiceId);
                preparedStatement.setInt(2, serviceId);
                preparedStatement.setDouble(3, hours);
                preparedStatement.executeUpdate();
        
                System.out.println("Service added to the invoice successfully.");
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter numeric values for invoice ID, service ID, and hours.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    // Method to update service hours in an invoice
    private static void updateServiceHoursInInvoice() throws SQLException {
        private static void updateServiceHoursInInvoice() throws SQLException {
            try {
                // Get invoice service ID
                System.out.print("Enter invoice service ID: ");
                int invoiceServiceId = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
        
                // Get new number of hours for the service
                System.out.print("Enter new number of hours: ");
                double newHours = scanner.nextDouble();
                scanner.nextLine(); // Consume newline character
        
                // Update the number of hours for the service in the invoice
                String updateServiceHoursSQL = "UPDATE invoice_services SET hours = ? WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateServiceHoursSQL);
                preparedStatement.setDouble(1, newHours);
                preparedStatement.setInt(2, invoiceServiceId);
                int rowsAffected = preparedStatement.executeUpdate();
        
                if (rowsAffected > 0) {
                    System.out.println("Service hours in the invoice updated successfully.");
                } else {
                    System.out.println("No invoice service found with ID " + invoiceServiceId + ". Update operation failed.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the new number of hours.");
                scanner.nextLine(); // Consume invalid input
            }
        }
    }

    // Method to delete an invoice
    private static void deleteInvoice() throws SQLException {
        private static void deleteInvoice() throws SQLException {
            try {
                // Get invoice ID
                System.out.print("Enter invoice ID: ");
                int invoiceId = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
        
                // Check if the invoice exists
                if (!checkInvoiceExists(invoiceId)) {
                    System.out.println("Invoice with ID " + invoiceId + " does not exist.");
                    return;
                }
        
                // Delete invoice from the database
                String deleteInvoiceSQL = "DELETE FROM invoices WHERE id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteInvoiceSQL);
                preparedStatement.setInt(1, invoiceId);
                int rowsAffected = preparedStatement.executeUpdate();
        
                if (rowsAffected > 0) {
                    System.out.println("Invoice with ID " + invoiceId + " deleted successfully.");
                } else {
                    System.out.println("No invoice found with ID " + invoiceId + ". Deletion operation failed.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the invoice ID.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        
        // Method to check if an invoice exists
        private static boolean checkInvoiceExists(int invoiceId) throws SQLException {
            String checkInvoiceSQL = "SELECT id FROM invoices WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(checkInvoiceSQL);
            preparedStatement.setInt(1, invoiceId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    // Method to view all invoices for a client
    private static void viewInvoicesForClient() throws SQLException {
        private static void viewInvoicesForClient() throws SQLException {
            try {
                // Get client ID
                System.out.print("Enter client ID: ");
                int clientId = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
        
                // Check if the client exists
                if (!checkClientExists(clientId)) {
                    System.out.println("Client with ID " + clientId + " does not exist.");
                    return;
                }
        
                // Retrieve invoices for the client
                String viewInvoicesForClientSQL = "SELECT * FROM invoices WHERE client_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(viewInvoicesForClientSQL);
                preparedStatement.setInt(1, clientId);
                ResultSet resultSet = preparedStatement.executeQuery();
        
                // Display invoices
                System.out.println("Invoices for Client ID " + clientId + ":");
                System.out.println("ID\tInvoice Date\t\tTotal Amount");
                System.out.println("---------------------------------------------");
        
                while (resultSet.next()) {
                    int invoiceId = resultSet.getInt("id");
                    Date invoiceDate = resultSet.getDate("invoice_date");
                    double totalAmount = resultSet.getDouble("total_amount");
                    
                    System.out.printf("%d\t%s\t%.2f\n", invoiceId, invoiceDate.toString(), totalAmount);
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numeric value for the client ID.");
                scanner.nextLine(); // Consume invalid input
            }
        }
        
        // Method to check if a client exists
        private static boolean checkClientExists(int clientId) throws SQLException {
            String checkClientSQL = "SELECT id FROM clients WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(checkClientSQL);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    // Method to view total amount for each invoice
    private static void viewTotalAmountForEachInvoice() throws SQLException {
        private static void viewTotalAmountForEachInvoice() throws SQLException {
            String viewTotalAmountForEachInvoiceSQL = "SELECT id, total_amount FROM invoices";
            ResultSet resultSet = statement.executeQuery(viewTotalAmountForEachInvoiceSQL);
        
            System.out.println("Total Amount for Each Invoice:");
            System.out.println("Invoice ID\tTotal Amount");
            System.out.println("--------------------------------");
        
            while (resultSet.next()) {
                int invoiceId = resultSet.getInt("id");
                double totalAmount = resultSet.getDouble("total_amount");
                
                System.out.printf("%d\t\t%.2f\n", invoiceId, totalAmount);
            }
        }
    }

    // Method to calculate total income for a given period
    private static void totalIncomeForPeriod() throws SQLException {
        private static void totalIncomeForPeriod() throws SQLException {
            try {
                // Get start date for the period
                System.out.print("Enter start date (YYYY-MM-DD): ");
                String startDateString = scanner.nextLine();
                Date startDate = Date.valueOf(startDateString);
        
                // Get end date for the period
                System.out.print("Enter end date (YYYY-MM-DD): ");
                String endDateString = scanner.nextLine();
                Date endDate = Date.valueOf(endDateString);
        
                // Retrieve total income for the period
                String totalIncomeForPeriodSQL = "SELECT SUM(total_amount) AS total_income FROM invoices WHERE invoice_date BETWEEN ? AND ?";
                PreparedStatement preparedStatement = connection.prepareStatement(totalIncomeForPeriodSQL);
                preparedStatement.setDate(1, startDate);
                preparedStatement.setDate(2, endDate);
                ResultSet resultSet = preparedStatement.executeQuery();
        
                // Display total income for the period
                if (resultSet.next()) {
                    double totalIncome = resultSet.getDouble("total_income");
                    System.out.printf("Total income for the period from %s to %s: %.2f\n", startDateString, endDateString, totalIncome);
                } else {
                    System.out.println("No invoices found for the specified period.");
                }
            } catch (InputMismatchException | IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use the format YYYY-MM-DD.");
            }
        }
    }

    // Method to find the most popular service for a given period
    private static void mostPopularServiceForPeriod() throws SQLException {
        private static void mostPopularServiceForPeriod() throws SQLException {
            try {
                // Get start date for the period
                System.out.print("Enter start date (YYYY-MM-DD): ");
                String startDateString = scanner.nextLine();
                Date startDate = Date.valueOf(startDateString);
        
                // Get end date for the period
                System.out.print("Enter end date (YYYY-MM-DD): ");
                String endDateString = scanner.nextLine();
                Date endDate = Date.valueOf(endDateString);
        
                // Retrieve most popular service for the period
                String mostPopularServiceForPeriodSQL = "SELECT s.name AS service_name, COUNT(is.service_id) AS service_count " +
                                                         "FROM services s " +
                                                         "INNER JOIN invoice_services is ON s.id = is.service_id " +
                                                         "INNER JOIN invoices i ON is.invoice_id = i.id " +
                                                         "WHERE i.invoice_date BETWEEN ? AND ? " +
                                                         "GROUP BY s.name " +
                                                         "ORDER BY service_count DESC " +
                                                         "LIMIT 1";
                PreparedStatement preparedStatement = connection.prepareStatement(mostPopularServiceForPeriodSQL);
                preparedStatement.setDate(1, startDate);
                preparedStatement.setDate(2, endDate);
                ResultSet resultSet = preparedStatement.executeQuery();
        
                // Display most popular service for the period
                if (resultSet.next()) {
                    String mostPopularService = resultSet.getString("service_name");
                    int serviceCount = resultSet.getInt("service_count");
                    System.out.printf("Most popular service for the period from %s to %s: %s (Used %d times)\n", startDateString, endDateString, mostPopularService, serviceCount);
                } else {
                    System.out.println("No invoices found for the specified period.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use the format YYYY-MM-DD.");
            }
        }
    }

    // Method to find the top client for a given period
    private static void topClientForPeriod() throws SQLException {
        private static void topClientForPeriod() throws SQLException {
            try {
                // Get start date for the period
                System.out.print("Enter start date (YYYY-MM-DD): ");
                String startDateString = scanner.nextLine();
                Date startDate = Date.valueOf(startDateString);
        
                // Get end date for the period
                System.out.print("Enter end date (YYYY-MM-DD): ");
                String endDateString = scanner.nextLine();
                Date endDate = Date.valueOf(endDateString);
        
                // Retrieve top client for the period
                String topClientForPeriodSQL = "SELECT c.name AS client_name, COUNT(i.client_id) AS invoice_count " +
                                                "FROM clients c " +
                                                "INNER JOIN invoices i ON c.id = i.client_id " +
                                                "WHERE i.invoice_date BETWEEN ? AND ? " +
                                                "GROUP BY c.name " +
                                                "ORDER BY invoice_count DESC " +
                                                "LIMIT 1";
                PreparedStatement preparedStatement = connection.prepareStatement(topClientForPeriodSQL);
                preparedStatement.setDate(1, startDate);
                preparedStatement.setDate(2, endDate);
                ResultSet resultSet = preparedStatement.executeQuery();
        
                // Display top client for the period
                if (resultSet.next()) {
                    String topClient = resultSet.getString("client_name");
                    int invoiceCount = resultSet.getInt("invoice_count");
                    System.out.printf("Top client for the period from %s to %s: %s (Total invoices: %d)\n", startDateString, endDateString, topClient, invoiceCount);
                } else {
                    System.out.println("No invoices found for the specified period.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date format. Please use the format YYYY-MM-DD.");
            }
        }
    }
}
