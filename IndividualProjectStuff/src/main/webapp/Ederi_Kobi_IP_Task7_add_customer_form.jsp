<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add Customer</title>
    </head>
    <body>
        <h2>Add Customer</h2>
        <!--
            Form for collecting user input for the new movie_night record.
            Upon form submission, add_movie.jsp file will be invoked.
        -->
        <form action="Ederi_Kobi_IP_Task7_add_customer.jsp">
            <!-- The form organized in an HTML table for better clarity. -->
            <table border=1>
                <tr>
                    <th colspan="2">Enter the Customer:</th>
                </tr>
                <tr>
                    <td>Customer Name:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=cust_name>
                    </div></td>
                </tr>
                <tr>
                    <td>Customer Address:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=cust_address>
                    </div></td>
                </tr>
                <tr>
                    <td>Customer Category:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=cust_category>
                    </div></td>
                </tr>
                <tr>
                    <td><div style="text-align: center;">
                    <input type=reset value=Clear>
                    </div></td>
                    <td><div style="text-align: center;">
                    <input type=submit value=Insert>
                    </div></td>
                </tr>
            </table>
        </form>
    </body>
</html>
