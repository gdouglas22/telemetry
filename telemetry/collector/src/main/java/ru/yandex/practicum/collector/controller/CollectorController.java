package ru.yandex.practicum.collector.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.yandex.practicum.collector.service.EventCollectorService;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class CollectorController extends CollectorControllerGrpc.CollectorControllerImplBase {

    private final EventCollectorService eventCollectorService;

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.debug("Received sensor event: id={}, hubId={}, payload={}",
                    request.getId(), request.getHubId(), request.getPayloadCase());
            eventCollectorService.collectSensorEvent(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Failed to process sensor event", e);
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL.withDescription(e.getLocalizedMessage()).withCause(e)));
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<Empty> responseObserver) {
        try {
            log.debug("Received hub event: hubId={}, payload={}",
                    request.getHubId(), request.getPayloadCase());
            eventCollectorService.collectHubEvent(request);
            responseObserver.onNext(Empty.getDefaultInstance());
            responseObserver.onCompleted();
        } catch (Exception e) {
            log.error("Failed to process hub event", e);
            responseObserver.onError(new StatusRuntimeException(
                    Status.INTERNAL.withDescription(e.getLocalizedMessage()).withCause(e)));
        }
    }
}
