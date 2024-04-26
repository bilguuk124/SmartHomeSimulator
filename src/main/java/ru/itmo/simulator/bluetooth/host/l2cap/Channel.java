package ru.itmo.simulator.bluetooth.host.l2cap;

import lombok.Getter;
import org.springframework.scheduling.annotation.Async;
import ru.itmo.simulator.bluetooth.Connection;
import ru.itmo.simulator.bluetooth.host.att.pdu.AttPdu;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.CompletableFuture;

@Getter
public class Channel {
    private static final int L2CAP_SIGNALING_CID = 0x05;

    private int sourceCid;
    private int destinationCid;
    private Connection connection;
    private Deque<byte[]> outputQueue;
    private ChannelState state;
    private L2ChannelManager channelManager;

    public Channel(Connection connection, int sourceCid, int destinationCid, L2ChannelManager channelManager) {
        this.connection = connection;
        this.outputQueue = new ArrayDeque<>();
        state = ChannelState.INIT;
        this.sourceCid = sourceCid;
        this.destinationCid = destinationCid;
        this.channelManager = channelManager;
    }

    private void changeState(ChannelState state) {
        this.state = state;
    }

    @Async
    public CompletableFuture<L2CapPdu> connect(){
        if (state != ChannelState.INIT) throw new IllegalStateException("Channel is not connectable");
        int identifier = channelManager.nextIdentifier();
        if (channelManager.getConnectionRequestsByIdentifier(identifier) != null) throw new RuntimeException("Too many requests");

        changeState(ChannelState.CONNECTING);
        L2CapControlFrame.L2CapControlConnectionRequest request = new L2CapControlFrame.L2CapControlConnectionRequest();
        channelManager.addConnectionRequest(identifier, request);
        sendControlFrame( request);
        return new CompletableFuture<>();
    }

    private void sendPdu(byte[] pdu){
        channelManager.sendPdu(connection, destinationCid, pdu);
    }

    private void sendControlFrame(L2CapControlFrame request) {
        channelManager.sendControlFrame(connection, L2CAP_SIGNALING_CID, request);
    }

    @Async
    public CompletableFuture<L2CapPdu> disconnect(){
        if (state != ChannelState.CONNECTED) throw new IllegalStateException("Channel is not connected");
        changeState(ChannelState.DISCONNECTING);
        flushOutput();
        L2CapControlFrame request = new L2CapControlFrame.L2CapControlDisconnectRequest();
        sendControlFrame(request);
        return new CompletableFuture<>();
    }

    private void flushOutput() {
        outputQueue = new ArrayDeque<>();
    }

    public void abort(){
        if(state == ChannelState.DISCONNECTED) {
            changeState(ChannelState.DISCONNECTING);
        }
    }

    public void onPdu(byte[] pdu){
        if (state != ChannelState.CONNECTED) return;
        AttPdu attPdu = AttPdu.fromBytes(pdu);
        if (attPdu.isRequest()){
            channelManager.onGattRequest(connection, AttPdu.fromBytes(pdu));
        } else if (attPdu.isResponse()){
            channelManager.onGattResponse(AttPdu.fromBytes(pdu));
        } else{
            channelManager.gattCommand(AttPdu.fromBytes(pdu));
        }
    }
}
