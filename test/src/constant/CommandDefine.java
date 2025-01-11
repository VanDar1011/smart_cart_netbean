/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package constant;

/**
 *
 * @author datth
 */
public enum CommandDefine {
    SEND_APDU("SEND_APDU", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00}),
    GET_INFOR_CARD("GET_INFOR_CARD", new byte[]{(byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00}),
    OTHER_SEND_APDU("OTHER_SEND_APDU", new byte[]{(byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00}),
    GET_PIN("GET_PIN", new byte[]{(byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x00}),
    GET_IMG("GET_IMG", new byte[]{(byte) 0x00, (byte) 0x11, (byte) 0x00, (byte) 0x00}),
    CHANGE_PIN("CHANGE_PIN", new byte[]{(byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}),
    CHECK_PIN("CHECK_PIN", new byte[]{(byte) 0x00, (byte) 0x13, (byte) 0x00, (byte) 0x00}),
    UNCLOCK_CARD("UNCLOCK_CARD", new byte[]{(byte) 0x00, (byte) 0x06, (byte) 0x00, (byte) 0x00}),
    CLOCK_CARD("CLOCK_CARD", new byte[]{(byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x00}),
    GET_PUCKEY("GET_PUCKEY", new byte[]{(byte) 0x00, (byte) 0x16, (byte) 0x00, (byte) 0x00}),
    GET_ID("GET_ID", new byte[]{(byte) 0x00, (byte) 0x19, (byte) 0x00, (byte) 0x00}),
    GET_STATUS("GET_STATUS", new byte[]{(byte) 0x00, (byte) 0x24, (byte) 0x00, (byte) 0x00}),
    CREATE_RSA_KEY("CREATE_RSA_KEY", new byte[]{(byte) 0x00, (byte) 0x17, (byte) 0x00, (byte) 0x00}),
    TEST_CRYPTO("TEST_CRYTO", new byte[]{(byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x00});
    private final String command;
    private final byte[] value;

    // Constructor to initialize the fields
    CommandDefine(String command, byte[] value) {
        this.command = command;
        this.value = value;
    }

    // Getter for the command name
    public String getCommand() {
        return command;
    }

    // Getter for the value
    public byte[] getValue() {
        return value;
    }

}
