package ru.itmo.simulator.bluetooth.host.l2cap;

public enum ChannelState {
    INIT,
    CONNECTED,
    CONNECTING,
    DISCONNECTING,
    DISCONNECTED,
    CONNECTION_ERROR
}
