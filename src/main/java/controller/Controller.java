package controller;

import corridor.Corridor;
import corridor.MainCorridor;
import equipment.Equipment;
import equipment.Priority;
import hotel.Floor;
import hotel.Hotel;
import listener.EventListener;

import java.util.*;

public class Controller implements EventListener {

    private Hotel hotel;

    public Controller() {}

    public void initializeController(Hotel hotel){
        this.hotel = hotel;
        registerToSensorEvents();
        initializeDefaultState();
    }

    public void initializeDefaultState(){
        hotel.getFloors().forEach( floor -> floor.setDefaultState());
    }

    /**
     * Registers to the sensors of the corridors of all the floors of the hotel
     * @param hotel
     */

    public void registerToSensorEvents(Hotel hotel) {
        List<Corridor> corridors = getCorridorFromFloor(hotel);
        if(corridors != null) {
            for(Corridor corridor : corridors) {
                if(corridor.getSensor() != null) {
                    corridor.getSensor().registerListener(this);
                }
            }
        }
    }

    /**
     * Retrieves the corridor information
     * @param hotel
     * @return list of corridors
     */

    private List<Corridor> getCorridorFromFloor(Hotel hotel) {
        List<Corridor> corridors = new ArrayList<>();
        List<Floor> floors = hotel.getFloors();
        if (floors != null) {
            for (Floor floor : floors) {
                corridors.addAll(floor.getCorridors());
            }
        }
        return corridors;
    }
    /**
     * UnRegisters to the sensors of the corridors of all the floors of the hotel
     * @param hotel
     */

    public void unregisterFromSensorEvents(Hotel hotel){
        List<Corridor> corridors = getCorridorFromFloor(hotel);
        if(corridors != null) {
            for(Corridor corridor : corridors) {
                if(corridor.getSensor() != null) {
                    corridor.getSensor().unregisterListener();
                }
            }
        }
    }


    public void registerToSensorEvents(){
        registerToSensorEvents(hotel);
    }

    public void unregisterFromSensorEvents(){
        unregisterFromSensorEvents(hotel);
    }

    @Override
    public void onEventDetected(String sensorId, boolean detected) {
        if(detected) {
            handleMovement(sensorId);
        } else {
            handleNoMovement(sensorId);
        }
    }

     /**
     * Retrieves the floor and corridor information from the input sensor id
     * and further processes the movement action.
     * @param sensorId
     */
    private void handleMovement( String sensorId ){
        try {
            Floor floor = getFloorFromSensorId(sensorId);
            Corridor corridor = getCorridorFromSensorId(sensorId, floor);

            System.out.println("Movement detected at Floor number "
                    + floor.getNumber() +", Corridor number "+ corridor.getId());

            corridor.setMovementDetected(true);
            controlEquipmentsOnMovement(floor, corridor);

            printFloorsInfo();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Controls the state of equipments of the given corridor in which the movement was detected
     * @param floor
     * @param corridor
     * @throws Exception when the power consumption is too high to be compensated.
     */

    private void controlEquipmentsOnMovement(Floor floor, Corridor corridor) throws Exception {
        List<Equipment> equipments = corridor.getEquipments();
        if(equipments != null) {
            for(Equipment equipment : equipments ) {
                if(equipment.getPriority() == Priority.HIGH) {
                    corridor.getStateChangeTrail().push(equipment);
                    equipment.switchOn();
                    System.out.println("Switching on "+equipment.getEquipmentType()+" of Floor "+ floor.getNumber()
                            +", Corridor "+ corridor.getId());
                }
            }
            long difference = getPowerDifference(floor);
            if(difference > 0){
                compensatePowerConsumption(floor, corridor, difference);
            }
        }
    }


    /**
     * If the power consumption of the floor exceeds it maximum value, the state of equipments in
     * other corridors of the same floor are handled accordingly.
     * @param floor
     * @param corridor
     * @param power
     * @throws Exception when the power consumption is too high to be compensated
     */
    private void compensatePowerConsumption(Floor floor, Corridor corridor, long power) throws Exception {
        List<Corridor> corridors = floor.getCorridors();
        // Consider only sub corridors for compensation in night mode
        corridors.removeIf(corr -> corr instanceof MainCorridor);
        if(corridors.size() > 1) {
            // Do not consider the corridor in which movement was detected
            corridors.remove(corridor);
        }

        // Cannot switch off lights for compensation in NIGHT mode
        int numCorridors = corridors.size();
        while ( numCorridors > 0  && power > 0) {
            Random random = new Random();
            // Pick a random sub corridor
            int corridorIndex = random.nextInt(numCorridors);
            Corridor chosenCorridor = corridors.get(corridorIndex);
            List<Equipment> equipments = chosenCorridor.getEquipments();

            //Sort in the descending order of priority
            Collections.sort(equipments);
            //Do not consider high priority equipments for compensation
            equipments.removeIf( equipment ->  equipment.getPriority() == Priority.HIGH);

            for(Equipment equipment: equipments){
                /*Push the equipment into the state change trail.
                This trail is necessary for reverting back the compensation in case of no movement*/
                corridor.getStateChangeTrail().push(equipment);
                equipment.switchOff();
                System.out.println("Switching off "+equipment.getEquipmentType()+" of Floor "+ floor.getNumber()
                        +", Corridor "+ chosenCorridor.getId());

                power -= equipment.getPowerUnits();
                if( power <= 0)
                    break;
            }
            numCorridors--;
        }

        if( power > 0){
            throw new Exception("ALERT!! Power consumption after compensation is exceeded by "+ power +" units");
        }
    }

    /**
     * Retrieves the floor and corridor information from the input sensor id
     * and further processes the no-movement action.
     * @param sensorId
     */
    private void handleNoMovement( String sensorId ){
        try {
            Floor floor = getFloorFromSensorId(sensorId);
            Corridor corridor = getCorridorFromSensorId(sensorId, floor);

            System.out.println("No Movement detected at Floor number "
                    + floor.getNumber() +", Corridor id "+ corridor.getId());
            corridor.setMovementDetected(false);

            controlEquipmentsOnNoMovement(floor, corridor);

            // Check if there are no movements in all the sub corridors
            long corridorsWithMovement = floor.getCorridors().stream().filter( Corridor::isMovementDetected).count();
            if(corridorsWithMovement == 0){
                floor.getCorridors().forEach( corr -> {
                    revertStateChange(corr.getStateChangeTrail());
                });
            }
            printFloorsInfo();

        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Controls the state of equipments of the given corridor in which no movement was observed.
     * @param floor
     * @param corridor
     */
    private void controlEquipmentsOnNoMovement(Floor floor, Corridor corridor){
        revertStateChange(corridor.getStateChangeTrail());

        long difference = getPowerDifference(floor);
        if(difference > 0){
            List<Equipment> equipments = corridor.getEquipments();
            Collections.sort(equipments);

            while( difference > 0){
                // Compensate by switching off equipments in this corridor
                for(Equipment equipment: equipments){
                    equipment.switchOff();
                    System.out.println("Switching off "+equipment.getEquipmentType()+" of Floor "+ floor.getNumber()
                            +", Corridor "+ corridor.getId());
                    corridor.getStateChangeTrail().push(equipment);
                    difference -= equipment.getPowerUnits();
                    if( difference <= 0)
                        break;
                }
            }
        }
    }

    /**
     * Brings back equipments to the state in which they were present before movement was detected
     * in the corridor.
     * @param stateChangeTrail
     */
    private void revertStateChange(Stack<Equipment> stateChangeTrail){
        while( !stateChangeTrail.isEmpty()){
            Equipment equipment = stateChangeTrail.pop();
            if(equipment.isOff()){
                equipment.switchOn();
            } else{
                equipment.switchOff();
            }
        }
    }

    /**
     * Returns the difference between the actual power consumption and maximum allowed power consumption
     * @param floor
     * @return difference
     */
    private long getPowerDifference(Floor floor){
        long difference = floor.getPowerConsumption() - floor.getMaxPowerConsumption();
        if(difference > 0) {
            System.out.println("Power consumption is " + floor.getPowerConsumption()+" units");
            System.out.println("Power consumption exceeded by " + difference+" units");
        }
        return difference;
    }

    /**
     * Retrieves the floor object based on the floor number present in the sensor id.
     * @param sensorId
     * @return floor
     * @throws Exception when the floor with given number is not found
     */
    private Floor getFloorFromSensorId(String sensorId) throws Exception {
        String[] sensorIdParts = sensorId.split("-");
        int floorNumber = Integer.parseInt(sensorIdParts[0]);
        Optional<Floor> floorOptional = hotel.getFloors().stream()
                .filter(floor -> floor.getNumber() == floorNumber)
                .findFirst();

        if(floorOptional.isPresent())
            return floorOptional.get();
        else
            throw new Exception("Floor with number "+ floorNumber+" not found");
    }

    /**
     * Retrieves the corridor object based on the corridor id present in the sensor id.
     * @param sensorId
     * @return floor
     * @throws Exception when the corridor with given id is not found
     */
    private Corridor getCorridorFromSensorId( String sensorId, Floor floor) throws Exception {
        String[] sensorIdParts = sensorId.split("-");
        String corridorId = sensorIdParts[1];

        Optional<Corridor> corridorOptional = floor.getCorridors().stream()
                .filter( corridor -> corridorId.equals(corridor.getId()))
                .findFirst();

        if(corridorOptional.isPresent())
            return corridorOptional.get();
        else
            throw new Exception("Corridor with id "+ corridorId+" not found");
    }

    /**
     * Prints the information of each floor in the hotel
     */
    public void printFloorsInfo(){
        hotel.getFloors().forEach(Floor::printFloorInfo);
        System.out.println("*******************************************************");
    }
}
