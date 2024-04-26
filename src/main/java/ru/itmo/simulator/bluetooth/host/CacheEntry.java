package ru.itmo.simulator.bluetooth.host;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode
public class CacheEntry {
    private LocalDateTime timestamp;
    private byte[] bytes;
}
