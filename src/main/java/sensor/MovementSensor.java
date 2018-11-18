package sensor;

import corridor.Corridor;
import hotel.Floor;

public class MovementSensor extends Sensor {

    public MovementSensor(Floor floor, Corridor corridor){
        super(floor, corridor);
    }

    public void eventDetected(boolean detected){
        this.notifyListener(detected);
    }
}
