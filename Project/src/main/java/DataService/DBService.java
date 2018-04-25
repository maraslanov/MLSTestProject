package DataService;

import javax.servlet.ServletException;
import java.sql.*;
import java.util.Date;

/**
 * Service for SQL queries
 */
public class DBService {
    private String urlString = "jdbc:postgresql://localhost/OnlineShop";
    private String username = "postgres";
    private String password = "123";
    private Connection dbcon;

    public Connection LoadDriver(String UrlString, String username, String password) throws ServletException {
        Connection dbcon;
        try {
            Class.forName("org.postgresql.Driver");
            dbcon = DriverManager.getConnection(UrlString, username, password);
            return dbcon;
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundException: " + e.getMessage());
            throw new ServletException("Class not found Error", e);
        } catch (SQLException e) {
            System.err.println("SQLException: " + e.getMessage());
        }
        return null;
    }

    public String GenerateSelectStatement(String PartNumber, String PartName, String Vendor, Integer Qty, Date ShippedAfter , Date ShippedBefore, Date ReceiveAfter, Date ReceiveBefore) {
        String str = "SELECT "+ Consts.partNumber + "," + Consts.partName + "," + Consts.vendor + "," + Consts.qty + "," + Consts.shipped + "," + Consts.received + " from "+Consts.operations_DataTable;
        if ((PartNumber!=null)&&(PartName!=null)&&(Vendor!=null))
        if ((!PartNumber.equals("")) || (!PartName.equals("")) || (!Vendor.equals("")) || (Qty != null) || (ShippedAfter != null) || (ShippedBefore != null) || (ReceiveAfter != null) || (ReceiveBefore != null)) {
            str = str + " Where ";
            boolean needAnd=false;
            if (!PartNumber.equals("")){
                str = str + Consts.partNumber + " like '%" + PartNumber + "%'";
                needAnd=true;
            }
            if (!PartName.equals("")) {
                if (needAnd) str = str + " and ";
                str = str +  Consts.partName + " like '%" + PartName + "%'";
                needAnd=true;
            }
            if (!Vendor.equals("")) {
                if (needAnd) str = str + " and ";
                str = str + Consts.vendor + " like '%" + Vendor + "%'";
                needAnd=true;
            }
            if (Qty != null) {
                if (needAnd) str = str + " and ";
                str = str + Consts.qty + ">=" + Qty;
                needAnd=true;
            }
            if (ShippedAfter != null) {
                if (needAnd) str = str + " and ";
                str = str +Consts.shipped + ">='" + ShippedAfter + "'";
                needAnd=true;
            }
            if (ShippedBefore != null) {
                if (needAnd) str = str + " and ";
                str = str + Consts.shipped + "<='" + ShippedBefore + "'";
                needAnd=true;
            }
            if (ReceiveAfter != null) {
                if (needAnd) str = str + " and ";
                str = str + Consts.received + ">='" + ReceiveAfter + "'";
                needAnd=true;
            }
            if (ReceiveBefore != null) {
                if (needAnd) str = str + " and ";
                str = str + Consts.received + "<='" + ReceiveBefore + "'";
            }
        }
        str = str + ";";
        return str;
    }

    //Executing Select Statement
    public DataModel[] getDataFromShops(String partNumber, String partName, String vendor, Integer qty, Date shipped1, Date shipped2, Date receive1, Date receive2) {
        DataModel[] result;
        try {
            dbcon = LoadDriver(urlString, username, password);
            Statement statement = dbcon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);
            String query = GenerateSelectStatement(partNumber, partName, vendor, qty, shipped1, shipped2, receive1, receive2);
            ResultSet rs = statement.executeQuery(query);
            int rowcount = 0;
            if (rs.last()) {
                rowcount = rs.getRow();
                rs.beforeFirst();
            }
            result = new DataModel[rowcount];
            rowcount = 0;
            while (rs.next()) {
                DataModel DM = new DataModel(rs.getString(Consts.partNumber),
                        rs.getString(Consts.partName),
                        rs.getString(Consts.vendor),
                        rs.getInt(Consts.qty),
                        rs.getDate(Consts.shipped),
                        rs.getDate(Consts.received));
                result[rowcount] = DM;
                rowcount++;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
