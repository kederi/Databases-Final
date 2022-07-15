<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Add Customer into Database</title>
</head>
    <body>

    <%@page import="IndividualProjectStuff.Ederi_Kobi_IP_Task7"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    <% 
    // The handler is the one in charge of establishing the connection.
    Ederi_Kobi_IP_Task7 handler = new Ederi_Kobi_IP_Task7();

    // Get the attribute values passed from the input form.
    String cName = request.getParameter("cust_name");
    String cAddress = request.getParameter("cust_address");
    String cCategory = request.getParameter("cust_category");

    /*
     * If the user hasn't filled out all the time, movie name and duration. This is very simple checking.
     */
     if (cName.equals("") || cAddress.equals("") || cCategory.equals("")) 
     {
         response.sendRedirect("Ederi_Kobi_Task7_add_customer_form.jsp");
     }
     else
     {
        int duration = Integer.parseInt(cCategory);
        
        // Now perform the query with the data from the form.
        boolean success = handler.addCustomer(cName, cAddress, duration);
        if (!success) 
        { // Something went wrong
            %>
                <h2>There was a problem inserting the course</h2>
            <%
        } else 
        { // Confirm success to the user
            %>
            <h2>The Customer:</h2>

            <ul>
                <li>Customer Name: <%=cName%></li>
                <li>Customer Address: <%=cAddress%></li>
                <li>Category: <%=cCategory%></li>
            </ul>

          
            <h2>Was successfully inserted.</h2> 
            <%  
        }
     }
    %>
    
    </body>
</html>
