package hotel;

import controller.Controller;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HotelTest {
    @Test

    public void testCreateHotel() {
        Hotel hotel = new Hotel("RM");
        assertEquals("pass",hotel.getHotelName(),"RM");
    }
    @Test
    public void testHotelWithController() {
        Hotel hotel = new Hotel("RM");
        Controller controller = new Controller();
        hotel.setController(controller);
        assertNotNull(hotel.getController());

    }

    @Test
    public void testHotelWithFloor() {
        Hotel hotel = new Hotel("RM");
        Controller controller = new Controller();
        hotel.setController(controller);
        Floor floor = new Floor(1);
        floor.setNumber(1);
        hotel.addFloor(floor);
        assertEquals("true",hotel.getFloors().size(),1);
        assertEquals("true",hotel.getFloorByNumber(1).get().getNumber(),1);
    }
}
