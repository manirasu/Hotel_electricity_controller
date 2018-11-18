package corridor;

import equipment.Equipment;
import equipment.EquipmentType;
import sensor.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public abstract class Corridor {
    private String id;
    protected List<Equipment> equipments = new ArrayList<Equipment>();

    //This trail is necessary for reverting back the compensation done in case of no movement
    private Stack<Equipment> stateChangeTrail = new Stack<>();

    private boolean movementDetected;

    private Sensor sensor;

    Corridor(String id){
        this.id = id;
    }

    public abstract void setDefaultState();

    public String getId() {
        return id;
    }

    public List<Equipment> getEquipments() {
       // return new ArrayList<>(equipments);
        return equipments;
    }

    public void addEquipment( Equipment equipment){
        this.equipments.add(equipment);
    }

    public Stack<Equipment> getStateChangeTrail() {
        return stateChangeTrail;
    }

    public void setSensor( Sensor sensor){
        this.sensor = sensor;
    }

    public Sensor getSensor(){
        return this.sensor;
    }

    public boolean isMovementDetected() {
        return movementDetected;
    }

    public void setMovementDetected(boolean movementDetected) {
        this.movementDetected = movementDetected;
    }

    public void printCorridorInfo(){
        List<Equipment> lights = equipments.stream().filter(equipment -> equipment.getEquipmentType() == EquipmentType.LIGHT).collect(Collectors.toList());
        List<Equipment> acs = equipments.stream().filter(equipment -> equipment.getEquipmentType() == EquipmentType.AC).collect(Collectors.toList());

        lights.forEach( light -> {
            System.out.print(" "+light.getEquipmentType()+" "+ (lights.indexOf(light)+1)+" : "+ light.getEquipmentState());
        });
        acs.forEach( ac -> {
            System.out.print(" "+ac.getEquipmentType()+" "+ (acs.indexOf(ac)+1)+" : "+ ac.getEquipmentState());
        });
    }

    /**
     * Get power consumption of the corridor by summing up the power units consumed by each active equipment
     * @return totalPower
     */

    public int getPowerConsumption(){
        int unit =0;
        if(equipments != null) {
            for(Equipment equipment : equipments) {
                if(equipment.isOn()) {
                    unit += equipment.getPowerUnits();
                }
            }
        }
      return unit;
    }


    /*public int getPowerConsumption(){
        return equipments.stream().filter(equipment -> equipment.isOn())
                .map(equipment -> equipment.getPowerUnits())
                .reduce(0, (a,b) -> a + b);
    }*/
}

