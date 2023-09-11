import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

public class Service implements Serializable {

    public static int a = 5;
    public  final Vehicle[] VEHICLE_LIST = new Vehicle[9];
    public  final Customer[] CUSTOMER_LIST = new Customer[3];

    private static Connection conn;
    private static Statement stmt;
    private static PreparedStatement ps;

    private static ResultSet rs;

    public  void generateVehiclesAndCustomers() {
        createVehicleListTables();
        insertVehicles();
//        VEHICLE_LIST[0] = new Car(2020, "Mercedes C-200", BigDecimal.valueOf(300), true);
//        VEHICLE_LIST[1] = new Car(2018, "Toyota Corolla", BigDecimal.valueOf(250), true);
//        VEHICLE_LIST[2] = new Car(2019, "Egea Cross", BigDecimal.valueOf(200), false);
//        VEHICLE_LIST[3] = new Car(2019, "Clio", BigDecimal.valueOf(150), false);
//        VEHICLE_LIST[4] = new Truck(2022, "Kamaz", BigDecimal.valueOf(5000));
//        VEHICLE_LIST[5] = new Truck(2022, "Mercedes UNIMOG", BigDecimal.valueOf(6000));
//        VEHICLE_LIST[6] = new Truck(2022, "Ford Cargo", BigDecimal.valueOf(4000));
//        VEHICLE_LIST[7] = new Van(2023, "Mercedes Benz Vito", BigDecimal.valueOf(500));
//        VEHICLE_LIST[8] = new Van(2022, "Mercedes Benz Vito", BigDecimal.valueOf(400));
        //serializeObjects(VEHICLE_LIST);

//        CUSTOMER_LIST[0] = new Customer("Ahmet", "Gültekin", 7);
//        CUSTOMER_LIST[1] = new Customer("Asım", "Kılıç", 1);
//        CUSTOMER_LIST[2] = new Customer("Emre", "Yılmaz", 2);
    }

    private static void createVehicleListTables() {
        conn = JDBCUtil.getConnection();
        String createString1 = "CREATE TABLE IF NOT EXISTS Vehicles " + " (vehicle_id INT NOT NULL AUTO_INCREMENT, " +
                "vehicle_type ENUM('Car', 'Van', 'Truck') NOT NULL,  year INT, brand VARCHAR(32), price_per_day INT, " +
                "has_child_seat INT, " +
                "PRIMARY KEY (vehicle_id)" +
                ")";

        String createString2 = "CREATE TABLE IF NOT EXISTS Customer " + "(customer_id INT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(48), surname VARCHAR(48), licence_year INT, " +
                "PRIMARY KEY(customer_id)" +
                ")";

        /*
        String createString1 = "CREATE TABLE IF NOT EXISTS Car " + "(vehicle_id  INT NOT NULL AUTO_INCREMENT, year INT, " +
                "brand VARCHAR(32), price_per_day INT, has_child_seat INT, " +
                "PRIMARY KEY (vehicle_id)" +
                ")";

        String createString2 = "CREATE TABLE IF NOT EXISTS Truck " + "(vehicle_id  INT NOT NULL AUTO_INCREMENT, year INT, " +
                "brand VARCHAR(32), price_per_day INT, " +
                "PRIMARY KEY (vehicle_id)" +
                ")";

        String createString3 = "CREATE TABLE IF NOT EXISTS Van " + "(vehicle_id  INT NOT NULL AUTO_INCREMENT, year INT, " +
                "brand VARCHAR(32), price_per_day INT, " +
                "PRIMARY KEY (vehicle_id)" +
                ")";

        String createString4 = "CREATE TABLE IF NOT EXISTS Customer " + "(customer_id INT NOT NULL AUTO_INCREMENT, " +
                "name VARCHAR(48), surname VARCHAR(48), licence_year INT, " +
                "PRIMARY KEY(customer_id)" +
                ")";
        */
        String[] createStrings = {createString1, createString2};
        for (String createString: createStrings) {
            System.out.println(createString);
            try {
                stmt = conn.createStatement();
                int rowCount = stmt.executeUpdate(createString);
                System.out.println("Row count: " + rowCount);
                stmt.close();

            } catch (SQLException ex) {
                System.err.println("SQLException: " + ex.getMessage());
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertVehicles() {
        conn = JDBCUtil.getConnection();

        try {
            stmt = conn.createStatement();
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Car', 2020, 'Mercedes C-200', 300, 1)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Car', 2018, 'Toyota Corolla', 300, 1)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Car', 2019, 'Egea Cross', 200, 0)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Car', 2019, 'Clio', 150, 0)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Truck', 2022, 'Kamaz', 5000, 0)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Truck', 2022, 'Mercedes UNIMOG', 6000, 0)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Truck', 2022, 'Ford Cargo', 4000, 0)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Van', 2023, 'Mercedes Benz Vito', 500, 0)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Vehicles " + "VALUES (0, 'Van', 2022, 'Mercedes Benz Vito', 400, 0)"));

            System.out.println(stmt.executeUpdate("INSERT INTO Customer " + "VALUES (0, 'Ahmet', 'Gültekin', 7)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Customer " + "VALUES (0, 'Asım', 'Kılıç', 1)"));
            System.out.println(stmt.executeUpdate("INSERT INTO Customer " + "VALUES (0, 'Emre', 'Yılmaz', 2)"));

            stmt.close();
        } catch (SQLException e) {
            System.err.println("Problem with inserting Vehicles or Customers. " + e.getMessage());
            System.err.println("Error code: " + e.getErrorCode());
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Vehicle getVehicleByIdSQL(int id) {
        conn = JDBCUtil.getConnection();

        Vehicle v = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM  Vehicles WHERE vehicle_id = ? ");
            ps.setInt(1, id);
            System.out.println(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                int year = rs.getInt("year");
                String vehicleTypeStr = rs.getString("vehicle_type");
                VehicleType vehicleType = VehicleType.fromString(vehicleTypeStr);
                String brand = rs.getString("brand");
                BigDecimal pricePerDay = rs.getBigDecimal("price_per_day");
                Boolean hasChildSeat = rs.getBoolean("has_child_seat");

                if (vehicleType == VehicleType.CAR) {
                    v = new Car(year, brand, pricePerDay, hasChildSeat);
                } else if (vehicleType == VehicleType.VAN) {
                    v = new Van(year, brand, pricePerDay);
                } else if (vehicleType == VehicleType.TRUCK) {
                    v = new Truck(year, brand, pricePerDay);
                } else {
                    throw new IllegalArgumentException();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return v;
    }

    private Customer getCustomerByIdSQL(int id) {
        conn = JDBCUtil.getConnection();
        Customer c = null;

        try {
            ps = conn.prepareStatement("SELECT * FROM  Customer WHERE customer_id = ? ");
            ps.setInt(1, id);
            System.out.println(ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                int licenceYear = rs.getInt("licence_year");
                c = new Customer(name, surname, licenceYear);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return c;
    }


    public void serializeObjects(Object[] object) {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(object[0].getClass().getName() + ".txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            objectOutputStream.close();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void rentVehicle(String vehicleId, String customerId, int countOfDay) {
        Vehicle vehicle = getVehicle(vehicleId);
        Customer customer = getCustomer(customerId);

        if (vehicle != null && customer != null) {
            vehicle.rent(customer, countOfDay);
        } else {
            System.out.println("Hatalı bilgi girişi");
        }
    }

    public void rentVehicle(int vehicleId, int customerId, int countOfDay) {
        //Vehicle vehicle = getVehicle(vehicleId);
        //Customer customer = getCustomer(customerId);
        Vehicle vehicle = getVehicleByIdSQL(vehicleId);
        Customer customer = getCustomerByIdSQL(customerId);


        if (vehicle != null && customer != null) {
            vehicle.rent(customer, countOfDay);
        } else {
            System.out.println("Hatalı bilgi girişi");
        }
    }

    public  void returnVehicle(String vehicleId) {
        Vehicle vehicle = getVehicle(vehicleId);
        if (vehicle != null) {
            vehicle.returnVehicle();
        }
    }

    private  Vehicle getVehicle(String vehicleId) {
        for (Vehicle vehicle : VEHICLE_LIST) {
            if (vehicle.getVehicleId().equalsIgnoreCase(vehicleId)) {
                return vehicle;
            }
        }
        return null;
    }

    private  Customer getCustomer(String customerId) {
        for (Customer customer : CUSTOMER_LIST) {
            if (customer.getCustomerId().equalsIgnoreCase(customerId)) {
                return customer;
            }
        }
        return null;
    }

    public  Vehicle[] getVehicles() {
        return VEHICLE_LIST;
    }

    public Customer[] getCustomers() {
        return CUSTOMER_LIST;
    }

    public  Vehicle getVehicle(int id) {
        return null;
        /*
        if (id >= VEHICLE_LIST.length) {
            return null;
        }
        return VEHICLE_LIST[id];

         */
    }

    public  Customer getCustomer(int id) {
        if (id >= CUSTOMER_LIST.length) {
            return null;
        }
        return CUSTOMER_LIST[id];
    }
}
