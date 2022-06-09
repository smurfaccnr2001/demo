package com.example.demo.Service;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Date;


import com.example.demo.Model.Report;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReportServiceImp implements ReportService{
    Connection c;
    
    public ReportServiceImp() throws SQLException{

        c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "JuriVjerdha123@@@");
    }
    @Override
    public void createReport(Report report) throws SQLException {
        Statement statement = c.createStatement();
        ResultSet rs = statement.executeQuery("SELECT MAX (id) FROM reports");
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = Calendar.getInstance().getTime();
        String strDate = dateFormat.format(date);
        if(rs.next()){

            Report report2 = new Report(report.getUsername(),report.getReport(),report.isCanceled(),rs.getLong("max")+1,report.getTitle());
             //check if username exists in users table
                String query1 = "SELECT FROM users where username= '" + report.getUsername().toLowerCase()+"'";
                ResultSet rs1 = statement.executeQuery(query1);
                if(rs1.next()){
                    PreparedStatement prep = c.prepareStatement("INSERT INTO reports(report,canceled,username,id,creationdate,title) VALUES(?,?,?,?,?,?)");
                    prep.setString(1, report2.getReport());
                    prep.setBoolean(2, report2.isCanceled());
                    prep.setString(3, report2.getUsername());
                    prep.setLong(4, report2.getId());
                    prep.setString(5,strDate);
                    prep.setString(6, report2.getTitle());
                    prep.executeUpdate();
                    ResultSet rs2 = statement.executeQuery("SELECT report FROM reports where id="+(report2.getId()-1));
                    while(rs2.next()){
                        PreparedStatement prep2 = c.prepareStatement("UPDATE reports SET hashcode = ? , correnthash = ?  WHERE id= ?");
    
                        prep2.setInt(1, rs2.getString("report").hashCode());
                        prep2.setInt(2, report.getReport().hashCode());
                        prep2.setLong(3, report2.getId());
                        prep2.executeUpdate();
                        
    
                    } 
                
        
                } else{
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User doesn't exist");
                }

        }
    }

    @Override
    public void updateReport(long id ,boolean canceled) {
    try {
        
       
            PreparedStatement prep =c.prepareStatement("UPDATE reports SET canceled = ? WHERE id =?");
            prep.setBoolean(1, canceled);
            prep.setLong(2, id);
            prep.executeUpdate();
        
    } catch (SQLException e) {
        e.printStackTrace();
    }

        
    }


    @Override
    public Collection<Report> getUserReports(String username) {
        AtomicInteger reportId= new AtomicInteger();
        Map <Integer, Report> reportRepo = new HashMap<>();
        
        Statement statement;
        try {
            statement = c.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM reports WHERE username = '" + username + "'" );
            while (rs.next()){
                Report report = new Report();
                report.setId(rs.getLong("id"));
                report.setUsername(rs.getString("username"));
                report.setCanceled(rs.getBoolean("canceled"));
                report.setReport(rs.getString("report"));
                report.setTitle(rs.getString("title"));
                report.setCreationdate(rs.getString("creationdate"));
                reportRepo.put(reportId.incrementAndGet(), report);

            }
        } catch (SQLException e) {
            
            e.printStackTrace();
        }

        return reportRepo.values();
    
    }
}

