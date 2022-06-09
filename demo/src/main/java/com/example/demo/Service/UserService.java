package com.example.demo.Service;
import java.sql.SQLException;
import java.text.ParseException;

import javax.sql.rowset.serial.SerialException;

import com.example.demo.Model.User;
public interface UserService {
    public abstract void createUser(String name,String surname,String email,String username,String password);
    public abstract void updateUserProfileData(String username , String name ,String surname , String email);
    public abstract void deleteUser(String username,String password);
    public abstract User getUser(String username,String password);
    public abstract  void changePassword(String username,String old_password,String new_password);
    public abstract void setpic( String base64,String username) throws SerialException, SQLException;
    public abstract String generateToken(String username) throws SQLException;
    public void sendemail(String username) throws SQLException;
    public void resetPass(String token,String newpassWord) throws SQLException, ParseException;
}
