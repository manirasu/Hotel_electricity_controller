package hotel;

import corridor.Corridor;
import corridor.MainCorridor;
import corridor.SubCorridor;
import equipment.Equipment;
import equipment.EquipmentType;
import org.junit.Test;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FloorTest {
    @Test
    public void testCreateFloor () {
        Floor floor = new Floor(2);
        // assertEquals("false", floor.getNumber(), 3);
        assertEquals("true",floor.getNumber(),2);

    }

    @Test
    public void testCreateFloorWithMainCorridor() {
        Floor floor = new Floor(1);
        MainCorridor mainCorridor = new MainCorridor(5);
        floor.addCorridors(mainCorridor);
        //assertEquals("true",floor.getMainCorridorsSize(),1);
        // assertEquals("true",floor.getMaxPowerConsumption(),15);
        assertEquals("true",floor.getNumber(),1);

    }
    @Test
    public void testCreateFloorWithSubCorridor() {
        Floor floor = new Floor(1);
        SubCorridor subCorridor = new SubCorridor(1);
        floor.addCorridors(subCorridor);
        assertEquals("true",floor.getSubCorridorsSize(),1);
        assertEquals("true",floor.getMaxPowerConsumption(),10);
    }
    @Test
    public void testCreateFloorWithMainCorridorAndSubCorridor() {
        Floor floor = new Floor(1);
        List<Corridor> corridorList = new ArrayList<Corridor>();
        MainCorridor mainCorridor = new MainCorridor(1);
        floor.addCorridors(mainCorridor);
        SubCorridor subCorridor = new SubCorridor(1);
        floor.addCorridors(subCorridor);
        corridorList.add(mainCorridor);
        corridorList.add(subCorridor);

       long count = corridorList.stream().filter(corr -> corr instanceof MainCorridor).count();
       int count1 = 0;
       for(Corridor c: corridorList) {
           if(c instanceof MainCorridor) {
               count1 ++;
           }
       }

       System.out.println(count);

        //assertEquals("pass",floor.getMaxPowerConsumption(),25);
    }

    @Test
    public void testPowerConsumptionOnFloor() {
        Floor floor = new Floor(1);
        MainCorridor mainCorridor = new MainCorridor(1);
        Equipment mainLight = new Equipment(EquipmentType.LIGHT ,5);
        mainLight.switchOn();
        mainCorridor.addEquipment(mainLight);

        Equipment mainAC = new Equipment(EquipmentType.AC,10);
        mainAC.switchOn();
        mainCorridor.addEquipment(mainAC);

        floor.addCorridors(mainCorridor);
        SubCorridor subCorridor = new SubCorridor(1);
        Equipment subLight = new Equipment(EquipmentType.LIGHT,5);
        mainAC.switchOff();
        subCorridor.addEquipment(subLight);

        Equipment subAC = new Equipment(EquipmentType.AC,10);
        subAC.switchOn();
        subCorridor.addEquipment(subAC);

        floor.addCorridors(subCorridor);

        assertEquals("pass",floor.getMaxPowerConsumption(),25);
    }

}
