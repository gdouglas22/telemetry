package ru.yandex.practicum.collector.model.hub;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(callSuper = true)
public class ScenarioAddedEvent extends HubEvent {
    @NotBlank
    private String name;
    private List<ScenarioCondition> conditions = new ArrayList<>();
    private List<DeviceAction> actions = new ArrayList<>();

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
