package ru.yandex.practicum.analyzer.service;

import com.google.protobuf.Timestamp;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Action;
import ru.yandex.practicum.analyzer.model.Scenario;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc.HubRouterControllerBlockingStub;

import java.time.Instant;

@Slf4j
@Service
public class HubRouterClientImpl implements HubRouterClient {

    private final HubRouterControllerBlockingStub hubRouterClient;

    public HubRouterClientImpl(@GrpcClient("hub-router")
                               HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    @Override
    public void sendAction(Scenario scenario, String sensorId, Action action) {
        DeviceActionProto.Builder actionBuilder = DeviceActionProto.newBuilder()
                .setSensorId(sensorId)
                .setType(ActionTypeProto.valueOf(action.getType().name()));
        if (action.getValue() != null) {
            actionBuilder.setValue(action.getValue());
        }

        Instant now = Instant.now();
        DeviceActionRequest request = DeviceActionRequest.newBuilder()
                .setHubId(scenario.getHubId())
                .setScenarioName(scenario.getName())
                .setAction(actionBuilder)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(now.getEpochSecond())
                        .setNanos(now.getNano()))
                .build();

        try {
            hubRouterClient.handleDeviceAction(request);
            log.info("Действие {} отправлено датчику {} (сценарий '{}', хаб {})",
                    action.getType(), sensorId, scenario.getName(), scenario.getHubId());
        } catch (StatusRuntimeException e) {
            log.error("Не удалось отправить действие датчику {} (сценарий '{}', хаб {})",
                    sensorId, scenario.getName(), scenario.getHubId(), e);
        }
    }
}
