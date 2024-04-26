package ru.itmo.simulator.bluetooth.exception;

import lombok.Getter;
import ru.itmo.simulator.bluetooth.host.att.ATT;

@Getter
public class AttError extends ProtocolError {
    private final int attHandle;
    private final String message;
    private final String errorName;

    public AttError(int errorCode, int attHandle, String message) {
        String errorName = ATT.ATT_ERROR_NAMES.get("errorCode");
        super(errorCode, "att", errorName, message);
        this.attHandle = attHandle;
        this.message = message;
        this.errorName = errorName;
    }

    @Override
    public String toString() {
        return String.format("ATT_Error(error=%s, handle=%04X): %s", this.errorName, this.attHandle, this.message);
    }
}
