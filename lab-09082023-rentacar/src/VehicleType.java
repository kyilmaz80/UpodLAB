public enum VehicleType {
    TRUCK(5,"Bu aracı kiralamak için minimum ehliyet yaşı 5'dir."),
    VAN(4,"Bu aracı kiralamak için minimum ehliyet yaşı 4'dür."),
    CAR(2,"Bu aracı kiralamak için minimum ehliyet yaşı 2'dir.");

    private final int minLicenceYear;
    private final String errorMessage;

    VehicleType(int minLicenceYear, String errorMessage) {
        this.minLicenceYear = minLicenceYear;
        this.errorMessage = errorMessage;
    }

    public int getMinLicenceYear() {
        return minLicenceYear;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static VehicleType fromString(String type) {
        if (type.equalsIgnoreCase("Car")) {
            return VehicleType.CAR;
        } else if (type.equalsIgnoreCase("Van")) {
            return VehicleType.VAN;
        } else if (type.equalsIgnoreCase("Truck")) {
            return VehicleType.TRUCK;
        } else {
            return null;
        }
    }
}
