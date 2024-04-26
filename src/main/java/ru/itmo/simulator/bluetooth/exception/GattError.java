package ru.itmo.simulator.bluetooth.exception;

public class GattError extends BaseError {
    public GattError(String errorName, String details) {
        super(0xFF, "gatt", errorName, details);
    }
}
