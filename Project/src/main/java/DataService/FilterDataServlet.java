package DataService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet for doPost and doGet from
 * http://localhost:8080/SimpleHTMLTableWS/
 */
@WebServlet(
        name = "selectdataservlet",
        urlPatterns = ""
)
public class FilterDataServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //getting input params from request for Select params
        String PN = req.getParameter(Consts.Filter_PartNumber);
        String PartName = req.getParameter(Consts.Filter_PartName);
        String Vendor = req.getParameter(Consts.Filter_Vendor);
        Integer Qty = null;
        Date ShippedAfter = null, ShippedBefore = null, ReceiveAfter = null, ReceiveBefore = null;
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        if (!req.getParameter(Consts.Filter_Qty).equals(""))
            Qty = Integer.parseInt(req.getParameter(Consts.Filter_Qty));
        try {
            if (!req.getParameter(Consts.Filter_Shipped_After).equals(""))
                ShippedAfter = formatter.parse(req.getParameter(Consts.Filter_Shipped_After));
            if (!req.getParameter(Consts.Filter_Shipped_Before).equals(""))
                ShippedBefore = formatter.parse(req.getParameter(Consts.Filter_Shipped_Before));
            if (!req.getParameter(Consts.Filter_Received_After).equals(""))
                ReceiveAfter = formatter.parse(req.getParameter(Consts.Filter_Shipped_After));
            if (!req.getParameter(Consts.Filter_Received_Before).equals(""))
                ReceiveBefore = formatter.parse(req.getParameter(Consts.Filter_Received_Before));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //getting data from db
        DataModel[] values = getDataModels(PN, PartName, Vendor, Qty, ShippedAfter, ShippedBefore, ReceiveAfter, ReceiveBefore);
        req.setAttribute("DataArray", values);
        //forward from servlet to a JSP (for filling table)
        RequestDispatcher view = req.getRequestDispatcher("index.jsp");
        view.forward(req, resp);
    }

    private DataModel[] getDataModels(String PN, String partName, String vendor, Integer qty, Date shippedAfter, Date shippedBefore, Date receiveAfter, Date receiveBefore) {
        DBService dbService = new DBService();
        return dbService.getDataFromShops(PN, partName, vendor, qty, shippedAfter, shippedBefore, receiveAfter, receiveBefore);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("index.html");
        view.forward(req, resp);
    }
}