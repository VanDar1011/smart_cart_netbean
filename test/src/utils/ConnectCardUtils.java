/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import constant.CommandDefine;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import ui.Home;

/**
 *
 * @author datth
 */
public class ConnectCardUtils {

    private static final byte CLA = 0x00;
    private static final byte INS_SEND_DATA = (byte) 0x08; // Example INS
    private static final short CHUNK_SIZE = 240;
    public static final byte[] AID_APPLET = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x0D, (byte) 0x00}; // 0d
    private static Card card;
    private static TerminalFactory factory;
    private static CardChannel chanel;
    private static CardTerminal terminal;
    private static List<CardTerminal> terminals;
    private static ResponseAPDU response;
    public ResponseAPDU resAPDU;
    private static CommandAPDU apduCommand;
    private static final Logger logger = Logger.getLogger(Home.class.getName());
    private static boolean statusConnected = false;

    public static boolean isConnected() {
        return statusConnected;
    }

    public static void setConnected(boolean statusConnected) {
        statusConnected = statusConnected;
    }

    public static boolean connectCard() {
        boolean currentStatus = false;
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                logger.info("No card terminals found.");
                return currentStatus;
            }
            terminal = terminals.get(0);
            card = terminal.connect("T=1");
            chanel = card.getBasicChannel();
            if (chanel == null) {
                logger.info("Failed to get a card channel.");
                return currentStatus;
            }
            apduCommand = new CommandAPDU(0x00, (byte) 0xA4, 0x04, 0x00, AID_APPLET);
            response = chanel.transmit(apduCommand);
            String check = Integer.toHexString(response.getSW());
            if (check.equals("9000")) {
                System.out.println(check.toString());
                statusConnected = true;
                currentStatus = true;
                JOptionPane.showMessageDialog(null, "Kết nối thẻ thành công");
            } else if (check.equals("6400")) {
                JOptionPane.showMessageDialog(null, "Kết nối không thành công. Vui lòng thử lại");
                currentStatus = false;
            }
        } catch (Exception ex) {
            logger.severe("Có lỗi xảy ra " + ex.getMessage());

        }
        return currentStatus;
    }

    public static boolean disconnectCard() {
        try {
            card.disconnect(false);
            statusConnected = false;
            System.out.println("Disconnect");
            JOptionPane.showMessageDialog(null, "Ngắt kết nối thẻ thành công");
            return true;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Ngắt kết nối không thành công. Vui lòng thử lại");
            logger.severe("Có lỗi xảy ra " + ex.getMessage());
        }
        return false;
    }

    public static void sendApdu(CommandDefine commandDefine) {
        if (!statusConnected) {
            JOptionPane.showMessageDialog(null, "Vui lòng connect với card trước khi gửi lệnh");
            System.out.println("Vui long connect truoc khi gui lenh");
        } else {
            try {
                CommandAPDU command = new CommandAPDU(commandDefine.getValue());
                ResponseAPDU response = chanel.transmit(command);
                String statusWord = Integer.toHexString(response.getSW());
                System.out.println("Response SW: " + statusWord);
                if (statusWord.equals("9000")) {
                    System.out.println("APDU Command executed successfully!");
                } else {
                    System.out.println("APDU Command failed with status: " + statusWord);
                }
            } catch (CardException ex) {
                logger.severe("Có lỗi xảy ra " + ex.getMessage());
            }
        }
    }

    public static void sendApduFromString(String apduCommand, JFrame jfame) throws InterruptedException {
//        System.out.println("apduCommand : " + apduCommand.length());
        if (!statusConnected) {
            JOptionPane.showMessageDialog(null, "Vui lòng connect với card trước khi gửi lệnh");
            System.out.println("Vui long connect truoc khi gui lenh");
        } else {
            try {
                // Kiểm tra nếu chuỗi nhập vào hợp lệ và có độ dài đúng cho lệnh APDU
                if (apduCommand != null && apduCommand.length() > 0) {
                    // Chuyển chuỗi nhập vào thành mảng byte
                    byte[] commandBytes = StringUtils.hexStringToByteArray(apduCommand);
                    CommandAPDU command = new CommandAPDU(commandBytes);
                    ResponseAPDU response = chanel.transmit(command);
                    String statusWord = Integer.toHexString(response.getSW());
                    System.out.println("Response SW: " + statusWord);
                    if (statusWord.equals("9000")) {
//                        Thread.sleep(2000);
//                        jfame.dispose();
                        System.out.println("APDU Command executed successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Đã có lỗi xảy ra");
                        System.out.println("APDU Command failed with status: " + statusWord);
                    }
                } else {
                    System.out.println("Chuỗi APDU không hợp lệ.");
                }
            } catch (CardException ex) {
                logger.severe("Có lỗi xảy ra " + ex.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Lỗi: Chuỗi APDU không đúng định dạng Hex.");
            }
        }
    }

    public static void sendDataToCard(byte[] data) throws CardException {
        if (chanel == null) {
            throw new IllegalArgumentException("CardChannel cannot be null");
        }

        short offset = 0;
        while (offset < data.length) {
            // Tính toán kích thước chunk (tối đa CHUNK_SIZE hoặc phần còn lại của dữ liệu)
            short length = (short) Math.min(CHUNK_SIZE, data.length - offset);

            // Lấy một chunk từ dữ liệu
            byte[] chunk = Arrays.copyOfRange(data, offset, offset + length);

            // Tạo lệnh APDU để gửi chunk
            CommandAPDU command = new CommandAPDU(CLA, INS_SEND_DATA, (byte) (offset >> 8), (byte) offset, chunk);
            System.out.println("Sending chunk: offset=" + offset + ", length=" + length);

            // Gửi lệnh và kiểm tra phản hồi
            ResponseAPDU response = chanel.transmit(command);
            if (response.getSW() != 0x9000) {
                throw new CardException("Error sending data at offset " + offset + ". SW=" + Integer.toHexString(response.getSW()));
            }

            // Cập nhật offset
            offset += length;
        }

        System.out.println("Data sent successfully!");
    }

    public static void sendExtendedApduFromString(byte[] apduCommand, JFrame jfame) throws InterruptedException {
        System.out.println("apduCommand length: " + (apduCommand != null ? apduCommand.length : "null"));
        if (!statusConnected) {
            JOptionPane.showMessageDialog(jfame, "Vui lòng connect với card trước khi gửi lệnh");
            System.out.println("Please connect to the card before sending commands.");
            return;
        }
        try {
            // Convert the hex string to byte array
            CommandAPDU command = new CommandAPDU(0x00, 0x10, 0x00, 0x00, apduCommand);
            // Transmit the command
            ResponseAPDU response = chanel.transmit(command);
            String statusWord = Integer.toHexString(response.getSW());
            System.out.println("Response SW: " + statusWord);

            if ("9000".equals(statusWord)) {
//                JOptionPane.showMessageDialog(jfame, "Lệnh APDU được thực thi thành công!");
                jfame.dispose();
                System.out.println("APDU Command executed successfully!");
            } else {
                JOptionPane.showMessageDialog(jfame, "Đã có lỗi xảy ra: SW=" + statusWord);
                System.out.println("APDU Command failed with status: " + statusWord);
            }
        } catch (IllegalArgumentException e) {
//            JOptionPane.showMessageDialog(jfame, "Lỗi: Chuỗi APDU không đúng định dạng Hex.");
            System.out.println("Error: Invalid Hex format for APDU command.");
            e.printStackTrace();
        } catch (CardException e) {
//            JOptionPane.showMessageDialog(jfame, "Lỗi: Không thể truyền lệnh đến thẻ.");
            System.out.println("CardException occurred: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
//            JOptionPane.showMessageDialog(jfame, "Đã xảy ra lỗi không xác định.");
            System.out.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static byte[] sendApduHaveResponse(CommandDefine commandDefine) {
        byte[] result = null;
        if (!statusConnected) {
            JOptionPane.showMessageDialog(null, "Vui lòng connect với card trước khi gửi lệnh");
            System.out.println("Vui long connect truoc khi gui lenh");
        } else {
            try {
                System.out.println("APDU Command: " + StringUtils.bytesToHex(commandDefine.getValue()));
                CommandAPDU command = new CommandAPDU(commandDefine.getValue());
                ResponseAPDU response = chanel.transmit(command);
                System.out.println("Response SW: " + Integer.toHexString(response.getSW()));
                byte[] responseData = response.getData();
                String responseDataHex = StringUtils.bytesToHex(responseData);
                System.out.println("Response Data: " + responseDataHex);
                String statusWord = Integer.toHexString(response.getSW());
                System.out.println("Response SW: " + statusWord);
                if (statusWord.equals("9000")) {
                    result = responseData;
                    System.out.println("APDU Command executed successfully!");
                } else {
                    System.out.println("APDU Command failed with status: " + statusWord);
                }
            } catch (CardException ex) {
                logger.severe("Có lỗi xảy ra " + ex.getMessage());
            }
        }
        return result;
    }

    public static byte[] getId(CommandDefine commandDefine) {
        byte[] result = null;
        if (!statusConnected) {
            JOptionPane.showMessageDialog(null, "Vui lòng connect với card trước khi gửi lệnh");
            System.out.println("Vui long connect truoc khi gui lenh");
        } else {
            try {
                System.out.println("APDU Command: " + StringUtils.bytesToHex(commandDefine.getValue()));
                CommandAPDU command = new CommandAPDU(commandDefine.getValue());
                ResponseAPDU response = chanel.transmit(command);
                System.out.println("Response SW: " + Integer.toHexString(response.getSW()));
                byte[] responseData = response.getData();
                String responseDataHex = StringUtils.bytesToHex(responseData);
                System.out.println("Response Data: " + responseDataHex);
                String statusWord = Integer.toHexString(response.getSW());
                System.out.println("Response SW: " + statusWord);
                if (statusWord.equals("9000")) {
                    result = responseData;
                    System.out.println("APDU Command executed successfully!");
                } else {
                    result = new byte[]{
                        0x00, 0x00
                    };
                    System.out.println("APDU Command failed with status: " + statusWord);
                }
            } catch (CardException ex) {
                logger.severe("Có lỗi xảy ra " + ex.getMessage());
            }
        }
        return result;
    }

    public static byte[] sendApduFromStringHaveResponse(String apduCommand) {
        byte[] result = null;
        System.out.println("apduCommand : " + apduCommand);
        if (!statusConnected) {
            JOptionPane.showMessageDialog(null, "Vui lòng connect với card trước khi gửi lệnh");
            System.out.println("Vui long connect truoc khi gui lenh");
        } else {
            try {
                // Kiểm tra nếu chuỗi nhập vào hợp lệ và có độ dài đúng cho lệnh APDU
                if (apduCommand != null && apduCommand.length() > 0) {
                    // Chuyển chuỗi nhập vào thành mảng byte
                    byte[] commandBytes = StringUtils.hexStringToByteArray(apduCommand);
                    CommandAPDU command = new CommandAPDU(commandBytes);
                    ResponseAPDU response = chanel.transmit(command);
                    byte[] responseData = response.getData();
                    String responseDataHex = StringUtils.bytesToHex(responseData);
                    System.out.println("Response Data: " + responseDataHex);
                    String statusWord = Integer.toHexString(response.getSW());
                    System.out.println("Response SW: " + statusWord);
                    if (statusWord.equals("9000")) {
                        System.out.println("APDU Command executed successfully!");
                        result = responseData;
                    } else {
                        System.out.println("APDU Command failed with status: " + statusWord);
                    }
                } else {
                    System.out.println("Chuỗi APDU không hợp lệ.");
                }
            } catch (CardException ex) {
                logger.severe("Có lỗi xảy ra " + ex.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Lỗi: Chuỗi APDU không đúng định dạng Hex.");
            }

        }
        return result;
    }

    public  void sendAPDUtoApplet(byte[] cmnds) {
        try {
            resAPDU= chanel.transmit(new CommandAPDU(cmnds[0], cmnds[1], cmnds[2], cmnds[3]));
        } catch (CardException e) {
            e.printStackTrace();
        }
    }

     public  void sendAPDUtoApplet(byte[] cmnds, byte[] data) {
        try {
             resAPDU = chanel.transmit(new CommandAPDU(cmnds[0], cmnds[1], cmnds[2], cmnds[3], data));
        } catch (CardException e) {
            e.printStackTrace();
        }
    }

// Phương thức phụ để chuyển chuỗi hex thành mảng byte
//    private static byte[] hexStringToByteArray(String s) {
//        int len = s.length();
//        byte[] data = new byte[len / 2];
//        for (int i = 0; i < len; i += 2) {
//            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
//                    + Character.digit(s.charAt(i + 1), 16));
//        }
//        return data;
//    }
    public static byte[] createDataWithPrefix(byte[] data, byte[] prefix) {
        // Tính toán độ dài dữ liệu
        int dataLength = data.length;
//        byte[] lengthByte = new byte[]{(byte) dataLength};
//        byte[] fullData = new byte[prefix.length + lengthByte.length + data.length];
        byte[] lengthByte;
        if (dataLength > 0xFF) {
            // If the length is greater than 255, use a short (2 bytes)
            lengthByte = new byte[2];
            lengthByte[0] = (byte) ((dataLength >> 8) & 0xFF); // High byte
            lengthByte[1] = (byte) (dataLength & 0xFF); // Low byte
        } else {
            // If the length is less than or equal to 255, use a single byte
            lengthByte = new byte[]{(byte) dataLength};
        }

        // Tạo mảng chứa kết quả cuối cùng (prefix + độ dài + dữ liệu)
//        byte[] lengthByte = new byte[]{(byte) dataLength};
        byte[] fullData = new byte[prefix.length + lengthByte.length + data.length];

        // Sao chép dữ liệu vào mảng kết quả
        System.arraycopy(prefix, 0, fullData, 0, prefix.length);
        System.arraycopy(lengthByte, 0, fullData, prefix.length, lengthByte.length);
        System.arraycopy(data, 0, fullData, prefix.length + lengthByte.length, data.length);

        return fullData;
    }

    public static boolean authenPin(String pin) {
        boolean statusAuthen = false;
        try {

//            ConnectCardUtils.sendApdu(CommandDefine.GET_IMG);
//            String pin = txt_pin_code.getText();
            byte[] pinBytes = pin.getBytes(StandardCharsets.UTF_8);
            byte[] fullData = ConnectCardUtils.createDataWithPrefix(pinBytes, CommandDefine.CHECK_PIN.getValue());
            String hexData = StringUtils.bytesToHex(fullData);
            System.out.println("Employee Data with Length : " + hexData.length());
            byte[] result = ConnectCardUtils.sendApduFromStringHaveResponse(hexData);
            String checkPass = StringUtils.bytesToHex(result);
            if (checkPass.equals("9000")) {
                statusAuthen = true;
                JOptionPane.showMessageDialog(null, "Mã pin hợp lệ");
            } else {
                JOptionPane.showMessageDialog(null, "Mã pin không hợp lệ");
            }

        } catch (Exception ex) {
            System.out.println("Exception :" + ex.getMessage());
        }
        return statusAuthen;
    }
}
