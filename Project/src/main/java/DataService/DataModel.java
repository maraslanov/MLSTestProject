package DataService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Entity Operations in the OnlineShops database
 */
public class DataModel {

    private String PartName;
    private String PartNumber;
    private String Vendor;
    private Integer Qty;
    private Date Shipped;
    private Date Receive;

    public DataModel(String partNumber, String partName, String vendor, Integer qty, Date shipped, Date receive) {
        PartName = partName;
        PartNumber = partNumber;
        Vendor = vendor;
        Qty = qty;
        Shipped = shipped;
        Receive = receive;
    }
    public DataModel(String partNumber, String partName, String vendor, String qty, String shipped, String receive) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        PartName = partName;
        PartNumber = partNumber;
        Vendor = vendor;
        try {
            if (!qty.equals("")) Qty = Integer.parseInt(qty); else Qty=null;
            if (!shipped.equals(""))Shipped = formatter.parse(shipped);else Shipped=null;
            if (!receive.equals(""))Receive = formatter.parse(receive);else Receive=null;
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getPartNumber() {
        return PartNumber;
    }

    public String getPartName() {
        return PartName;
    }


    public String getVendor() {
        return Vendor;
    }

    public Integer getQty() {
        return Qty;
    }

    public Date getShipped() {
        return Shipped;
    }

    public Date getReceive() {
        return Receive;
    }

    public void setPartName(String partName) {
        PartName = partName;
    }

    public void setPartNumber(String partNumber) {
        PartNumber = partNumber;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public void setQty(Integer qty) {
        Qty = qty;
    }

    public void setShipped(Date shipped) {
        Shipped = shipped;
    }

    public void setReceive(Date receive) {
        Receive = receive;
    }
}