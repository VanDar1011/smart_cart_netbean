/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import ui.MainGui;
import constant.CommandDefine;
import javax.smartcardio.*;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JOptionPane;

/**
 *
 * @author datth
 */
public class Main {

    public static final byte[] AID_APPLET = {(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x09, (byte) 0x00};
    private Card card;
    private TerminalFactory factory;
    private CardChannel chanel;
    private CardTerminal terminal;
    private List<CardTerminal> terminals;
    private ResponseAPDU response;
    private CommandAPDU apduCommand;
    private boolean statusConnected = false;

    public boolean isConnected() {
        return this.statusConnected;
    }

    public void setConnected(boolean statusConnected) {
        this.statusConnected = statusConnected;
    }
    private MainGui gui; // Reference to the GUI
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public Main() {

    }

    /**
     * @param args the command line arguments
     */
//    public static void main(String[] args) {
//        MainGui gui = new MainGui();
//        gui.setVisible(true);
//        Main smartCard = new Main();
////        JOptionPane.showMessageDialog(null, "oci do");
//        EmployeeDAO.getAccountById(1);
//        EmployeeDAO.updatePublicKey("datngo", 1);
//    }

    public boolean connectCard() {
        boolean currentStatus = false;
        try {
            factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            if (terminals.isEmpty()) {
                logger.info("No card terminals found.");
                return currentStatus;
            }
            terminal = terminals.get(0);
            card = terminal.connect("T=0");
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
                this.statusConnected = true;
                currentStatus = true;
            } else if (check.equals("6400")) {
                currentStatus = false;
            }
        } catch (Exception e) {
            logger.severe("Có lỗi xảy ra " + e.getMessage());

        }
        return currentStatus;
    }

    public boolean disconnectCard() {
        try {
            card.disconnect(false);
            this.statusConnected = false;
            System.out.println("Disconnect");
            return true;
        } catch (Exception e) {
            logger.severe("Có lỗi xảy ra " + e.getMessage());
        }
        return false;
    }

    public void sendApdu(CommandDefine commandDefine) {
        if (!this.statusConnected) {
            JOptionPane.showMessageDialog(null, "Vui lòng connect với card trước khi gửi lệnh");
//            System.out.println("Vui long connect truoc khi gui lenh");
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
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

}
