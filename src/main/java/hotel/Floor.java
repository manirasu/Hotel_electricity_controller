package hotel;

import corridor.Corridor;
import corridor.MainCorridor;
import corridor.SubCorridor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Floor {
    private int number;

    private List<Corridor> corridors = new ArrayList<>();

    public Floor(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<Corridor> getCorridors() {
        return new ArrayList<>(this.corridors);

    }

    public void addCorridors(Corridor corridors) {
        this.corridors.add(corridors);
    }
    public long getMaxPowerConsumption() {
        return getMainCorridorsSize() * 15 + getSubCorridorsSize() * 10;
    }

    public long getMainCorridorsSize() {
        return corridors.stream().filter(corridor -> corridor instanceof MainCorridor).count();
    }

    public long getSubCorridorsSize() {
        return corridors.stream().filter(corridor -> corridor instanceof SubCorridor).count();
    }
    /**
     * Get power consumption of the floor by summing up the power units consumed by each active floor
     * @return totalPower
     */


    public long getPowerConsumption() {
        long power = 0;
        for(Corridor corridor : corridors) {
            power+= corridor.getPowerConsumption();
        }
        return power;
    }

    public Optional<Corridor> getCorridorById(String id){
        return corridors.stream()
                .filter( corridor -> corridor.getId().equals(id))
                .findFirst();
    }

    public void setDefaultState() {
        corridors.forEach(corridor -> corridor.setDefaultState());
    }

    public void printFloorInfo() {
        System.out.println("              Floor " + this.number);
        List<Corridor> mainCorridors = corridors.stream()
                .filter(corridor -> corridor instanceof MainCorridor).collect(Collectors.toList());
        List<Corridor> subCorridors = corridors.stream()
                .filter(corridor -> corridor instanceof SubCorridor).collect(Collectors.toList());

        mainCorridors.forEach(mainCorridor -> {
            System.out.print("Main corridor " + (mainCorridors.indexOf(mainCorridor) + 1));
            mainCorridor.printCorridorInfo();
            System.out.println();
        });

        subCorridors.forEach(subCorridor -> {
            System.out.print("Sub corridor " + (subCorridors.indexOf(subCorridor) + 1));
            subCorridor.printCorridorInfo();
            System.out.println();
        });

        System.out.println("Power consumption is " + getPowerConsumption() +" units");

    }

}
