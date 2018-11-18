package corridor;

import equipment.Equipment;
import equipment.EquipmentType;
import hotel.Floor;
import org.junit.Test;
import sensor.MovementSensor;
import sensor.Sensor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CorridorTest {
    @Test
    public void testCreateMainCorridorWithLight(){
        MainCorridor mc = new MainCorridor(1);
        Equipment light = new Equipment(EquipmentType.LIGHT, 5);
        mc.addEquipment(light);

        assertEquals("true", mc.getEquipments().get(0).getEquipmentType(), EquipmentType.LIGHT);
    }

    @Test
    public void testCreateMainCorridorWithAC(){
        MainCorridor mc = new MainCorridor(1);
        Equipment light = new Equipment(EquipmentType.AC, 10);
        mc.addEquipment(light);

        assertEquals("true", mc.getEquipments().get(0).getEquipmentType(), EquipmentType.AC);
    }

    @Test
    public void testCreateSubCorridorWithLight(){
        SubCorridor sc = new SubCorridor(1);
        Equipment light = new Equipment(EquipmentType.LIGHT, 5);
        sc.addEquipment(light);

        assertEquals("true", sc.getEquipments().get(0).getEquipmentType(), EquipmentType.LIGHT);
    }

    @Test
    public void testCreateSubCorridorWithAC(){
        SubCorridor sc = new SubCorridor(1);
        Equipment light = new Equipment(EquipmentType.AC, 10);
        sc.addEquipment(light);

        assertEquals("true", sc.getEquipments().get(0).getEquipmentType(), EquipmentType.AC);
    }

    @Test
    public void testMainCorridorPowerConsumption(){
        MainCorridor mc = new MainCorridor(1);

        Equipment light = new Equipment(EquipmentType.LIGHT, 5);
        light.switchOff();
        Equipment ac = new Equipment(EquipmentType.AC, 10);
        ac.switchOn();

        mc.addEquipment(light);
        mc.addEquipment(ac);

        assertEquals("true", mc.getPowerConsumption(), 10);
    }

    @Test
    public void testSubCorridorPowerConsumption(){
        SubCorridor sc = new SubCorridor(1);

        Equipment light = new Equipment(EquipmentType.LIGHT, 5);
        light.switchOn();
        Equipment ac = new Equipment(EquipmentType.AC, 10);
        ac.switchOff();

        sc.addEquipment(light);
        sc.addEquipment(ac);

        assertEquals("true", sc.getPowerConsumption(), 5);
    }

    @Test
    public void testCreateCorridorWithSensor(){
        Floor floor = new Floor(2);
        SubCorridor sc = new SubCorridor(2);
        floor.addCorridors(sc);
        Sensor sensor = new MovementSensor(floor, sc);

        assertTrue(sensor.getId().startsWith("2-SC2"));
    }


}
