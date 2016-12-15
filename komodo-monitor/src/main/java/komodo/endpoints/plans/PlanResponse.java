package komodo.endpoints.plans;

import java.util.HashMap;
import java.util.Map;

public class PlanResponse {
    private String name;
    private boolean active;
    private Boolean successful;
    private String lastUpdate;
    private double interval;
    private Map<String, String> config = new HashMap<>();

    public PlanResponse(String name, boolean active, Boolean successful, double interval, String lastUpdate) {
        this.name = name;
        this.active = active;
        this.successful = successful;
        this.interval = interval;
        this.lastUpdate = lastUpdate;
    }

    public String getName() {
        return name;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public boolean isActive() {
        return active;
    }

    public Map<String, String> getConfig() {
        return config;
    }

    public double getInterval() {
        return interval;
    }
}
