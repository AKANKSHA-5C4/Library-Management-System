package com.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class staff 
{
	private Connection connection;
	private Scanner scanner;
	// Constructor to initialize connection and scanner
	public staff(Connection connection,Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
	}
	
	// Method to perform operations on staff
	public void operationOnStaff()
	{
		try
		{
			while(true)
			{
				
				// Display options for staff operations
				System.out.println("Performing operations on staff");
				System.out.println("1. Add Staff");
				System.out.println("2. View Staff list");
				System.out.println("3. Remove Staff by Id");
				System.out.println("4. Exit");
				System.out.println("Please enter your choice");
				int choice;
				choice=scanner.nextInt();
				scanner.nextLine();
				
				// Handle user choice using a switch statement
				switch(choice)
				{
				case 1:
					// Call method to add a staff member
					addStaff();
					System.out.println();
					break;
				case 2:
					// Call method to view all staff members
					viewStaff();
					System.out.println();
					break;
				case 3:
					// Call method to remove a staff member by ID
					removeStaffById();
					System.out.println();
					break;
				case 4:
					System.out.println("Thank you!");
					return;
				default:
					System.out.println("Please enter valid choice!!");
					break;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Method to add a new staff member to the database
	public void addStaff()
	{
		System.out.println("Enter the name of the staff: ");
		String name=scanner.nextLine();
		System.out.println("Enter the role of the staff: ");
		String role=scanner.nextLine();
		try
		{
			// SQL query to insert a new staff member
			String query="insert into staff(name,role) values(?,?)";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, role);
			
			// Execute the update and check if the staff member was added successfully
			int affectedRows=preparedStatement.executeUpdate();
			if(affectedRows>0)
			{
				System.out.println("Staff added successfully");
			}
			else
			{
				System.out.println("Failed to add staff member!");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to view all staff members in the database
	public void viewStaff()
	{
		try
		{
			// SQL query to select all staff members
			String query="select * from staff";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			ResultSet resultset=preparedStatement.executeQuery();
			// Display the staff members in a formatted table
			System.out.println("Staff Members:");
			System.out.println("+------+-------------------+--------------------+");
			System.out.println("| Id   | Name              | Role               |");
			System.out.println("+------+-------------------+--------------------+");
			while(resultset.next())
			{
				// Retrieve staff details from the result set
				int id=resultset.getInt("id");
				String name=resultset.getString("name");
				String role=resultset.getString("role");
				System.out.printf("|%-6s|%-19s|%-20s|\n",id,name,role);
				System.out.println("+------+-------------------+--------------------+");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to remove a staff member by their ID
	public void removeStaffById()
	{
		System.out.println("Enter the id of the staff: ");
		int id=scanner.nextInt();
		try
		{
			// SQL query to delete a staff member by ID
			String query="delete from staff where id=?";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1,id);
			int affectedRows=preparedStatement.executeUpdate();
			if(affectedRows>0)
			{
				System.out.println("Staff Member with id "+id+" removed successfully");
			}
			else
			{
				System.out.println("No staff member found with that id no:"+id);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to check if a staff member exists by their ID
	public boolean getStaffByID(int id)
	{
		String query="select * from staff where id=?";
		try
		{
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultset=preparedStatement.executeQuery();
			if(resultset.next())
			{
				return true; // Return true if a staff member with the given ID exists
			}
			else
			{
				return false; // Return false if no staff member is found
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false; // Return false if an exception occurs
	}
}

