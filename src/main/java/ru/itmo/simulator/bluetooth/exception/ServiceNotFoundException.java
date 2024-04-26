package ru.itmo.simulator.bluetooth.exception;

import java.util.UUID;

public class ServiceNotFoundException extends GattError{
    public ServiceNotFoundException(UUID uuid) {
        super("SERVICE_NOT_FOUND", "Service with given uuid not found=" + uuid);
    }
}
