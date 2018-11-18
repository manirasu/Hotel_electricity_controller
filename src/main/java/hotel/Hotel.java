package hotel;

import controller.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Hotel {

    private String hotelName ;
    private Controller controller;
    private List<Floor> floors = new ArrayList<Floor>();

    public Hotel () {}

    public Hotel(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getHotelName() {
        return hotelName;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void addFloor( Floor floor ){
        this.floors.add(floor);
    }

    public Optional<Floor> getFloorByNumber(int number){
        Optional<Floor> floorOptional = floors.stream()
                .filter(floor -> floor.getNumber() == number)
                .findFirst();

        return floorOptional;
    }

}
