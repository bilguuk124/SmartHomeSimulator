package ru.itmo.simulator.bluetooth.host.att.pdu;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.itmo.simulator.bluetooth.host.att.ATT;
import ru.itmo.simulator.bluetooth.host.att.AttUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import static ru.itmo.simulator.bluetooth.host.att.ATT.HANDLE_FIELD_SPEC;
import static ru.itmo.simulator.bluetooth.host.att.AttUtils.getPduOpcodeByName;

@Data
@EqualsAndHashCode
public abstract class AttPdu {
    private static Map<Integer, Class<? extends AttPdu>> pduClasses = new HashMap<>();
    private String name;
    private int opCode;
    private byte[] pdu;
    private Properties fields;

    public AttPdu(int opCode, String name, byte[] pdu, Properties fields) {
        this.opCode = opCode;
        this.name = name;
        this.pdu = pdu;
        this.fields = fields;
    }

    public AttPdu(int opCode, String name, Properties fields) {
        if (fields != null){
            // init from fields
        }
        // init pdu from dict to bytes
        this.opCode = opCode;
        this.name = name;
    }

    private AttPdu(){}


    public static AttPdu fromBytes(byte[] pdu) {
        int opCode = pdu[0];
        
        Class<? extends AttPdu> clazz = pduClasses.get(opCode);
        if (clazz == null) {
        }
        throw new RuntimeException();

    }

    public static String pduName(int opCode) {
        return AttUtils.nameOrNumber(ATT.ATT_PDU_NAMES, opCode);
    }

    public static String errorName(int errorCode){
        return AttUtils.nameOrNumber(ATT.ATT_ERROR_NAMES, errorCode);
    }

    public boolean isCommand(){
        return ((opCode >> 6) & 1) == 1;
    }

    public byte[] toBytes(){
        return pdu;
    }

    private static void subclass(int opCode, Class<? extends AttPdu> clazz){
        pduClasses.put(opCode, clazz);
    }

    public static class ATT_ERROR_RESPONSE extends AttPdu {
        public ATT_ERROR_RESPONSE() {
            final String name = "ATT_ERROR_RESPONSE";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("request_opcode_in_error", Map.of("size", 1, "mapper", (Function<Integer, String>) AttPdu::pduName));
            fields.put("attribute_handle_in_error", Map.of("size", 2, "mapper", HANDLE_FIELD_SPEC));
            fields.put("error_code", Map.of("size", 1, "mapper", (Function<Integer, String>) AttPdu::errorName));
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_ERROR_RESPONSE.class);
        }
    }

    public static class ATT_EXCHANGE_MTU_REQUEST extends AttPdu{
        public ATT_EXCHANGE_MTU_REQUEST() {
            final String name = "ATT_EXCHANGE_MTU_REQUEST";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("client_rx_mtu",2);
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_EXCHANGE_MTU_REQUEST.class);
        }
    }

    public static class ATT_EXCHANGE_MTU_RESPONSE extends AttPdu{
        public ATT_EXCHANGE_MTU_RESPONSE() {
            final String name = "ATT_EXCHANGE_MTU_RESPONSE";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("server_rx_mtu", 2);
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_EXCHANGE_MTU_RESPONSE.class);
        }
    }

    public static class ATT_FIND_INFORMATION_REQUEST extends AttPdu {
        public ATT_FIND_INFORMATION_REQUEST() {
            final String name = "ATT_FIND_INFORMATION_REQUEST";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("starting_handle", HANDLE_FIELD_SPEC);
            fields.put("ending_handle", HANDLE_FIELD_SPEC);
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_FIND_INFORMATION_REQUEST.class);
        }
    }

    public static class ATT_FIND_INFORMATION_RESPONSE extends AttPdu {
        public ATT_FIND_INFORMATION_RESPONSE() {
            final String name = "ATT_FIND_INFORMATION_RESPONSE";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("format", 1);
            fields.put("information_data", '*');
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_FIND_INFORMATION_RESPONSE.class);
            //TODO: parse
        }
    }

    public static class ATT_FIND_BY_TYPE_VALUE_REQUEST extends AttPdu{
        public ATT_FIND_BY_TYPE_VALUE_REQUEST() {
            final String name = "ATT_FIND_BY_TYPE_VALUE_REQUEST";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("starting_handle", HANDLE_FIELD_SPEC);
            fields.put("ending_handle", HANDLE_FIELD_SPEC);
            fields.put("attribute_type", (Function<String, UUID>) UUID::fromString);
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_FIND_BY_TYPE_VALUE_REQUEST.class);
        }
    }


    public static class ATT_FIND_BY_TYPE_VALUE_RESPONSE extends AttPdu {
        public ATT_FIND_BY_TYPE_VALUE_RESPONSE(byte[] pdu) {
            final String name = "ATT_FIND_BY_TYPE_VALUE_RESPONSE";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("handles_information_list", '*');
            super(opCode, name, pdu, fields);
            AttPdu.subclass(opCode, ATT_FIND_BY_TYPE_VALUE_RESPONSE.class);
        }

        //TODO Parse?
    }

    public static class ATT_READ_BY_TYPE_REQUEST extends AttPdu{
        public ATT_READ_BY_TYPE_REQUEST() {
            final String name = "ATT_READ_BY_TYPE_REQUEST";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("starting_handle", HANDLE_FIELD_SPEC);
            fields.put("ending_handle", HANDLE_FIELD_SPEC);
            fields.put("attribute_type", (Function<String, UUID>) UUID::fromString);
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_READ_BY_TYPE_REQUEST.class);
        }
    }

    public static class ATT_READ_BY_TYPE_RESPONSE extends AttPdu {


        public ATT_READ_BY_TYPE_RESPONSE() {
            final String name = "ATT_READ_BY_TYPE_RESPONSE";
            int opCode = getPduOpcodeByName(name);
            Properties fields = new Properties();
            fields.put("length", 1);
            fields.put("attribute_data_list", '*');
            super(opCode, name, fields);
            AttPdu.subclass(opCode, ATT_READ_BY_TYPE_RESPONSE.class);
        }

        public void parseAttributeDataList(){

        }
    }



}
