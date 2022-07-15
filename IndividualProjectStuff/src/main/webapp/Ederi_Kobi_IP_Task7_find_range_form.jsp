<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Enter a Range for Category</title>
    </head>
    <body>
        <h2>Enter Range</h2>
        <!--
            Form for collecting user input for the new movie_night record.
            Upon form submission, add_movie.jsp file will be invoked.
        -->
        <form action="Ederi_Kobi_IP_Task7_find_range.jsp">
            <!-- The form organized in an HTML table for better clarity. -->
            <table border=1>
                <tr>
                    <th colspan="2">Enter the Min and Max Values:</th>
                </tr>
                <tr>
                    <td>Min Value:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=min>
                    </div></td>
                </tr>
                <tr>
                    <td>Max Value:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=max>
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
