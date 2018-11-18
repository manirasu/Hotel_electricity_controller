package listener;

public interface EventListener {
    void onEventDetected(String sensorId, boolean detected);
}
