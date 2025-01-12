/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author admin
 */
public class CheckinCheckout {
    private String id;
    private java.sql.Timestamp checkin;
    private java.sql.Timestamp checkout;
    private java.sql.Date date;

    // Getters và Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public java.sql.Timestamp getCheckin() { return checkin; }
    public void setCheckin(java.sql.Timestamp checkin) { this.checkin = checkin; }
    public java.sql.Timestamp getCheckout() { return checkout; }
    public void setCheckout(java.sql.Timestamp checkout) { this.checkout = checkout; }
    public java.sql.Date getDate() { return date; }
    public void setDate(java.sql.Date date) { this.date = date; }
}

