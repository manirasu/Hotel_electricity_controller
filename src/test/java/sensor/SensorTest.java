package sensor;

import controller.Controller;
import corridor.MainCorridor;
import corridor.SubCorridor;
import equipment.Equipment;
import equipment.EquipmentType;
import hotel.Floor;
import hotel.Hotel;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class SensorTest {
    @Test
    public void testMovementDetectionOnSubCorridor() {
        Hotel hotel = new Hotel("RM");
        Controller controller = new Controller();
        hotel.setController(controller);

        Floor floor = new Floor(1);
        SubCorridor subCorridor = new SubCorridor(1);
        SubCorridor subCorridor1 = new SubCorridor(2);

        Equipment light = new Equipment(EquipmentType.LIGHT,5);
        Equipment ac = new Equipment(EquipmentType.AC,10);

        subCorridor.addEquipment(light);
        subCorridor.addEquipment(ac);

        subCorridor1.addEquipment(light);
        subCorridor1.addEquipment(ac);

        floor.addCorridors(subCorridor);
        floor.addCorridors(subCorridor1);
        hotel.addFloor(floor);

        Sensor sensor = new MovementSensor(floor,subCorridor);
        subCorridor.setSensor(sensor);

        controller.initializeController(hotel);

        subCorridor.getSensor().eventDetected(true);
        assertTrue(subCorridor.isMovementDetected());
        controller.unregisterFromSensorEvents(hotel);
    }
    @Test
    public void testLightOffDuringOnMovement() {
        Hotel hotel = new Hotel("RM");
        Controller controller = new Controller();
        hotel.setController(controller);

        Floor floor = new Floor(1);

        MainCorridor mainCorridor = new MainCorridor(1);

        Equipment mL = new Equipment(EquipmentType.LIGHT ,5);
        Equipment mAC = new Equipment(EquipmentType.AC,10);
        mainCorridor.addEquipment(mL);
        mainCorridor.addEquipment(mAC);

        floor.addCorridors(mainCorridor);

        SubCorridor subCorridor1 = new SubCorridor(1);

        Equipment sL = new Equipment(EquipmentType.LIGHT,5);
        Equipment sAC = new Equipment(EquipmentType.AC,10);

        subCorridor1.addEquipment(sL);
        subCorridor1.addEquipment(sAC);

        floor.addCorridors(subCorridor1);

        Sensor sensor = new MovementSensor(floor,subCorridor1);
        subCorridor1.setSensor(sensor);

        hotel.addFloor(floor);
        controller.initializeController(hotel);

        subCorridor1.getSensor().eventDetected(false);
        assertTrue(sL.isOff());
        controller.unregisterFromSensorEvents(hotel);
    }
}
