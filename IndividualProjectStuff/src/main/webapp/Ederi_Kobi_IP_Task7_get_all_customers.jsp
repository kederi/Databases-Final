<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
    <meta charset="UTF-8">
        <title>Customers</title>
    </head>
    <body>
        <%@page import="IndividualProjectStuff.Ederi_Kobi_IP_Task7"%>
        <%@page import="java.sql.ResultSet"%>
        <%
            // We instantiate the data handler here, and get all the movies from the database
            final Ederi_Kobi_IP_Task7 handler = new Ederi_Kobi_IP_Task7();
            final ResultSet cust = handler.getAllCustomers();
        %>
        <!-- The table for displaying all the movie records -->
        <table cellspacing="2" cellpadding="2" border="1">
            <tr> <!-- The table headers row -->
              <td align="center">
                <h4>Name</h4>
              </td>
              <td align="center">
                <h4>Address</h4>
              </td>
              <td align="center">
                <h4>Category</h4>
              </td>
            </tr>
            <%
               while(cust.next()) { // For each movie_night record returned...
                   // Extract the attribute values for every row returned
                   final String name = cust.getString("cust_name");
                   final String addy = cust.getString("cust_address");
                   final String cat = cust.getString("cust_category");
                   
                   out.println("<tr>"); // Start printing out the new table row
                   out.println( // Print each attribute value
                        "</td><td align=\"center\"> " + name +
                        "</td><td align=\"center\"> " + addy +
                        "</td><td align=\"center\"> " + cat + "</td>");
                   out.println("</tr>");
               }
               %>
          </table>
    </body>
</html>
