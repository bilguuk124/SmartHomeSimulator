package ru.itmo.simulator.bluetooth.host.hci;

import lombok.Getter;

import java.nio.ByteBuffer;

@Getter
public abstract class HciPacket {
    public final static int HCI_COMMAND_PACKET = 0x01;
    public final static int HCI_ACL_DATA_PACKET = 0x02;

    private int packetType;

    public HciPacket(int packetType) {
        this.packetType = packetType;
    }

    public static HciPacket fromBytes(byte[] packet){
        int packetType = Byte.toUnsignedInt(packet[0]);

        if (packetType == HCI_COMMAND_PACKET) {
            return HciCommand.fromBytes(packet);
        }

        if (packetType == HCI_ACL_DATA_PACKET){
            return HciAclDataPacket.fromBytes(packet);
        }

        throw new IllegalArgumentException("Unknown packet type: " + packetType);
    }


    private static class HciCommand extends HciPacket {

        public HciCommand(int opcode, String name) {
            super(HCI_COMMAND_PACKET);
        }
    }

    @Getter
    public static class HciAclDataPacket extends HciPacket {
        private int connectionHandle;
        private int packetBoundaryFlag;
        private int length;
        private byte[] data;

        public HciAclDataPacket(int connectionHandle, int packetBoundaryFlag, int length, byte[] payload) {
            super(HCI_ACL_DATA_PACKET);
            this.connectionHandle = connectionHandle;
            this.packetBoundaryFlag = packetBoundaryFlag;
            this.length = length;
            this.data = payload;
        }

        public static HciAclDataPacket fromBytes(byte[] packet){
            ByteBuffer buffer = ByteBuffer.wrap(packet);
            int h = buffer.getShort(1) & 0xFF;
            int connectionHandle = h & 0xFF;
            int pbFlag = (h >> 12) & 3;
            int length = buffer.getShort(2) & 0xFF;
            byte[] data = new byte[packet.length - 5];
            buffer.position(5);
            buffer.get(data);
            if (data.length != length){
                throw new IllegalArgumentException("invalid packet length: " + length);
            }
            return new HciAclDataPacket(connectionHandle, pbFlag, length, data);
        }

        public byte[] toBytes(){
            ByteBuffer buffer = ByteBuffer.allocate(data.length + 5);
            int h = (packetBoundaryFlag << 12) | (0) | connectionHandle;
            buffer.put((byte) HCI_ACL_DATA_PACKET);
            buffer.putShort((short) h);
            buffer.putShort((short) length);
            buffer.put(data);
            return buffer.array();
        }
    }
}
