import java.io.Serializable;

enum RoomType implements Serializable {
    SINGLE("Single Room", "Single bed, bathroom, TV, closet", 20.0),
    DOUBLE("Double Room", "Double bed, bathroom, TV, closet", 35.0),
    DELUXE("Deluxe Room", "Minibar, bathtub, king-size bed, sitting area", 55.0);

    private final String description;
    private final String facilities;
    private final double pricePerDay;

    RoomType(String description, String facilities, double pricePerDay) {
        this.description = description;
        this.facilities = facilities;
        this.pricePerDay = pricePerDay;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFacilities() {
        return facilities;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

}

