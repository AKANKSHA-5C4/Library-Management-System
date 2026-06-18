package com.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class member 
{
	private Connection connection;
	private Scanner scanner;
	
	// Constructor to initialize connection and scanner
	public member(Connection connection,Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
	}
	
	// Method to perform operations on members
	public void operationOnMembers()
	{
		try
		{
			while(true)
			{
				// Display options for member operations
				System.out.println("Performing operations on memberships");
				System.out.println("1. Add Member");
				System.out.println("2. View Member list");
				System.out.println("3. Remove member by Id");
				System.out.println("4. Exit");
				System.out.println("Please enter your choice");
				int choice=scanner.nextInt();
				scanner.nextLine();
				// Handle user choice using a switch statement
				switch(choice)
				{
				case 1:
					// Call method to add a member
					addMember();
					System.out.println();
					break;
				case 2:
					// Call method to view all members
					viewMember();
					System.out.println();
					break;
				case 3:
					// Call method to remove a member by ID
					removeMemberById();
					System.out.println();
					break;
				case 4:
					// Exit the method
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
	
	// Method to add a new member to the database
	public void addMember()
	{
		System.out.println("Enter the name: ");
		String name=scanner.next();
		System.out.println("Enter the mobile number: ");
		Long mobile_no=scanner.nextLong();
		System.out.println("Enter the address: ");
		String address=scanner.next();
		System.out.println("Enter the occupation: ");
		String occupation=scanner.next();
		try
		{
			// SQL query to insert a new member
			String Query="Insert into members(name,mobile_no,address,member_type) values (?,?,?,?)";
			PreparedStatement preparedStatement=connection.prepareStatement(Query);
			preparedStatement.setString(1,name);
			preparedStatement.setLong(2,mobile_no);
			preparedStatement.setString(3, address);
			preparedStatement.setString(4, occupation);
			// Execute the update and check if the member was added successfully
			int affectedRows=preparedStatement.executeUpdate();
			if(affectedRows>0)
			{
				System.out.println("Member added successfully");
			}
			else
			{
				System.out.println("Failed to add Member!");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to view all members in the database
	public void viewMember()
	{
		try
		{
			// SQL query to select all members
			String query="select * from members";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			ResultSet resultset=preparedStatement.executeQuery();
			
			// Display the members in a formatted table
			System.out.println("Members:");
			System.out.println("+------+----------------+----------------+----------------------------+----------------+");
			System.out.println("| Id   | Name           | Mobile no      | Address                    | Occupation     |");
			System.out.println("+------+----------------+----------------+----------------------------+----------------+");
			while(resultset.next())
			{
				// Retrieve member details from the result set
				int id=resultset.getInt("id");
				String name=resultset.getString("name");
				Long mobile_no=resultset.getLong("mobile_no");
				String address=resultset.getString("address");
				String occupation=resultset.getString("member_type");
				System.out.printf("|%-6s|%-16s|%-16s|%-28s|%-16s|\n",id,name,mobile_no,address,occupation);
				System.out.println("+------+----------------+----------------+----------------------------+----------------+");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to remove a member by their ID
	public void removeMemberById()
	{
		System.out.println("Enter the id of the member: ");
		int id=scanner.nextInt();
		try
		{
			// SQL query to delete a member by ID
			String query="delete from members where id=?";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1,id);
			int affectedRows=preparedStatement.executeUpdate();
			if(affectedRows>0)
			{
				System.out.println("Member with id "+id+" removed successfully");
			}
			else
			{
				System.out.println("No member found with that id no:"+id);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to check if a member exists by their ID
	public boolean getMemberByID(int id)
	{
		String query="select * from members where id=?";
		try
		{
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultset=preparedStatement.executeQuery();
			if(resultset.next())
			{
				return true; // Return true if a member with the given ID exists
			}
			else
			{
				return false; // Return false if no member is found
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return false; // Return false if an exception occurs
	}
}
