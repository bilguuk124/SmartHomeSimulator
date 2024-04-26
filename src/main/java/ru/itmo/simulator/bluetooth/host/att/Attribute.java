package ru.itmo.simulator.bluetooth.host.att;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Async;
import ru.itmo.simulator.bluetooth.Connection;
import ru.itmo.simulator.bluetooth.exception.NotPermittedException;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
@AllArgsConstructor
@Data
@Builder
@Setter
@NoArgsConstructor
public abstract class Attribute {
    private short handle;
    private short endGroupHandle;
    private UUID type;
    private Permissions permission;
    private byte[] value;

    public Attribute(UUID type, Permissions permission, byte[] value) {
        this.handle = 0;
        this.endGroupHandle = 0;
        this.type = type;
        this.permission = permission;
        this.value = value;
    }

    @Async
    public CompletableFuture<byte[]> readValue() {
        CompletableFuture<byte[]> future = new CompletableFuture<>();
        if (((this.permission.getValue() & Permissions.READABLE.getValue()) != 1) &&
        (this.permission.getValue() & Permissions.READABLE_WRITABLE.getValue()) != 1) {
            future.completeExceptionally(new NotPermittedException(ATT.ATT_READ_NOT_PERMITTED_ERROR, handle));
            return future;
        }
        future.complete(value);
        return future;
    }

    @Async
    public void writeValue(Connection connection, byte[] value) {
        if ((this.permission.getValue() & Permissions.WRITABLE.getValue()) != 1){
            throw new NotPermittedException(ATT.ATT_WRITE_NOT_PERMITTED_ERROR, handle);
        }
        this.value = value;
    }

    @Override
    public String toString(){
        String valueString = ", value=" + value;
        return String.format("Attribute(handle=0x%04X, type=%s, permissions = %d%s",
                this.handle, this.type, this.permission.getValue(), valueString);
    }
}
