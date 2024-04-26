package ru.itmo.simulator.bluetooth.exception;

public class ProtocolError extends BaseError {
    public ProtocolError(Integer errorCode, String errorNamespace, String errorName, String details) {
        super(errorCode, errorNamespace, errorName, details);
    }
}
