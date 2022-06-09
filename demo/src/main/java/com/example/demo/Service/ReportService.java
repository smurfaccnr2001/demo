package com.example.demo.Service;

import java.sql.SQLException;
import java.util.Collection;


import com.example.demo.Model.Report;

public interface ReportService {
    public abstract void createReport(Report report) throws SQLException;
    public abstract void updateReport(long id ,boolean canceled);
    public abstract Collection<Report> getUserReports(String username);


}
