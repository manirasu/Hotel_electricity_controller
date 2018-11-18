package controller;

import corridor.MainCorridor;
import corridor.SubCorridor;
import equipment.Equipment;
import equipment.EquipmentType;
import hotel.Floor;
import hotel.Hotel;
import org.junit.Test;
import sensor.MovementSensor;
import sensor.Sensor;

import static org.junit.Assert.assertTrue;

public class ControllerTest {
    @Test
    public void testCompensatePower() {
        Hotel hotel = new Hotel("RM");

        Controller controller = new Controller();
        hotel.setController(controller);

        Floor floor = new Floor(1);

        MainCorridor mainCorridor = new MainCorridor(1);

        Equipment mL = new Equipment(EquipmentType.LIGHT,5);
        Equipment mAC = new Equipment(EquipmentType.AC,10);

        mainCorridor.addEquipment(mL);
        mainCorridor.addEquipment(mAC);

        SubCorridor subCorridor = new SubCorridor(1);

        Equipment sL = new Equipment(EquipmentType.LIGHT,5);
        Equipment sAC = new Equipment(EquipmentType.AC,10);

        subCorridor.addEquipment(sL);
        subCorridor.addEquipment(sAC);

        SubCorridor subCorridor1 = new SubCorridor(2);
        Equipment sL1 = new Equipment(EquipmentType.LIGHT,5);
        Equipment sAC1 = new Equipment(EquipmentType.AC,10);

        subCorridor1.addEquipment(sL1);
        subCorridor1.addEquipment(sAC1);

        floor.addCorridors(subCorridor1);

        floor.addCorridors(mainCorridor);
        floor.addCorridors(subCorridor);

        hotel.addFloor(floor);

        Sensor sensor = new MovementSensor(floor,subCorridor);
        subCorridor.setSensor(sensor);

        controller.initializeController(hotel);
        subCorridor.getSensor().eventDetected(false);
        assertTrue("true",floor.getPowerConsumption() <= floor.getMaxPowerConsumption());
        controller.unregisterFromSensorEvents(hotel);
    }
}
