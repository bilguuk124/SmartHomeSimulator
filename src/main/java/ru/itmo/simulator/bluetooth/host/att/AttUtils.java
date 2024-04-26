package ru.itmo.simulator.bluetooth.host.att;

import java.util.Map;

public final class AttUtils {

    private AttUtils(){}

    public static String nameOrNumber(Map<Integer, String> dictionary, int number){
        return nameOrNumber(dictionary, number, null);
    }

    public static String nameOrNumber(Map<Integer, String> dictionary, int number, Integer width) {
        width = width != null ? width : 2;
        String name = dictionary.get(number);
        if (name != null) {
            return name;
        }
        return String.format("0x%0" + width + "X", number);
    }

    public static <Y, T> Y getKeyByValue(Map<Y, T> map, T value) {
        for (Map.Entry<Y, T> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static Integer getPduOpcodeByName(String name){
        Integer opcode = getKeyByValue(ATT.ATT_PDU_NAMES, name);
        if (opcode == null) {
            throw new IllegalArgumentException("Opcode not found: " + name);
        }
        return opcode;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
