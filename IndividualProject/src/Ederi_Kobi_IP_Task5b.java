
	import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
	import java.sql.Statement;
	import java.util.Scanner;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;

	public class Ederi_Kobi_IP_Task5b {

	    // Database credentials
	    final static String HOSTNAME = "eder0000-sql-server.database.windows.net";
	    final static String DBNAME = "cs-dsa-4513-sql-db";
	    final static String USERNAME = "eder0000";
	    final static String PASSWORD = "Thinkdifferent#4";

	    // Database connection string
	    final static String URL = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
	            HOSTNAME, DBNAME, USERNAME, PASSWORD);

	    // Query templates
	    final static String QUERY_TEMPLATE_1 = "INSERT INTO Customer " + 
	                                           "VALUES (?, ?, ?);";

	    final static String QUERY_TEMPLATE_2 = "INSERT INTO Department"
	    									+ " VALUES(?, ?);";
        final static String QUERY_TEMPLATE_3 = "INSERT INTO Process (process_ID, process_data, dnum) "+
												"VALUES (?, ?, ?);";
        final static String QUERY_TEMPLATE_3a ="INSERT INTO Paint (pid, paint_type, painting_method)"+
												"VALUES (?, ?, ?);";
        final static String QUERY_TEMPLATE_3b ="INSERT INTO Fit (pid, fit_type)"+
												"VALUES (?, ?);";
        final static String QUERY_TEMPLATE_3c ="INSERT INTO Cut (pid, cut_type)"+
												"VALUES (?, ?);";
        final static String QUERY_TEMPLATE_4 ="INSERT INTO Assembly (assembly_ID, date_ordered, assembly_details, custname)"+
											  "VALUES (?, ?, ?, ?);";
        final static String QUERY_TEMPLATE_4a ="INSERT INTO Manufactures (aid, pid, proctypes)"+
        										"VALUES (?, ?, ?);";
        final static String QUERY_TEMPLATE_5 ="INSERT INTO Account (acct_num, date_est)"+
												"VALUES (?, ?);";
        final static String QUERY_TEMPLATE_5a="INSERT INTO Process_Account(acctno, pid, process_cost)"+
        										"VALUES (?, ?, ?);";
        final static String QUERY_TEMPLATE_5b="INSERT INTO Assembly_Account(acctno, aid, assembly_cost)"+
												"VALUES (?, ?, ?);";
        final static String QUERY_TEMPLATE_5c="INSERT INTO Department_Account(acctno, dnum, department_cost)"+
												"VALUES (?, ?, ?);";
        final static String QUERY_TEMPLATE_6="INSERT INTO Job(job_no, date_commence, aid, pid)"+
												"VALUES (?, ?, ?, ?);";
        final static String QUERY_TEMPLATE_7="UPDATE Job SET date_complete=? WHERE job_no=?;";
        
        final static String QUERY_TEMPLATE_7a ="INSERT INTO PaintJob (jobno, color, volume, labortime)"+
												"VALUES (?, ?, ?, ?);";
        final static String QUERY_TEMPLATE_7b ="INSERT INTO CutJob (jobno, machine_type, machine_time, materials_used, cut_time)"+
												"VALUES (?, ?, ?, ?, ?);";
        final static String QUERY_TEMPLATE_7c ="INSERT INTO FitJob (jobno, labortime)"+
												"VALUES (?, ?);";
        final static String QUERY_TEMPLATE_8 ="INSERT INTO Transactiontab(trans_num, sup_cost, jobno)"+
        										"VALUES (?, ?, ?);";
        final static String QUERY_TEMPLATE_8a="UPDATE Account SET transno=? WHERE acct_num=?;"+
        		                              "UPDATE Process_Account SET process_cost= (process_cost + ?) WHERE acctno=?;"+
        									  "UPDATE Assembly_Account SET assembly_cost=(assembly_cost + ?) WHERE acctno=?;"+
        									  "UPDATE Department_Account SET department_cost=(department_cost + ?) WHERE acctno=?;";
        
        /**TODO**/
        //Rest of 8, 10-12
       /* final static String QUERY_TEMPLATE_10="SELECT process_ID FROM Process WHERE dept_num=? AS piroc"+
        									  "SELECT job_no FROM Job WHERE pid = piroc AND date_complete=? AS jn" +
        									  "SELECT labortime FROM PaintJob WHERE jobno = jn AS ptim"+
        									  "SELECT cut_time FROM CutJob WHERE jobno = jn AS ctim"+
        									  "SELECT labortime FROM FitJob WHERE jobno = jn AS ftim"+
        									  "ptim + ctim + ftim";*/
        									  
        									  
        
        final static String QUERY_TEMPLATE_15 ="UPDATE PaintJob SET color=? WHERE jobno=?";
        
        //final static String QUERY_TEMPLATE_13="SELECT cust_name FROM Customer WHERE (cust_category >= min=? AND cust_category <= max=?;";
        
        //final static String QUERY_TEMPLATE_9 ="SELECT assembly_cost FROM Assembly_Account WHERE aid=?";
	    // User input prompt//
	    final static String PROMPT = 
	            "\nPlease select one of the options below: \n" +
	              "(1)  Enter a new customer  \n" +  
	              "(2)  Enter a new department  \n" +
	              "(3)  Enter a new process-id and its department together with its type and information relevant to \r\n"
	              + "the type  \n" +
	              "(4)  Enter  a  new  assembly  with  its  customer-name,  assembly-details,  assembly-id,  and  date-\r\n"
	              + "ordered and associate it with one or more processes  \n" +
	              "(5)  Create a new account and associate it with the process, assembly, or department to which it is \r\n"
	              + "applicable \n" +
	              "(6)  Enter  a  new  job,  given  its  job-no,  assembly-id,  process-id,  and  date  the  job  commenced  \n" +
	              "(7)  At the completion of a job, enter the date it completed and the information relevant to the type \r\n"
	              + "of job  \n" +
	              "(8)  Enter  a  transaction-no  and  its  sup-cost  and  update  all  the  costs  (details)  of  the  affected \r\n"
	              + "accounts by adding sup-cost to their current values of details \n" +
	              "(9)  Retrieve the total cost incurred on an assembly-id  \n" +
	              "(10) Retrieve the total labor time within a department for jobs completed in the department during a \r\n"
	              		 + "given date  \n" +
	              "(11) Retrieve  the  processes  through  which  a  given  assembly-id  has  passed  so  far  (in  date-\r\n"
	              		+ "commenced order) and the department responsible for each process \n" +
	              "(12) Retrieve  the  jobs  (together  with  their  type  information  and  assembly-id)  completed  during  a \r\n"
	              		+ "given date in a given department \n" +
	              "(13) Retrieve the customers (in name order) whose category is in a given range \n" +
	              "(14) Delete all cut-jobs whose job-no is in a given range \n" +
	              "(15) Change the color of a given paint job \n" +
	              "(16) Import: Enter new customers from a data file until the file is empty (the user is asked input"
	              + "file name). \n" +
	              "(17) Export: Retrieve the customers (in name order) whose category is in a given range and \r\n"
	              + "output them to a data file instead of screen (the user must be asked to enter the output file \r\n"
	              + "name). \n" +
	              "(18) Quit \n";
	            		 
	    public static void main(String[] args) throws SQLException, IOException {

	        System.out.println("Welcome to the sample application!");

	        final Scanner sc = new Scanner(System.in); // Scanner is used to collect the user input
	        String option = ""; // Initialize user option selection as nothing
	        while (!option.equals("18")) { // As user for options until option 3 is selected
	            System.out.println(PROMPT); // Print the available options
	            option = sc.next(); // Read in the user option selection
	            
	            /**First query on the table which just enters a new Customer with customer name, address 
	             * and category
	             */
	            switch (option) { // Switch between different options
	                case "1": // Insert a Customer into the Database
	                    // Collect the new student data from the user
	                    System.out.println("Please enter a Customer Name:");
	                    sc.nextLine();
	                    final String name = sc.nextLine(); // Read in the user input of Customer Name
	                    
	                
	                    System.out.println("Please enter a Customer Address:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    final String address = sc.nextLine(); // Read in user input of a Customer Address

	                    System.out.println("Please enter Customer category:");
	                    // No need to call nextLine extra time here, because the preceding nextLine consumed the newline character.
	                    final int category = sc.nextInt(); // Read in user input of Customer category
	                    
	                    sc.nextLine();

	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setString(1, name);
	                        	statement.setString(2, address);
	                        	statement.setInt(3, category);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }

	                    break;
	                case "2":
	                    // Collect the new student data from the user
	                    System.out.println("Please enter a Department Num:");
	                    final int dept_num = sc.nextInt(); // Read in the user input of Department Number
	                    
	                    
	                    System.out.println("Please enter a Department Data:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    sc.nextLine();
	                    final String dept_data = sc.nextLine(); // Read in user input of a Customer Address
	                    
	                    sc.nextLine();

	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_2)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setInt(1, dept_num);
	                        	statement.setString(2, dept_data);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    break;
	                case "3": 

	                	
	                	 // Collect the new Process ID from the User
	                    System.out.println("Please enter a Process ID:");
	                    
	                    final int pid = sc.nextInt(); // Read in the user input of Process ID
	                    
	                
	                    System.out.println("Please enter a Department Number:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    sc.nextLine();
	                    final int dnum = sc.nextInt(); // Read in user input of a Department Number
	                    
	                    
	                    System.out.println("Please enter Process Type:");
	                    // No need to call nextLine extra time here, because the preceding nextLine consumed the newline character.
	                    sc.nextLine();
	                    final String ptype = sc.nextLine(); // Read in user input of a Process Type	                    
	                    
	                    

	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setInt(1, pid);
	                        	statement.setString(2, ptype);
	                        	statement.setInt(3, dnum);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    
	                    /** Block to create the Paint table query **/ 
	                    if(ptype.equals("Paint"))
	                    {
	                    	System.out.println("Please enter Paint Type:");
	                    	
	                    	//Ask user for the Paint Type
	                    	final String paintType = sc.nextLine();
	                    	
	                    	//Ask user for Paint Method
	                    	System.out.println("Please enter Painting Method:");
	                    	
	                    	final String paintMethod = sc.nextLine();
	                    	
		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3a)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, pid);
		                        	statement.setString(2, paintType);
		                        	statement.setString(3, paintMethod);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    	
	                    }
	                    
	                    /**Block to create the Fit table query**/ 
	                    if(ptype.equals("Fit"))
	                    {
	                    	System.out.println("Please enter Fit Type:");
	                    	
	                    	//Ask user for the Fit Type
	                    	final String fitType = sc.nextLine();
	                    	
		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3b)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, pid);
		                        	statement.setString(2, fitType);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    }
	                    
	                    /**Block to create the Cut table query**/
	                    if(ptype.equals("Cut"))
	                    {
	                    	System.out.println("Please enter Cut Type:");
	                    	
	                    	//Ask user for the Cut Type
	                    	final String cutType = sc.nextLine();
	                    	
		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_3c)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, pid);
		                        	statement.setString(2, cutType);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    }
	                    else 
	                    {
	                    	break;
	                    }
	                	break;
	                case "4":
	                	 // Collect the new Assembly ID from the User
	                    System.out.println("Please enter a Assembly ID:");
	                    
	                    final int assid = sc.nextInt(); // Read in the user input of Assembly ID
	                    
	                    sc.nextLine();
	                    System.out.println("Please enter a Date Created(YYYY-MM-DD):");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    
	                    final String dCreated= sc.nextLine(); // Read in user input of a Date Created
	                    
	                    
	                    System.out.println("Please enter Assembly Details:");
	                    // No need to call nextLine extra time here, because the preceding nextLine consumed the newline character.
	                    final String assDet = sc.nextLine(); // Read in user input of Assembly Details	  
	                    
	                    
	                    System.out.println("Please enter a valid Customer Name:");
	                    
	                    final String cname = sc.nextLine(); // Read in user input of Customer Name  

	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setInt(1, assid);
	                        	statement.setString(2, dCreated);
	                        	statement.setString(3, assDet);
	                        	statement.setString(4, cname);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    
	                    //After creating the assembly, must associate that with one or more processes, so I must create a few processes first
	                    // After that do a query that has a while user input = y I continue associating processes with
	                    // The assembly
	                    String userIn = "Y";
	                    String procTypes = "";
	                    while (userIn.equals("Y"))
	                    {
		                	 // Collect the new Process ID from the User
		                    System.out.println("Please enter a Process ID:");
		                    
		                    final int processid = sc.nextInt(); // Read in the user input of Process ID
		                    
		                    sc.nextLine();
		                    
		                	 // Collect the new Process Type from the User
		                    System.out.println("Please enter a Process Type:");
		                    
		                    procTypes += sc.nextLine();		                  
		                    
		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_4a)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, assid);
		                        	statement.setInt(2, processid);
		                        	statement.setString(3, procTypes);              
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
		                	 // Collect the new Process ID from the User
		                    System.out.println("Would You Like to Continue Associating Processes? (Y/N):");
		                    
		                    userIn = sc.nextLine();
		                    
	                    }

	                	break; 
	                	
	                case "5": 

	                	 // Collect the new Account Number from the User
	                    System.out.println("Please enter a Account Number:");
	                    
	                    final int acctNum = sc.nextInt(); // Read in the user input of Account Num
          
	                    System.out.println("Please enter a Date Established (YYYY-MM-DD:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    sc.nextLine();
	                    final String date_est = sc.nextLine(); // Read in user input of a Date Established
	                    
	                    
	                	 // Collect the new Account Number from the User
	                    System.out.println("Please enter a Account Type:");
	                    
	                    final String accType = sc.nextLine(); // Read in the user input of Account Type
	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5)) {
	                            // Populate the query template with the data collected from the user

	                        	statement.setInt(1, acctNum);
	                        	statement.setString(2, date_est);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    
	                    /** Block to create the Paint table query **/ 
	                    if(accType.equals("Process"))
	                    {
	                    	System.out.println("Please enter Process Costs:");
	                    	
	                    	//Ask user for the Paint Type
	                    	System.out.print("$");
	                    	final Float procCost = sc.nextFloat();
	                    	sc.nextLine();
	                    	//Ask user for existing process ID 
	                    	System.out.println("Please enter valid Process ID:");
	                    	final int procID = sc.nextInt();
	                    	sc.nextLine();
	                    	

		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5a)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, acctNum);
		                        	statement.setInt(2, procID);
		                        	statement.setFloat(3, procCost);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    	
	                    }
	                    
	                    /**Block to create the Assembly table query**/ 
	                    if(accType.equals("Assembly"))
	                    {
	                    	System.out.println("Please enter Assembly Costs:");
	                    	
	                    	//Ask user for the Assembly Costs Type
	                    	System.out.print("$");
	                    	final Float assCost = sc.nextFloat();
	                    	sc.nextLine();
	                    	//Ask user for existing process ID 
	                    	System.out.println("Please enter valid Assembly Number:");
	                    	final int assemID = sc.nextInt();
	                    	sc.nextLine();
	                    	

		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5b)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, acctNum);
		                        	statement.setInt(2, assemID);
		                        	statement.setFloat(3, assCost);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    	
	                    }
	                    
	                    /**Block to create the Department table query**/
	                    if(accType.equals("Department"))
	                    {
	                    	System.out.println("Please enter Department Costs:");
	                    	
	                    	//Ask user for the Department Costs Type
	                    	System.out.print("$");
	                    	final Float deptCost = sc.nextFloat();
	                    	sc.nextLine();
	                    	//Ask user for existing process ID 
	                    	System.out.println("Please enter valid Department Number:");
	                    	final int deparNum = sc.nextInt();
	                    	sc.nextLine();
	                    	

		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_5c)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, acctNum);
		                        	statement.setInt(2, deparNum);
		                        	statement.setFloat(3, deptCost);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    	
	                    }
	                    else 
	                    {
	                    	break;
	                    }
	                	
	                	break;
	                case "6": 
	                	 // Collect the new Job Number
	                    System.out.println("Please enter a Job Number:");
	              
	                    final int jobNum = sc.nextInt(); // Read in the user input of Job Number
	                    
	                    
	                    System.out.println("Please enter a valid Assembly ID:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    sc.nextLine();
	                    final int assemblyID = sc.nextInt(); // Read in user input of a Assembly ID
	                    
	 
	                    sc.nextLine();
	                    System.out.println("Please enter a valid Process ID:");
	                    // No need to call nextLine extra time here, because the preceding nextLine consumed the newline character.
	                    final int processID = sc.nextInt(); // Read in user input of Process ID
	                    
	                    sc.nextLine();
	                    
	                    System.out.println("Please enter a Date Begin(YYYY-MM-DD:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    final String date_begin = sc.nextLine(); // Read in user input of a Date Established

	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_6)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setInt(1, jobNum);
	                        	statement.setString(2, date_begin);
	                        	statement.setInt(3, assemblyID);
	                        	statement.setInt(4, processID);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }

	                	break;
	                case "7":
	                	/**Get the user inputed Date complete of the Job
	                	 * 
	                	 */
	                	sc.nextLine();
	                    System.out.println("Please enter a Date Complete (YYYY-MM-DD):");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    // Read in user input of a Date Completed
	                    final String dcomplete = sc.nextLine();	
	                    
	                	//sc.nextLine();
	                    
	                	 // Collect the new Job Number
	                    System.out.println("Please enter a Job Number:");
	   
	                    final int jobNumba = sc.nextInt(); // Read in the user input of Job Number
	                    sc.nextLine();
	                    
	                	 // Collect the new Job Type
	                    System.out.println("Please enter a Job Type:");    
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    final String job_type = sc.nextLine(); // Read in user input of a Date Established
	                    
	                    
	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setString(1, dcomplete);
	                        	statement.setInt(2, jobNumba);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    /** Block to create the Paint table query **/ 
	                    if(job_type.equals("Paint"))
	                    {
	                    	System.out.println("Please enter Paint Color:");
 	
	                    	//Ask user for the Paint Color
	                    	final String color = sc.nextLine();
	                    	
	                    	System.out.println("Please enter Paint Volume:");
	                     	
	                    	//Ask user for the Paint Color
	                    	final int volume = sc.nextInt();
	                    	
	                    	sc.nextLine();
	                    	
	                    	System.out.println("Please enter Paint Labor Time:");
	                    	
	                    	//Ask user for the LaborTime
	                    	final Float ptime = sc.nextFloat();
	                    	
	                    	sc.nextLine();

		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7a)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, jobNumba);
		                        	statement.setString(2, color);
		                        	statement.setInt(3, volume);
		                        	statement.setFloat(4, ptime);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    	
	                    }
	                    
	                    /**Block to create the Assembly table query**/ 
	                    if(job_type.equals("Cut"))
	                    {
	                    	System.out.println("Please enter a Machine Type:");
	                    	
	                    	//Ask user for the Machine Type
	                    	final String macType = sc.nextLine();
	                    	
	                    	//Machine Time
	                    	System.out.println("Please enter Machine Time:");
	                    	
	                    	//Ask user for the LaborTime
	                    	final Float mactime = sc.nextFloat();
	                    	
	                    	sc.nextLine();
	                    	
	                    	System.out.println("Please enter Materials Used:");
	                    	
	                    	//Ask user for the Machine Type
	                    	final String matUsed = sc.nextLine();
	                    	
	                    	//Machine Time
	                    	System.out.println("Please enter Machine Labor Time:");
	                    	
	                    	//Ask user for the LaborTime
	                    	final Float laborTime = sc.nextFloat();
	                    	
	                    	sc.nextLine();
	                    	

		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7b)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, jobNumba);
		                        	statement.setString(2, macType);
		                        	statement.setFloat(3, mactime);
		                        	statement.setString(4, matUsed);
		                        	statement.setFloat(5, laborTime);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    	
	                    }
	                    
	                    /**Block to create the Department table query**/
	                   if(job_type.equals("Fit"))
	                    {
	                    	//Cut Time
	                    	System.out.println("Please enter Fit Labor Time:");
	                    	
	                    	//Ask user for the LaborTime
	                    	final Float cTime = sc.nextFloat();
	                    	
	                    	sc.nextLine();
	                    	

		                    System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_7c)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setInt(1, jobNumba);
		                        	statement.setFloat(2, cTime);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
	                    	
	                    }
	                    else 
	                    {
	                    	break;
	                    }

	                	break;
	                case "8": 
	                	
	                	/**Get the user inputed Transaction Number
	                	 */
	                    System.out.println("Please enter a valid Transaction Number:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    // Read in user input of a job number
	                    final int transNum = sc.nextInt();	
	                    
	                	sc.nextLine();
	                	/**Get the user inputed Job number
	                	 */
	                    System.out.println("Please enter a valid Job Number:");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    // Read in user input of a job number
	                    final int jNum = sc.nextInt();	
	                    
	                	sc.nextLine();

	                	 // Collect the new Sup Cost
	                    System.out.println("Please enter a Supply Cost: ");
	   
	                    final Float supCost = sc.nextFloat(); // Read in the user input of Sup Cost
	                    sc.nextLine();
	                   	                                       
	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_8)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setInt(1, transNum);
	                        	statement.setFloat(2, supCost);
	                        	statement.setInt(3, jNum);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                	//Ask user for input of an account number
	                    //For each valid account number must add the sup costs to 
	                	/**TODO*/
	                    //First get the account number to use
	                    // Collect the new Account Number from the User
	                    System.out.println("Please enter a Account Number:");
	                    
	                    final int acNum = sc.nextInt(); // Read in the user input of Account Num
	                    
	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_8a)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setInt(1, transNum);
	                        	statement.setInt(2, acNum);
	                        	statement.setFloat(3, supCost);
	                        	statement.setInt(4, acNum);
	                        
	                        	statement.setFloat(5, supCost);
	                        	statement.setInt(6, acNum);
	                        	
	                        	statement.setFloat(7, supCost);
	                        	statement.setInt(8, acNum);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    
	                	
	                	break;
	                	
	                case"9":
	                	//For this, get a user inputed Assembly ID and then select the account with that id, 
	                	//Return the cost 
	                	// Collect the new Assembly ID from the User
	                    System.out.println("Please enter a Assembly ID:");
	                    
	                    final int assemid = sc.nextInt(); // Read in the user input of Assembly ID
	                    
	                    sc.nextLine();
	                    
	                    System.out.println("Connecting to the database...");
	                    // Get the database connection, create statement and execute it right away, as no user input need be collected
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        System.out.println("Dispatching the query...");
	                        try (
	                            final Statement statement = connection.createStatement();
	                            final ResultSet resultSet = statement.executeQuery("SELECT assembly_cost FROM Assembly_Account WHERE aid=" +assemid)) {

	                                System.out.println("Contents of the Query table:");
	                                System.out.println("Assembly Cost");

	                                // Unpack the tuples returned by the database and print them out to the user
	                                while (resultSet.next()) {
	                                    System.out.println(String.format("%f",
	                                        resultSet.getFloat(1)));
	                                }
	                        }
	                    }
	                    
	                    break;
	                case "10":
	                	/**First retrieve a Date then get a process number, for that process number retrieve the 
	                	 * department number
	                	 */
	                	sc.nextLine();
	                    System.out.println("Please enter a Date Complete (YYYY-MM-DD):");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    // Read in user input of a Date Completed
	                    final String dcomp = sc.nextLine();	
	                    
	                    // Collect the new student data from the user
	                    System.out.println("Please enter a Department Num:");
	                    final int deptN = sc.nextInt(); // Read in the user input of Department Number
	                    
	                    sc.nextLine();
	                    
	                    System.out.println("Connecting to the database...");
	                    // Get the database connection, create statement and execute it right away, as no user input need be collected
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        System.out.println("Dispatching the query...");
	                        try (
	                            final Statement statement = connection.createStatement();
	                            final ResultSet resultSet = statement.executeQuery("SET @piroc = SELECT process_ID FROM Process WHERE dept_num="+ deptN +
  									  "SET @jn = SELECT job_no FROM Job WHERE pid = piroc AND date_complete=" + dcomp + 
  									  "SET @ptim = SELECT labortime FROM PaintJob WHERE jobno = jn"+
  									  "SET @ctim = SELECT cut_time FROM CutJob WHERE jobno = jn  INTO ctim" +
  									  "SET @ftim = SELECT labortime FROM FitJob WHERE jobno = jn"+
  									  "@ptim + @ctim + @ftim")) {

                                System.out.println("Contents of the Result table:");
                                System.out.println("Time");

                                // Unpack the tuples returned by the database and print them out to the user
                                while (resultSet.next()) {
                                    System.out.println(String.format("%f ",
                                        resultSet.getFloat(1)
                                        /*resultSet.getString(2),
                                        resultSet.getInt(3)*/));
	                                }
	                        }
	                    }
	                	
	                case "11":
	                	break;
	                case "12":
	                	sc.nextLine();
	                    System.out.println("Please enter a Date Complete (YYYY-MM-DD):");
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    // Read in user input of a Date Completed
	                    final String daycomplete = sc.nextLine();	
	                    
	                    // Collect the new student data from the user
	                    System.out.println("Please enter a Department Num:");
	                    final int deptNumb = sc.nextInt(); // Read in the user input of Department Number
	                    System.out.println("Connecting to the database...");
	                    // Get the database connection, create statement and execute it right away, as no user input need be collected
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        System.out.println("Dispatching the query...");
	                        try (
	                            final Statement statement = connection.createStatement();
	                            final ResultSet resultSet = statement.executeQuery("SELECT job_no FROM Job WHERE dnum=" + deptNumb + "AND date_complete = " + daycomplete +
	                            	"FULL JOIN SELECT")) {

                                System.out.println("Contents of the Result table:");
                                System.out.println("Time");

                                // Unpack the tuples returned by the database and print them out to the user
                                while (resultSet.next()) {
                                    System.out.println(String.format("%f ",
                                        resultSet.getFloat(1)
                                        /*resultSet.getString(2),
                                        resultSet.getInt(3)*/));
	                                }
	                        }
	                    }
	                    
	                	break;
	                case "13": 
	                	// Collect the new Min from the User
	                    System.out.println("Please enter a Min Value:");
	                    
	                    final int min = sc.nextInt(); // Read in the user input of Assembly ID
	                    
	                    sc.nextLine();
	                    
	                
	                	// Collect the new Max from the User
	                    System.out.println("Please enter a Max Value:");
	                    
	                    final int max = sc.nextInt(); // Read in the user input of Assembly ID
	                    
	                    sc.nextLine();
	                    
	                    System.out.println("Connecting to the database...");
	                    // Get the database connection, create statement and execute it right away, as no user input need be collected
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        System.out.println("Dispatching the query...");
	                        try (
	                            final Statement statement = connection.createStatement();
	                            final ResultSet resultSet = statement.executeQuery("SELECT cust_name FROM Customer WHERE cust_category BETWEEN " + min + " AND " + max +";")) {

                                System.out.println("Contents of the Customer table:");
                                System.out.println("Name | address | category");

                                // Unpack the tuples returned by the database and print them out to the user
                                while (resultSet.next()) {
                                    System.out.println(String.format("%s ",
                                        resultSet.getString(1)
                                        /*resultSet.getString(2),
                                        resultSet.getInt(3)*/));
	                                }
	                        }
	                    }
	                    break;
	                case "14":
	                	// Collect the new Min from the User
	                    System.out.println("Please enter a Min Value:");
	                    
	                    final int minJob = sc.nextInt(); // Read in the user input of Assembly ID
	                    
	                    sc.nextLine();
	                    
	                
	                	// Collect the new Max from the User
	                    System.out.println("Please enter a Max Value:");
	                    
	                    final int maxJob = sc.nextInt(); // Read in the user input of Assembly ID
	                    
	                    sc.nextLine();
	                    
	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement("DELETE FROM CutJob WHERE jobno BETWEEN " + minJob + " AND " + maxJob +";")) {

	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    break;
	                    
	                case "15": 
	                	 // Collect the new Job Number
	                    System.out.println("Please enter a Job Number:");
	   
	                    final int jobNumbee = sc.nextInt(); // Read in the user input of Job Number
	                    sc.nextLine();
	                    
	                	 // Collect the new Color to change to
	                    System.out.println("Please enter a Color to Change to:");    
	                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
	                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing
	                    final String colorchange= sc.nextLine(); // Read in user input of a Date Established
	                    
	                    
	                    System.out.println("Connecting to the database...");
	                    // Get a database connection and prepare a query statement
	                    try (final Connection connection = DriverManager.getConnection(URL)) {
	                        try (
	                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_15)) {
	                            // Populate the query template with the data collected from the user
	                        	statement.setString(1, colorchange);
	                        	statement.setInt(2, jobNumbee);
	                            System.out.println("Dispatching the query...");
	                            // Actually execute the populated query
	                            final int rows_inserted = statement.executeUpdate();
	                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
	                        }
	                    }
	                    break;
	                case "16":
	                	/**TODO**/
	                	sc.nextLine();
	                	 // Collect the new Job Number
	                    System.out.println("Please enter a File Name:");
	                    
	                    final String file = sc.nextLine(); // Read in the user input of Job Number
	                    //File fil = new File(file);
	                    //Scanner myReader = new Scanner(fil);
	                    BufferedReader br = new BufferedReader(new FileReader(file));
	                    //myReader.useDelimiter(",");
	                   // myReader.nextLine();
	                    String contentLine = br.readLine();
	                    while (contentLine != null){
	                    	
	                       String[] words = contentLine.split(" ");
	                       String custname = words[0];
	                       //System.out.print(custname);
	                       String custaddy = words[1];
	                       int custcat = Integer.parseInt(words[2]);                     

	                      System.out.println("Connecting to the database...");
		                    // Get a database connection and prepare a query statement
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        try (
		                            final PreparedStatement statement = connection.prepareStatement(QUERY_TEMPLATE_1)) {
		                            // Populate the query template with the data collected from the user
		                        	statement.setString(1, custname);
		                        	statement.setString(2, custaddy);
		                        	statement.setInt(3, custcat);
		                            System.out.println("Dispatching the query...");
		                            // Actually execute the populated query
		                            final int rows_inserted = statement.executeUpdate();
		                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
		                        }
		                    }
		                    contentLine = br.readLine();
	              
	                    }
	                 
	                    break;
	                case "17":
	                	/**TODO**/
	                	sc.nextLine();
	                	 // Collect the new Job Number
	                    System.out.println("Please enter a File Name:");
	                    
	                    final String fileN = sc.nextLine(); // Read in the user input of FileName 
	                    try {

	                        //String content = "This is the content to write into file";
	                    	//File to write to
	                        File fileWrite = new  File(fileN);

	                        // if file doesnt exists, then create it
	                        if (!fileWrite.exists()) {
	                            fileWrite.createNewFile();
	                        }

	                        FileWriter fw = new FileWriter(fileWrite.getAbsoluteFile());
	                        BufferedWriter bw = new BufferedWriter(fw);
	                        
		                	// Collect the new Min from the User
		                    System.out.println("Please enter a Min Value:");
		                    
		                    final int minW = sc.nextInt(); // Read in the user input of Assembly ID
		                    
		                    sc.nextLine();

		                	// Collect the new Max from the User
		                    System.out.println("Please enter a Max Value:");
		                    
		                    final int maxW = sc.nextInt(); // Read in the user input of Assembly ID
		                    
		                    sc.nextLine();
		                    
		                    System.out.println("Connecting to the database...");
		                    // Get the database connection, create statement and execute it right away, as no user input need be collected
		                    try (final Connection connection = DriverManager.getConnection(URL)) {
		                        System.out.println("Dispatching the query...");
		                        try (
		                            final Statement statement = connection.createStatement();
		                            final ResultSet resultSet = statement.executeQuery("SELECT cust_name FROM Customer WHERE cust_category BETWEEN " + minW + " AND " + maxW +";")) {

	                                System.out.println("Contents of the Customer table:");
	                                System.out.println("Name | address | category");

	                                // Unpack the tuples returned by the database and print them out to the user
	                                while (resultSet.next()) {
	                                    System.out.println(String.format("%s ",
	                                        resultSet.getString(1)
	                                        /*resultSet.getString(2),
	                                        resultSet.getInt(3)*/));
	                                    bw.write(resultSet.getString(1) + "\n");
		                                }
		                        }
		                    }
	                        
	                        bw.close();

	                        System.out.println("Done");

	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    }
	                case "18": // Do nothing, the while loop will terminate upon the next iteration
	                    System.out.println("Exiting! Good-buy!");
	                    break;
	                default: // Unrecognized option, re-prompt the user for the correct one
	                    System.out.println(String.format(
	                        "Unrecognized option: %s\n" + 
	                        "Please try again!", 
	                        option));
	                    break;
	            }
	        }

	        sc.close(); // Close the scanner before exiting the application
	    }

}
