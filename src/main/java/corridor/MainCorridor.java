package corridor;

import equipment.EquipmentType;
import equipment.Priority;

public class MainCorridor extends Corridor {

    public MainCorridor(int id){
        super("MC"+id);
    }

    /**
     * Sets the default state of equipments.
     */
    @Override
    public void setDefaultState() {
        //By default, all ACs are switched ON, all the time
        getEquipments().stream()
                .filter(equipment -> equipment.getEquipmentType() == EquipmentType.AC)
                .forEach(equipment -> equipment.switchOn());

        setNightModeOn();
    }

    /**
     * Sets the priority of equipments in NIGHT mode
     */
    private void setNightModeOn(){
        //All the lights need to be switched on during night mode in main corridor
        getEquipments().stream()
                .filter(equipment -> equipment.getEquipmentType() == EquipmentType.LIGHT)
                .forEach(equipment -> {
                    equipment.switchOn();
                    equipment.setPriority(Priority.HIGH);
                });
    }
}
