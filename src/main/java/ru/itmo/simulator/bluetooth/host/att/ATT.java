package ru.itmo.simulator.bluetooth.host.att;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ATT {

    private ATT(){}

    public final static int ATT_CID = 0x04;
    public final static int ATT_ERROR_RESPONSE = 0x01;
    public final static int ATT_EXCHANGE_MTU_REQUEST = 0x02;
    public final static int ATT_EXCHANGE_MTU_RESPONSE = 0x03;
    public final static int ATT_FIND_INFORMATION_REQUEST = 0x04;
    public final static int ATT_FIND_INFORMATION_RESPONSE = 0x05;
    public final static int ATT_FIND_BY_TYPE_VALUE_REQUEST = 0x06;
    public final static int ATT_FIND_BY_TYPE_VALUE_RESPONSE = 0x07;
    public final static int ATT_READ_BY_TYPE_REQUEST = 0x08;
    public final static int ATT_READ_BY_TYPE_RESPONSE = 0x09;
    public final static int ATT_READ_REQUEST = 0x0A;
    public final static int ATT_READ_RESPONSE = 0x0B;
    public final static int ATT_READ_BLOB_REQUEST = 0x0C;
    public final static int ATT_READ_BLOB_RESPONSE = 0x0D;
    public final static int ATT_READ_MULTIPLE_REQUEST = 0x0E;
    public final static int ATT_READ_MULTIPLE_RESPONSE = 0x0F;
    public final static int ATT_READ_BY_GROUP_TYPE_REQUEST = 0x10;
    public final static int ATT_READ_BY_GROUP_TYPE_RESPONSE = 0x11;
    public final static int ATT_WRITE_REQUEST = 0x12;
    public final static int ATT_WRITE_RESPONSE = 0x13;
    public final static int ATT_WRITE_COMMAND = 0x52;
    public final static int ATT_PREPARE_WRITE_REQUEST = 0x16;
    public final static int ATT_PREPARE_WRITE_RESPONSE = 0x17;
    public final static int ATT_EXECUTE_WRITE_REQUEST = 0x18;
    public final static int ATT_EXECUTE_WRITE_RESPONSE = 0x19;
    public final static int ATT_HANDLE_VALUE_NOTIFICATION = 0x1B;
    public final static int ATT_HANDLE_VALUE_INDICATION = 0x1D;
    public final static int ATT_HANDLE_VALUE_CONFIRMATION = 0x1E;

    public static final HashMap<Integer, String> ATT_PDU_NAMES = new HashMap<>(){{
        put(ATT_ERROR_RESPONSE, "ATT_ERROR_RESPONSE");
        put(ATT_EXCHANGE_MTU_REQUEST, "ATT_EXCHANGE_MTU_REQUEST");
        put(ATT_EXCHANGE_MTU_RESPONSE, "ATT_EXCHANGE_MTU_RESPONSE");
        put(ATT_FIND_INFORMATION_REQUEST, "ATT_FIND_INFORMATION_REQUEST");
        put(ATT_FIND_INFORMATION_RESPONSE, "ATT_FIND_INFORMATION_RESPONSE");
        put(ATT_FIND_BY_TYPE_VALUE_REQUEST, "ATT_FIND_BY_TYPE_VALUE_REQUEST");
        put(ATT_FIND_BY_TYPE_VALUE_RESPONSE, "ATT_FIND_BY_TYPE_VALUE_RESPONSE");
        put(ATT_READ_BY_TYPE_REQUEST, "ATT_READ_BY_TYPE_REQUEST");
        put(ATT_READ_BY_TYPE_RESPONSE, "ATT_READ_BY_TYPE_RESPONSE");
        put(ATT_READ_REQUEST, "ATT_READ_REQUEST");
        put(ATT_READ_RESPONSE, "ATT_READ_RESPONSE");
        put(ATT_READ_BLOB_REQUEST, "ATT_READ_BLOB_REQUEST");
        put(ATT_READ_BLOB_RESPONSE, "ATT_READ_BLOB_RESPONSE");
        put(ATT_READ_MULTIPLE_REQUEST, "ATT_READ_MULTIPLE_REQUEST");
        put(ATT_READ_MULTIPLE_RESPONSE, "ATT_READ_MULTIPLE_RESPONSE");
        put(ATT_READ_BY_GROUP_TYPE_REQUEST, "ATT_READ_BY_GROUP_TYPE_REQUEST");
        put(ATT_READ_BY_GROUP_TYPE_RESPONSE, "ATT_READ_BY_GROUP_TYPE_RESPONSE");
        put(ATT_WRITE_REQUEST, "ATT_WRITE_REQUEST");
        put(ATT_WRITE_RESPONSE, "ATT_WRITE_RESPONSE");
        put(ATT_WRITE_COMMAND, "ATT_WRITE_COMMAND");
        put(ATT_PREPARE_WRITE_REQUEST, "ATT_PREPARE_WRITE_REQUEST");
        put(ATT_PREPARE_WRITE_RESPONSE, "ATT_PREPARE_WRITE_RESPONSE");
        put(ATT_EXECUTE_WRITE_REQUEST, "ATT_EXECUTE_WRITE_REQUEST");
        put(ATT_EXECUTE_WRITE_RESPONSE, "ATT_EXECUTE_WRITE_RESPONSE");
        put(ATT_HANDLE_VALUE_NOTIFICATION, "ATT_HANDLE_VALUE_NOTIFICATION");
        put(ATT_HANDLE_VALUE_INDICATION, "ATT_HANDLE_VALUE_INDICATION");
        put(ATT_HANDLE_VALUE_CONFIRMATION, "ATT_HANDLE_VALUE_CONFIRMATION");
    }};

    public static final List<Integer> ATT_REQUESTS = new ArrayList<>(){{
        add(ATT_EXCHANGE_MTU_REQUEST);
        add(ATT_FIND_INFORMATION_REQUEST);
        add(ATT_FIND_BY_TYPE_VALUE_REQUEST);
        add(ATT_READ_BY_TYPE_REQUEST);
        add(ATT_READ_REQUEST);
        add(ATT_READ_BLOB_REQUEST);
        add(ATT_READ_MULTIPLE_REQUEST);
        add(ATT_READ_BY_GROUP_TYPE_REQUEST);
        add(ATT_WRITE_REQUEST);
        add(ATT_PREPARE_WRITE_REQUEST);
        add(ATT_EXECUTE_WRITE_REQUEST);
    }};

    public static final List<Integer> ATT_RESPONSES = new ArrayList<>(){{
        add(ATT_ERROR_RESPONSE);
        add(ATT_EXCHANGE_MTU_RESPONSE);
        add(ATT_FIND_INFORMATION_RESPONSE);
        add(ATT_FIND_BY_TYPE_VALUE_RESPONSE);
        add(ATT_READ_BY_TYPE_RESPONSE);
        add(ATT_READ_RESPONSE);
        add(ATT_READ_BLOB_RESPONSE);
        add(ATT_READ_MULTIPLE_RESPONSE);
        add(ATT_READ_BY_GROUP_TYPE_RESPONSE);
        add(ATT_WRITE_RESPONSE);
        add(ATT_PREPARE_WRITE_RESPONSE);
        add(ATT_EXECUTE_WRITE_RESPONSE);
    }};

    public static final int ATT_INVALID_HANDLE_ERROR                   = 0x01;
    public static final int ATT_READ_NOT_PERMITTED_ERROR               = 0x02;
    public static final int ATT_WRITE_NOT_PERMITTED_ERROR              = 0x03;
    public static final int ATT_INVALID_PDU_ERROR                      = 0x04;
    public static final int ATT_REQUEST_NOT_SUPPORTED_ERROR            = 0x06;
    public static final int ATT_INVALID_OFFSET_ERROR                   = 0x07;
    public static final int ATT_PREPARE_QUEUE_FULL_ERROR               = 0x09;
    public static final int ATT_ATTRIBUTE_NOT_FOUND_ERROR              = 0x0A;
    public static final int ATT_ATTRIBUTE_NOT_LONG_ERROR               = 0x0B;
    public static final int ATT_INVALID_ATTRIBUTE_LENGTH_ERROR         = 0x0D;
    public static final int ATT_UNLIKELY_ERROR_ERROR                   = 0x0E;
    public static final int ATT_UNSUPPORTED_GROUP_TYPE_ERROR           = 0x10;

    public static final Map<Integer, String> ATT_ERROR_NAMES = new HashMap<>() {{
        put(ATT_INVALID_HANDLE_ERROR, "ATT_INVALID_HANDLE_ERROR");
        put(ATT_READ_NOT_PERMITTED_ERROR, "ATT_READ_NOT_PERMITTED_ERROR");
        put(ATT_WRITE_NOT_PERMITTED_ERROR, "ATT_WRITE_NOT_PERMITTED_ERROR");
        put(ATT_INVALID_PDU_ERROR, "ATT_INVALID_PDU_ERROR");
        put(ATT_REQUEST_NOT_SUPPORTED_ERROR, "ATT_REQUEST_NOT_SUPPORTED_ERROR");
        put(ATT_INVALID_OFFSET_ERROR, "ATT_INVALID_OFFSET_ERROR");
        put(ATT_PREPARE_QUEUE_FULL_ERROR, "ATT_PREPARE_QUEUE_FULL_ERROR");
        put(ATT_ATTRIBUTE_NOT_FOUND_ERROR, "ATT_ATTRIBUTE_NOT_FOUND_ERROR");
        put(ATT_ATTRIBUTE_NOT_LONG_ERROR, "ATT_ATTRIBUTE_NOT_LONG_ERROR");
        put(ATT_INVALID_ATTRIBUTE_LENGTH_ERROR, "ATT_INVALID_ATTRIBUTE_LENGTH_ERROR");
        put(ATT_UNLIKELY_ERROR_ERROR, "ATT_UNLIKELY_ERROR_ERROR");
        put(ATT_UNSUPPORTED_GROUP_TYPE_ERROR, "ATT_UNSUPPORTED_GROUP_TYPE_ERROR");
    }};

    public static final int ATT_DEFAULT_MTU = 23;
    public static final Map<String, Object> HANDLE_FIELD_SPEC = Map.of("size",2,"mapper",  (Function<Integer, String>) (x) -> String.format("0x%04X", x));

}

