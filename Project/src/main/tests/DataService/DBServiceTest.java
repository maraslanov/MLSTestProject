
package DataService;

import junit.framework.Assert;
import org.junit.Test;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DBServiceTest extends DBService {
    @Test
    public void GenerateSelectStatementSuccess() {
        String str = GenerateSelectStatement("PN", "PartName", "Vendor", 60, null, null, null, null);
        String actual="SELECT partnumber,partname,vendor,qty,shipped,received from operations Where partnumber like '%PN%' and partname like '%PartName%' and vendor like '%Vendor%' and qty>=60;";
        Assert.assertEquals(str,actual);
    }

    @Test
    public void GenerateSelectStatementNoInput() {
        String str = GenerateSelectStatement(null, null, null, 60, null, null, null, null);
        String actual="SELECT partnumber,partname,vendor,qty,shipped,received from operations;";
        Assert.assertEquals(str,actual);
    }

    @Test
    public void getDataFromShopsSuccess(){
        //todo запилить схему для тестирования в бд
        String partNumber="56H212-01", partName="HPC Blade 7", vendor="CH-DAL";
        String qty="64";
        String shipped1="", shipped2="", receive1="Feb 06, 2007",  receive2="May 05, 2007";
        DataModel[] testdm=new DataModel[1];
        DataModel exampledm=new DataModel(partNumber, partName,  vendor,  qty,  "Feb 06, 2007" , "Mar 06, 2007");
        testdm[0]=exampledm;
        Integer Qty = null;
        Date ShippedAfter = null, ShippedBefore = null, ReceiveAfter = null, ReceiveBefore = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        if (!qty.equals(""))
            Qty = Integer.parseInt(qty);
        try {
            if (!shipped1.equals(""))
                ShippedAfter = formatter.parse(shipped1);
            if (!shipped2.equals(""))
                ShippedBefore = formatter.parse(shipped2);
            if (!receive1.equals(""))
                ReceiveAfter = formatter.parse(receive1);
            if (!receive2.equals(""))
                ReceiveBefore = formatter.parse(receive2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DataModel[] realdm=getDataFromShops2(partNumber, partName,  vendor,  Qty,  ShippedAfter ,  ShippedBefore,  ReceiveAfter,  ReceiveBefore);
        Assert.assertEquals(testdm[0].getPartName(),realdm[0].getPartName());
        Assert.assertEquals(testdm[0].getPartNumber(),realdm[0].getPartNumber());
        Assert.assertEquals(testdm[0].getVendor(),realdm[0].getVendor());
        Assert.assertEquals(testdm[0].getQty(),realdm[0].getQty());
        SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            realdm[0].setShipped(newformat.parse(realdm[0].getShipped().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(testdm[0].getShipped(),realdm[0].getShipped());
        try {
            realdm[0].setReceive(newformat.parse(realdm[0].getReceive().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Assert.assertEquals(testdm[0].getReceive(),realdm[0].getReceive());
    }

}