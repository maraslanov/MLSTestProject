<%@ page import ="java.util.*" %>
<%@page import="DataService.DataModel"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Example Of Interface</title>
<style>
table {
    border-spacing: 0;
    width: 100%;
    border: 1px solid #ddd;
}

th {
    cursor: pointer;
}

th, td {
    text-align: left;
    padding: 16px;
    border: 1px solid black;
}
tr:nth-child(even) {
    background-color: #f2f2f2
}
</style>
</head>
<body>
<form method="post" action="">
<table id="FilterTable"align="left" border="1" cellpadding="1" cellspacing="1" style="width: 600px">
	<thead>
		<tr>
			<th scope="col">Filter</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>PN&nbsp;&nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<input name="PNTF" type="text" value=<%= request.getParameter("PNTF")%> ></td>
		</tr>
		<tr>
			<td>Part Name&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<input name="PartNameTF" type="text" value=<%= request.getParameter("PartNameTF")%> ></td>
		</tr>
		<tr>
			<td>Vendor&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp;<input name="VendorTF" type="text" value=<%= request.getParameter("VendorTF")%> ></td>
		</tr>
		<tr>
			<td>Qty&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<input placeholder="input number" name="QtyTF" type="number" pattern="[0-9]{,11}" value=<%= request.getParameter("QtyTF")%> ></td>
		</tr>
		<tr>
			<td>Shipped&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; after&nbsp;<input placeholder="input date MMM dd,yyyy" name="ShippedTF1" size="15" type="text" value=<%= request.getParameter("ShippedTF1")%> >&nbsp;before&nbsp;<input placeholder="input date MMM dd,yyyy" name="ShippedTF2" size="15" type=<%= request.getParameter("ShippedTF2")%> ></td>
		</tr>
		<tr>
			<td>Recieved&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; after&nbsp;<input placeholder="input date MMM dd,yyyy" name="RecievedTF1" size="15" type="text" value=<%= request.getParameter("RecievedTF1")%> >&nbsp;before&nbsp;<input placeholder="input date MMM dd,yyyy" name="RecievedTF2" size="15" type="text" value=<%= request.getParameter("RecievedTF2")%> ></td>
		</tr>
	</tbody>
</table>
  <br><br>
  <input type="submit">
</form>
<table id="MyTable">
  <tr>
    <th onclick="sortTable(0)">PN</th>
    <th onclick="sortTable(1)">Part Name</th>
    <th onclick="sortTable(2)">Vendor</th>
    <th onclick="sortTable(3)">Qty</th>
    <th onclick="sortTable(4)">Shipped</th>
    <th onclick="sortTable(5)">Recieved</th>
  </tr>
  <%if (request.getAttribute("DataArray") != null) {
      for (DataModel item : (DataModel[]) request.getAttribute("DataArray")) {
                    if ((item.getShipped()!=null)&&(item.getReceive()!=null))
                        out.println("<tr>"+"<td>"+item.getPartNumber()+"</td>"+"<td>"+item.getPartName()+"</td>"+"<td>"+item.getVendor()+"</td>"+"<td>"+item.getQty()+"</td>"+"<td>"+item.getShipped()+"</td>"+"<td>"+item.getReceive()+"</td>"+"</tr>");
                    if ((item.getShipped()!=null)&&(item.getReceive()==null))
                         out.println("<tr>"+"<td>"+item.getPartNumber()+"</td>"+"<td>"+item.getPartName()+"</td>"+"<td>"+item.getVendor()+"</td>"+"<td>"+item.getQty()+"</td>"+"<td>"+item.getShipped()+"</td>"+"<td>"+""+"</td>"+"</tr>");
                    if ((item.getShipped()==null)&&(item.getReceive()!=null))
                         out.println("<tr>"+"<td>"+item.getPartNumber()+"</td>"+"<td>"+item.getPartName()+"</td>"+"<td>"+item.getVendor()+"</td>"+"<td>"+item.getQty()+"</td>"+"<td>"+""+"</td>"+"<td>"+item.getReceive()+"</td>"+"</tr>");
                    if ((item.getShipped()==null)&&(item.getReceive()==null))
                         out.println("<tr>"+"<td>"+item.getPartNumber()+"</td>"+"<td>"+item.getPartName()+"</td>"+"<td>"+item.getVendor()+"</td>"+"<td>"+item.getQty()+"</td>"+"<td>"+""+"</td>"+"<td>"+""+"</td>"+"</tr>");
      }
   }
  %>
</table>
<script>
function sortTable(n) {
  var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
  table = document.getElementById("MyTable");
  switching = true;
  dir = "asc";
  while (switching) {
    switching = false;
    rows = table.getElementsByTagName("TR");
    for (i = 1; i < (rows.length - 1); i++) {
      shouldSwitch = false;
      x = rows[i].getElementsByTagName("TD")[n];
      y = rows[i + 1].getElementsByTagName("TD")[n];
      if (dir == "asc") {
        if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
          shouldSwitch= true;
          break;
        }
      } else if (dir == "desc") {
        if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
          shouldSwitch= true;
          break;
        }
      }
    }
    if (shouldSwitch) {
      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
      switching = true;
      switchcount ++;
    } else {
      if (switchcount == 0 && dir == "asc") {
        dir = "desc";
        switching = true;
      }
    }
  }
}
</script>
</body>
</html>
