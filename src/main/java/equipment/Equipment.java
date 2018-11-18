package equipment;

public class Equipment implements Comparable<Equipment>{
    private EquipmentType equipmentType;

    private EquipmentState equipmentState = EquipmentState.OFF;

    private int powerUnits;

    private Priority priority = Priority.LOW;

    public Equipment(EquipmentType equipmentType, int powerUnits){
        this.equipmentType = equipmentType;
        this.powerUnits = powerUnits;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public boolean isOn(){
        return this.equipmentState == EquipmentState.ON;
    }

    public boolean isOff(){
        return this.equipmentState == EquipmentState.OFF;
    }

    public EquipmentState getEquipmentState() {
        return equipmentState;
    }

    public int getPowerUnits() {
        return powerUnits;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void switchOn(){
        this.equipmentState = EquipmentState.ON;
    }

    public void switchOff(){
        this.equipmentState = EquipmentState.OFF;
    }

    @Override
    public int compareTo(Equipment equipment) {
        return equipment.getPriority().getPriorityNumber() - this.getPriority().getPriorityNumber();
    }
}
