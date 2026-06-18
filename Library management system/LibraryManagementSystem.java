package com.Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;


public class LibraryManagementSystem 
{
	//database connection details
	private static final String url="jdbc:mysql://localhost:3306/library";
	private static final String username="mysql";
	private static final String password="Amulya@30";
	public static void main(String[] args) 
	{
		// Load the MySQL JDBC driver
		try
		{
			Class.forName("com.mysql.cj.jdbc.Driver");
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		// Create a Scanner object for user input
		Scanner scanner=new Scanner(System.in);
		try
		{
			// Establish a connection to the database
			Connection connection=DriverManager.getConnection(url,username,password);
			// Create instances of different management classes
			Books Books=new Books(connection,scanner);
			member member=new member(connection,scanner);
			staff staff=new staff(connection,scanner);
			issue issue=new issue(connection,scanner);
			// Main loop for the library management system
			while(true)
			{
				System.out.println("Library Management System");
				System.out.println("1. Operations on Books");
				System.out.println("2. Operations on Members");
				System.out.println("3. Operations on Staff");
				System.out.println("4. Issue Books");
				System.out.println("5. Exit");
				System.out.println("Please enter your choice");
				int choice=scanner.nextInt();
				// Handle user choice using a switch statement
				switch(choice)
				{
				case 1:
					// Perform operations on books
					Books.operationOnBooks(); 
					System.out.println();
					break;
				case 2:
					// Perform operations on members
					member.operationOnMembers();
					System.out.println();
					break;
				case 3:
					// Perform operations on staff
					staff.operationOnStaff();
					System.out.println();
					break;
				case 4:
					// Perform operations on issuing books
					issue.operationsOnIssueBook();
					System.out.println();
					break;
				case 5:
					// Exit the program
					System.out.println("Thank you! for using library management system!");
					return;
				default:
					System.out.println("please enter valid input");
					break;
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}	
	}
}
