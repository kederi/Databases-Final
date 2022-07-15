
<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Result</title>
</head>
    <body>
    <%@page import="IndividualProjectStuff.Ederi_Kobi_IP_Task7"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    <%
    	final ResultSet callVals;
    %>

    <% 
    // The handler is the one in charge of establishing the connection.
    Ederi_Kobi_IP_Task7 handler = new Ederi_Kobi_IP_Task7();

    // Get the attribute values passed from the input form.
    String min = request.getParameter("min");
    String max = request.getParameter("max");


    /*
     * If the user hasn't filled out all the time, movie name and duration. This is very simple checking.
     */
    if (min.equals("") || max.equals("")) {
        response.sendRedirect("Ederi_Kobi_IP_Task7_find_range_form.jsp");
    } 
    else 
    {
        int minF = Integer.parseInt(min);
		int maxF = Integer.parseInt(max);
        // Now perform the query with the data from the form.
		     
        callVals = handler.getCustInRange(minF, maxF);
        %>  
        <!-- The table for displaying all the movie records -->
        <table cellspacing="2" cellpadding="2" border="1">
            <tr> <!-- The table headers row -->
              <td align="center">
                <h4>Name</h4>
              </td>
            </tr>
           <% 
               while(callVals.next()) { // For each movie_night record returned...
                   // Extract the attribute values for every row returned
                   final String name = callVals.getString("cust_name");
                   //final String addy = callVals.getString("cust_address");
                   //final String cat = callVals.getString("cust_category");
                   
                   out.println("<tr>"); // Start printing out the new table row
                   out.println( // Print each attribute value
                        "</td><td align=\"center\"> " + name +"</td>");
                   out.println("</tr>");
               }
            %>
            </table>
           
<%
    }
    %>
    </body>
</html>
