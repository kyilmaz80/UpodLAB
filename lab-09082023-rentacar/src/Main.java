public class Main {
    public static void main(String[] args) {
        Service service = new Service();
        //service.generateVehiclesAndCustomers();
        // debug ile araya girmek için ekledik.
        int customerId = 3;
        int vehicleId = 2;
        int day = 5;
        service.rentVehicle(vehicleId, customerId, day);

    }
}