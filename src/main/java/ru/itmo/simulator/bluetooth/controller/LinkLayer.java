package ru.itmo.simulator.bluetooth.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.itmo.simulator.bluetooth.Connection;
import ru.itmo.simulator.bluetooth.host.hci.HciPacket;
import ru.itmo.simulator.bluetooth.host.l2cap.L2CapPdu;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("prototype")
public class LinkLayer {
    private Controller controller;
    private PhysicalLayer physicalLayer;
    private Map<Integer, Connection> connections;
    private ConnectionState state;

    private final static int PREAMBLE = 0x44;
    private final static int POLYNOMIAL = 0x1021;

    protected LinkLayer(Controller controller, PhysicalLayer physicalLayer) {
        this.controller = controller;
        this.physicalLayer = physicalLayer;
        this.state = ConnectionState.STANDBY;
        this.connections = new ConcurrentHashMap<>();
    }

    protected void setController(Controller controller) {
        this.controller = controller;
    }

    protected void setPhysicalLayer(PhysicalLayer physicalLayer) {
        this.physicalLayer = physicalLayer;
    }

    public void getAdvertisement(byte[] pdu) {
        if (state != ConnectionState.SCANNING) return;
        
    }
    
    private void parseMessage(Connection connection, byte[] data){
        byte[] header = new byte[]{data[0], data[1]};
        int length = header[1];
        byte rest = header[0];
        byte llid = (byte) (rest & 0xC0);
        if (llid == (byte) 0xC0){
            byte[] dataWithoutHeader = new byte[length];
            System.arraycopy(data, 2, dataWithoutHeader, 0, length);
            onPdu(connection, dataWithoutHeader);
        }
        else{
            onControlPdu(data);
        }
    }

    private void onControlPdu(byte[] data) {
    }

    private void onPdu(Connection connection, byte[] data) {
        L2CapPdu pdu = L2CapPdu.fromBytes(data);
        controller.onPduReceived(pdu.getCid(), pdu.getPayload());
    }


    public void getFrame(Connection connection, byte[] frame) {
        if (frame[0] != PREAMBLE) throw new RuntimeException("Preamble not match");
        if (!isValidAccessAddress(new byte[]{frame[1], frame[2], frame[3], frame[4]})) throw new RuntimeException("Invalid access address");
        byte[] data = new byte[frame.length - 8];
        byte[] crc = new byte[frame.length - data.length - 5];
        System.arraycopy(frame, 5, data, 0, data.length);
        System.arraycopy(frame, data.length + 5, crc, 0, crc.length);
        if (!isCrcValid(crc, data)) throw new RuntimeException("Invalid crc");
        parseMessage(connection, data);
    }


    private boolean isValidAccessAddress(byte[] accessAddress) {
        for (int i = 0; i < 4; i++) {
            for (int j = i + 1; j < 4; j++) {
                if (accessAddress[i] == accessAddress[j]) {
                    return false;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            int countOnes = 0;
            int countZeros = 0;
            int byteValue = accessAddress[i];
            for (int j = 0; j < 8; j++) {
                if ((byteValue & 0x01) == 1) {
                    countOnes++;
                    countZeros = 0;
                } else {
                    countZeros++;
                    countOnes = 0;
                }
                if (countOnes > 6 || countZeros > 6) {
                    return false;
                }
                byteValue >>= 1;
            }
        }

        int msbBits = ((accessAddress[0] & 0x3F) << 24) | ((accessAddress[1] & 0xFF) << 16) |
                ((accessAddress[2] & 0xFF) << 8) | (accessAddress[3] & 0xFF);
        int transitions = 0;
        for (int i = 0; i < 5; i++) {
            if (((msbBits >> i) & 0x01) != ((msbBits >> (i + 1)) & 0x01)) {
                transitions++;
            }
        }
        return transitions >= 2;
    }

    public void sendFrame(int connectionHandle, byte[] frame) {
        ByteBuffer buffer = ByteBuffer.allocate(frame.length + 8);
        buffer.put((byte) PREAMBLE);
        buffer.put(generateAccessAddress());
        buffer.put(frame);
        buffer.put(calculateCrc(frame));
        physicalLayer.sendFrame(connections.get(connectionHandle), buffer.array());
    }

    public void sendFrame(HciPacket packet) {
        if (packet instanceof HciPacket.HciAclDataPacket dataPacket){
            Connection connection = connections.get(dataPacket.getConnectionHandle());
            if (connection == null){
                throw new RuntimeException("Connection not found");
            }
            sendFrame(connection.getHandle(), dataPacket.toBytes());
        }
    }

    private byte[] generateAccessAddress() {
        Random random = new Random();
        byte[] address = new byte[4];
        do{
            random.nextBytes(address);
        } while(!isValidAccessAddress(address));
        return address;
    }

    private byte[] calculateCrc(byte[] message) {
        int crc = 0xFFFF;

        for(byte b : message){
            crc = crc ^ (b & 0xFF);
            for (int i = 0; i < 8; i++){
                if ((crc & 0x01) == 1){
                    crc = (crc >> 1) ^ POLYNOMIAL;
                } else {
                    crc = crc >> 1;
                }
            }
        }

        byte[] crcArray = new byte[3];
        crcArray[0] = (byte) ((crc >> 8) & 0xFF);
        crcArray[1] = (byte) (crc & 0xFF);
        crcArray[2] = (byte) ((crc >> 16) & 0xFF);
        return crcArray;
    }

    private boolean isCrcValid(byte[] crcArray, byte[] data) {
        byte[] calculatedCRC = calculateCrc(data);
        return Arrays.equals(crcArray, calculatedCRC);
    }
}
