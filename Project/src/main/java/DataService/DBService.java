package DataService;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service for SQL queries
 */
public class DBService {
    private Connection dbcon;

    private static Logger log = Logger.getLogger(DataModel.class.getName());

    //creating connection with db
    public Connection LoadDriver() throws ServletException {
        Properties props = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream input = classLoader.getResourceAsStream("db.properties");
        //get connection properties from file
        try {
            props.load(input);
        } catch (IOException e) {
            log.log(Level.SEVERE, "IOException: ", e);
        }
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");
        String driver = props.getProperty("jdbc.driver");
        Connection dbcon;
        //attempt to create a connection
        try {
            Class.forName(driver);
            dbcon = DriverManager.getConnection(url, username, password);
            return dbcon;
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "ClassNotFoundException: ", e);
            throw new ServletException("Class not found Error ", e);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException: ", e);
        }
        return null;
    }

    public Connection LoadDriver(String url, String username, String password, String driver) throws ServletException {
        Connection dbcon;
        //attempt to create a connection
        try {
            Class.forName(driver);
            dbcon = DriverManager.getConnection(url, username, password);
            return dbcon;
        } catch (ClassNotFoundException e) {
            log.log(Level.SEVERE, "ClassNotFoundException: ", e);
            throw new ServletException("Class not found Error ", e);
        } catch (SQLException e) {
            log.log(Level.SEVERE, "SQLException: ", e);
        }
        return null;
    }

    public String GenerateSelectStatement(String PartNumber, String PartName, String Vendor, Integer Qty, Date ShippedAfter, Date ShippedBefore, Date ReceiveAfter, Date ReceiveBefore) {
        String str = "SELECT " + Consts.partNumber + "," + Consts.partName + "," + Consts.vendor + "," + Consts.qty + "," + Consts.shipped + "," + Consts.received + " from " + Consts.operations_DataTable;
        if ((PartNumber != null) && (PartName != null) && (Vendor != null))
            if ((!PartNumber.equals("")) || (!PartName.equals("")) || (!Vendor.equals("")) || (Qty != null) || (ShippedAfter != null) || (ShippedBefore != null) || (ReceiveAfter != null) || (ReceiveBefore != null)) {
                str = str + " Where ";
                boolean needAnd = false;
                if (!PartNumber.equals("")) {
                    str = str + Consts.partNumber + " like ?";
                    needAnd = true;
                }
                if (!PartName.equals("")) {
                    if (needAnd) str = str + " and ";
                    str = str + Consts.partName + " like ?";
                    needAnd = true;
                }
                if (!Vendor.equals("")) {
                    if (needAnd) str = str + " and ";
                    str = str + Consts.vendor + " like ?";
                    needAnd = true;
                }
                if (Qty != null) {
                    if (needAnd) str = str + " and ";
                    str = str + Consts.qty + ">=?";
                    needAnd = true;
                }
                if (ShippedAfter != null) {
                    if (needAnd) str = str + " and ";
                    str = str + Consts.shipped + ">=?";
                    needAnd = true;
                }
                if (ShippedBefore != null) {
                    if (needAnd) str = str + " and ";
                    str = str + Consts.shipped +  "<=?";
                    needAnd = true;
                }
                if (ReceiveAfter != null) {
                    if (needAnd) str = str + " and ";
                    str = str + Consts.received +  ">=?";
                    needAnd = true;
                }
                if (ReceiveBefore != null) {
                    if (needAnd) str = str + " and ";
                    str = str + Consts.received +  "<=?";
                }
            }
        str = str + ";";
        return str;
    }

    //getting array of objects from db.properties, input params - data from html form
    public DataModel[] getDataFromShops(String partNumber, String partName, String vendor, Integer qty, Date shipped1, Date shipped2, Date receive1, Date receive2) {
        DataModel[] result;
        try {
            dbcon = LoadDriver();
            PreparedStatement pStatement = dbcon.prepareStatement(GenerateSelectStatement(partNumber, partName, vendor, qty, shipped1, shipped2, receive1, receive2),
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = null;
            int i = 1;
            //filling PreparedStatement params
            if (!partNumber.equals("")) pStatement.setString(i++, "%"+partNumber+"%");
            if (!partName.equals("")) pStatement.setString(i++, "%"+partName+"%");
            if (!vendor.equals("")) pStatement.setString(i++, "%"+vendor+"%");
            if (qty!=null) pStatement.setInt(i++, qty);
            if (shipped1 !=null) pStatement.setDate(i++, new java.sql.Date(shipped1.getTime()));
            if (shipped2 !=null) pStatement.setDate(i++, new java.sql.Date(shipped2.getTime()));
            if (receive1 !=null) pStatement.setDate(i++, new java.sql.Date(receive1.getTime()));
            if (receive2 !=null) pStatement.setDate(i++, new java.sql.Date(receive2.getTime()));
            rs=pStatement.executeQuery();
            //geting resultset count
            int number=0;
            try {
                rs.last();
                number = rs.getRow();
                rs.beforeFirst();
            }catch(SQLException e) {
                log.log(Level.SEVERE, "SQLException: ", e);
            }
            //creating array from resultset
            result = new DataModel[number];
            number=0;
            while (rs.next()) {
                DataModel DM = new DataModel(rs.getString(Consts.partNumber),
                        rs.getString(Consts.partName),
                        rs.getString(Consts.vendor),
                        rs.getInt(Consts.qty),
                        rs.getDate(Consts.shipped),
                        rs.getDate(Consts.received));
                result[number] = DM;
                number++;
            }
            dbcon.close();
            return result;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: ", e);
            return null;
        }
    }

    public DataModel[] getDataFromShops2(String partNumber, String partName, String vendor, Integer qty, Date shipped1, Date shipped2, Date receive1, Date receive2) {
        DataModel[] result;
        try {
            dbcon = LoadDriver("jdbc:postgresql://localhost/OnlineShop", "postgres" ,"123","org.postgresql.Driver" );
            PreparedStatement pStatement = dbcon.prepareStatement(GenerateSelectStatement(partNumber, partName, vendor, qty, shipped1, shipped2, receive1, receive2),
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = null;
            int i = 1;
            //filling PreparedStatement params
            if (!partNumber.equals("")) pStatement.setString(i++, "%"+partNumber+"%");
            if (!partName.equals("")) pStatement.setString(i++, "%"+partName+"%");
            if (!vendor.equals("")) pStatement.setString(i++, "%"+vendor+"%");
            if (qty!=null) pStatement.setInt(i++, qty);
            if (shipped1 !=null) pStatement.setDate(i++, new java.sql.Date(shipped1.getTime()));
            if (shipped2 !=null) pStatement.setDate(i++, new java.sql.Date(shipped2.getTime()));
            if (receive1 !=null) pStatement.setDate(i++, new java.sql.Date(receive1.getTime()));
            if (receive2 !=null) pStatement.setDate(i++, new java.sql.Date(receive2.getTime()));
            rs=pStatement.executeQuery();
            //geting resultset count
            int number=0;
            try {
                rs.last();
                number = rs.getRow();
                rs.beforeFirst();
            }catch(SQLException e) {
                log.log(Level.SEVERE, "SQLException: ", e);
            }
            //creating array from resultset
            result = new DataModel[number];
            number=0;
            while (rs.next()) {
                DataModel DM = new DataModel(rs.getString(Consts.partNumber),
                        rs.getString(Consts.partName),
                        rs.getString(Consts.vendor),
                        rs.getInt(Consts.qty),
                        rs.getDate(Consts.shipped),
                        rs.getDate(Consts.received));
                result[number] = DM;
                number++;
            }
            dbcon.close();
            return result;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: ", e);
            return null;
        }
    }

}
