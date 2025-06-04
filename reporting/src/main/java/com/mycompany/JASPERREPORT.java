package com.mycompany;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JASPERREPORT {

    private static final String destFileName = "InstructorReport.pdf"; // Nama file hasil output

    public static void main(String[] args) {
        System.out.println("Generating Jasper report...");

        try {
            JasperReport jasperReport = getJasperReport();

            Map<String, Object> parameters = getParameters();

            // Register driver JDBC SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl =
                    "jdbc:sqlserver://localhost:1433;" +
                            "databaseName=University2;" +
                            "user=sa;" +
                            "password=123456789;" +
                            "trustServerCertificate=true;" +
                            "loginTimeout=30;";

            try (Connection con = DriverManager.getConnection(connectionUrl)) {

                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);

                JasperViewer.viewReport(jasperPrint, false);

                // Export ke PDF
                JasperExportManager.exportReportToPdfFile(jasperPrint, destFileName);
                System.out.println("Laporan berhasil dibuat: " + destFileName);
            }

        } catch (JRException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    private static JasperReport getJasperReport() throws JRException {
        File template = Paths.get("src", "main", "resources", "Rezareport.jrxml").toFile();


        if (!template.exists()) {
            throw new JRException("File template tidak ditemukan: " + template.getAbsolutePath());
        }

        return JasperCompileManager.compileReport(template.getAbsolutePath());
    }


    private static Map<String, Object> getParameters() {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Putra");
        return parameters;
    }
}
