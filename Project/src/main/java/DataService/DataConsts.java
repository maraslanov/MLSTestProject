package DataService;

public interface DataConsts {
    String operations_DataTable ="operations";

    String partNumber = "partnumber";
    String partName = "partname";
    String vendor = "vendor";
    String qty = "qty";
    String shipped = "shipped";
    String received = "received";

    String partNumberfield = operations_DataTable+"."+partNumber;
    String partNamefield = operations_DataTable+"."+partName;
    String vendorfield = operations_DataTable+"."+vendor;
    String qtyfield = operations_DataTable+"."+qty;
    String shippedfield = operations_DataTable+"."+shipped;
    String receivedfield = operations_DataTable+"."+received;
}
