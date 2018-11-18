package equipment;

import controller.Controller;
import corridor.MainCorridor;
import hotel.Floor;
import hotel.Hotel;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EquipmentTest {
    @Test
    public void testEquipmentCreate() {
        Equipment equipment = new Equipment(EquipmentType.LIGHT,5);
        assertNotNull(equipment);
    }

    @Test
    public void testEquipmentActionStateOn() {
        Equipment light = new Equipment(EquipmentType.LIGHT,5);
        light.switchOn();
        assertEquals("true",light.getEquipmentState() ,EquipmentState.ON);

        Equipment ac = new Equipment(EquipmentType.AC,10);
        ac.switchOn();
        assertEquals("true",ac.getEquipmentState(),EquipmentState.ON);

    }

    @Test
    public void testEquipmentActionStateOff() {
        Equipment light = new Equipment(EquipmentType.LIGHT,5);
        light.switchOff();
        assertEquals("true",light.getEquipmentState(),EquipmentState.OFF);

        Equipment ac = new Equipment(EquipmentType.AC,10);
        ac.switchOff();
        assertEquals("true",ac.getEquipmentState(),EquipmentState.OFF);
    }

    @Test
    public void testEquipmentType() {
        Equipment light = new Equipment(EquipmentType.LIGHT,5);
        assertEquals("true",light.getEquipmentType(),EquipmentType.LIGHT);

        Equipment ac = new Equipment(EquipmentType.AC,10);
        assertEquals("true",ac.getEquipmentType(),EquipmentType.AC);
    }

    @Test
    public void mainCorridorEquipmentInitialState() {
        Hotel hotel = new Hotel("RM");
        Controller controller = new Controller();
        hotel.setController(controller);

        Floor floor = new Floor(1);

        MainCorridor mainCorridor= new MainCorridor(1);

        Equipment light = new Equipment(EquipmentType.LIGHT,5);

        Equipment ac = new Equipment(EquipmentType.AC,10);

        mainCorridor.addEquipment(light);
        mainCorridor.addEquipment(ac);

        floor.addCorridors(mainCorridor);
        hotel.addFloor(floor);

        controller.initializeController(hotel);

        assertTrue(light.isOn() && ac.isOn());
        controller.unregisterFromSensorEvents(hotel);

    }


}
