import com.fasterxml.jackson.databind.ObjectMapper;
import controller.Controller;
import corridor.Corridor;
import corridor.MainCorridor;
import corridor.SubCorridor;
import equipment.Equipment;
import equipment.EquipmentType;
import hotel.Floor;
import hotel.Hotel;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import sensor.MovementSensor;
import sensor.Sensor;
import util.Action;
import util.InputFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;


public class FunctionalTest {
    /**
     * Number of floors: 2
     * Number of main corridors: 1
     * Number of sub corridors: 2
     * Mode: NIGHT
     * Actions:
     * 1. Movement in Floor 1, Sub corridor 1
     * 2. No Movement in Floor 1, Sub corridor 1
     * 3. Movement in Floor 1, Sub corridor 2
     * 4. No Movement in Floor 1, Sub corridor 2
     * 5. Movement in Floor 2, Sub corridor 1
     * 6. No Movement in Floor 2, Sub corridor 1
     * 7. Movement in Floor 2, Sub corridor 2
     * 8. Movement in Floor 2, Sub corridor 2
     */
    @Test
    public void testInput1() {
        System.out.println("START --- Executing usecase from input.json -- START");
        System.out.println("**********************************************");
        InputFile input = readSettingsFromFile("input.json");
        executeTest(input);
        System.out.println("END --- Executing usecase from input.json -- END");
        System.out.println("********************************************");
    }

    private void executeTest(InputFile inputFile){
        Hotel hotel = new Hotel();
        createFloors( hotel, inputFile.getNumFloors());
        hotel.getFloors().forEach( floor -> createMainCorridors(floor, inputFile.getNumMainCorridors()));
        hotel.getFloors().forEach( floor -> createSubCorridors(floor, inputFile.getNumSubCorridors()));

        //Add equipments to each corridor
        hotel.getFloors().stream()
                .map( floor -> floor.getCorridors())
                .flatMap(Collection::stream)
                .forEach(this::addEquipments);

        // Add sensors to each corridor
        hotel.getFloors().forEach(this::addSensors);

        Controller controller = new Controller();
        hotel.setController(controller);
        controller.initializeController(hotel);
        controller.printFloorsInfo();

        //Execute the sequence of events provided in input.json file
        execute(hotel, inputFile.getActions());

        //Unregister the sensor events
        controller.unregisterFromSensorEvents();
    }

    private static void execute(Hotel hotel, List<Action> events){
        events.forEach( event -> {
            hotel.getFloorByNumber(event.getFloorNumber())
                    .ifPresent(floor -> floor.getCorridorById(event.getCorridorId())
                            .ifPresent( corridor -> corridor.getSensor().eventDetected(event.isMovementDetected())));
        });
    }

    private void createFloors(Hotel hotel, int numFloors){
        for(int i = 0; i < numFloors; i++ ){
            hotel.addFloor( new Floor(i+1));
        }
    }

    private void createMainCorridors(Floor floor, int numMainCorridors){
        for(int i = 0; i < numMainCorridors; i++ ){
            floor.addCorridors( new MainCorridor(i+1));
        }
    }

    private void createSubCorridors(Floor floor, int numSubCorridors){
        for(int i = 0; i < numSubCorridors; i++ ){
            floor.addCorridors( new SubCorridor(i+1));
        }
    }

    private void addEquipments(Corridor corridor){
        Equipment light = new Equipment(EquipmentType.LIGHT, 5);
        corridor.addEquipment(light);

        Equipment ac = new Equipment(EquipmentType.AC, 10);
        corridor.addEquipment(ac);
    }

    private void addSensors(Floor floor){
        floor.getCorridors().forEach( corridor -> {
            Sensor sensor = new MovementSensor(floor, corridor);
            corridor.setSensor(sensor);
        });
    }

    /**
     * Read the settings input from the given file and map it to the settings.Settings object.
     * @param inputFileName
     * @return settings.Settings
     */
    private InputFile readSettingsFromFile(String inputFileName){
        InputFile inputs = new InputFile();
        try {
            ClassLoader classLoader = FunctionalTest.class.getClassLoader();
            URL fileUrl = classLoader.getResource(inputFileName);
            if( fileUrl != null ) {
                File inputFile = new File(fileUrl.getFile());
                String inputJson = FileUtils.readFileToString(inputFile,StandardCharsets.UTF_8);
                ObjectMapper mapper = new ObjectMapper();
                inputs = mapper.readValue(inputJson, InputFile.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputs;
    }


}
