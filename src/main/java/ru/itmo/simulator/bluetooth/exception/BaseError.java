package ru.itmo.simulator.bluetooth.exception;

import lombok.Getter;

@Getter
public class BaseError extends RuntimeException {
    private final Integer errorCode;
    private final String errorNamespace;
    private final String errorName;
    private final String details;

    public BaseError(Integer errorCode, String errorNamespace, String errorName, String details) {
        this.errorCode = errorCode;
        this.errorNamespace = errorNamespace;
        this.errorName = errorName;
        this.details = details;
    }

    @Override
    public String toString(){
        String namespace = this.errorNamespace.isEmpty() ? "" : this.errorNamespace + "/";
        boolean haveName = !this.errorName.isEmpty();
        boolean haveCode = this.errorCode != null;

        String errorText;
        if (haveName && haveCode) {
            errorText = String.format("%s [0x%X]", this.errorName, this.errorCode);
        } else if (haveName && !haveCode) {
            errorText = this.errorName;
        } else if (!haveName && haveCode) {
            errorText = String.format("0x%X", this.errorCode);
        } else {
            errorText = "<unspecified>";
        }

        return String.format("%s(%s%s)", this.getClass().getSimpleName(), namespace, errorText);
    }
}
