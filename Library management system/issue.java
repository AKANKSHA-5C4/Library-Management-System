package com.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class issue 
{
	private Connection connection;
	private Scanner scanner;
	// Constructor to initialize connection and scanner
	public issue(Connection connection,Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
	}
	
	// Method to perform operations on issued books
	public void operationsOnIssueBook()
	{
		try
		{
			while(true)
			{
				// Display options for issuing books
				System.out.println("Performing operations on issued books");
				System.out.println("1. Issue Book");
				System.out.println("2. View issued book");
				System.out.println("3. Return book");
				System.out.println("4. Exit");
				System.out.println("Please enter your choice");
				// Create instances of other classes for operations
				Books books=new Books(connection,scanner);
				member member=new member(connection,scanner);
				staff staff=new staff(connection,scanner);
				int choice=scanner.nextInt();
				scanner.nextLine();
				
				// Handle user choice using a switch statement
				switch(choice)
				{
				case 1:
					// Call method to issue a book
					issueBook(books,member,staff,connection,scanner);
					System.out.println();
					break;
				case 2:
					// Call method to view issued books
					viewIssuedBook();
					System.out.println();
					break;
				case 3:
					// Call method to return a book
					returnBook();
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
	
	// Method to issue a book to a member
	public void issueBook(Books books,member member,staff staff,Connection connection,Scanner scanner)
	{
		System.out.println("Enter the member Id: ");
		int member_id=scanner.nextInt();
		System.out.println("Enter the book Id: ");
		int book_id=scanner.nextInt();
		System.out.println("Enter your staff Id: ");
		int staff_id=scanner.nextInt();
		try
		{
			// Check if the book, member, and staff IDs are valid
			if(books.getBookByID(book_id) && member.getMemberByID(member_id) && staff.getStaffByID(staff_id))
			{
				// Query to get book details
				String query="select name,no_of_copies from books where id=?";
				PreparedStatement preparedBook=connection.prepareStatement(query);
				preparedBook.setInt(1,book_id);
				ResultSet resultset=preparedBook.executeQuery();
				if(resultset.next())
				{
					int copies=resultset.getInt("no_of_copies");
					String book_name=resultset.getString("name");
					
					// Check if copies are available
					if(copies>0)
					{
						// Query to get member name
						String Query="select name from members where id=?";
						PreparedStatement preparedMember=connection.prepareStatement(Query);
						preparedMember.setInt(1,member_id);
						ResultSet resultmember=preparedMember.executeQuery();
						String member_name=null;
						if(resultmember.next())
						{
							member_name=resultmember.getString("name");
						}
						
						// Query to insert the issue record
						String issueQuery="insert into issue(staff_id,book_id,member_id,member_name,book_name,issue_date,return_date) values(?,?,?,?,?,CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY))";
						PreparedStatement prepareIssue=connection.prepareStatement(issueQuery);
						prepareIssue.setInt(1,staff_id);
						prepareIssue.setInt(2,book_id);
						prepareIssue.setInt(3,member_id);
						prepareIssue.setString(4,member_name);
						prepareIssue.setString(5,book_name);
						
						// Execute the insert and check if successful
						int affectedRows=prepareIssue.executeUpdate();
						if(affectedRows>0)
						{
							// Update the number of copies in the books table
							String updateQuery="update books set no_of_copies=no_of_copies-1 where id=?";
							PreparedStatement prepareUpdate=connection.prepareStatement(updateQuery);
							prepareUpdate.setInt(1,book_id);
							prepareUpdate.executeUpdate();
							System.out.println("Book issued successfully");
						}
						else
						{
							System.out.println("Failed to issue Book!");
						}
					}
					else
					{
						System.out.println("No copies avaliable for the given book");
					}
				}
			}
			else
			{
				if(!books.getBookByID(book_id))
				{
					System.out.println("Invalid book id provided");
				}
				else if(!member.getMemberByID(member_id))
				{
					System.out.println("Invalid member id provided");
				}
				else if(!staff.getStaffByID(staff_id))
				{
					System.out.println("Invalid staff id provided");
				}
				System.out.println("please enter valid id");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to view all issued books
	public void viewIssuedBook()
	{
		try
		{
			// Query to select all issued books
			String query="select * from issue";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			ResultSet resultset=preparedStatement.executeQuery();
			
			// Display the issued books in a formatted table
			System.out.println("Issued Books:");
			System.out.println("+------+--------------+--------------+--------------+------------------------+------------------------+--------------+--------------+");
			System.out.println("| Id   | staff id     | book id      | member id    | member name            | book name              | issue date   | return date  |");
			System.out.println("+------+--------------+--------------+--------------+------------------------+------------------------+--------------+--------------+");
			while(resultset.next())
			{
				// Retrieve issued book details from the result set
				int id=resultset.getInt("id");
				int staff_id=resultset.getInt("staff_id");
				int book_id=resultset.getInt("book_id");
				int member_id=resultset.getInt("member_id");
				String member_name=resultset.getString("member_name");
				String book_name=resultset.getString("book_name");
				String issue_date=resultset.getString("issue_date");
				String return_date=resultset.getString("return_date");
				System.out.printf("|%-6s|%-14s|%-14s|%-14s|%-24s|%-24s|%-14s|%-14s|\n",id,staff_id,book_id,member_id,member_name,book_name,issue_date,return_date);
				System.out.println("+------+--------------+--------------+--------------+------------------------+------------------------+--------------+--------------+");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	// Method to return a book
	public void returnBook()
	{
		System.out.println("Enter the id of the issue: ");
		int id=scanner.nextInt();
		try
		{
			// Query to delete the issue record
			String query="delete from issue where id=?";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1,id);
			int affectedRows=preparedStatement.executeUpdate();
			if(affectedRows>0)
			{
				System.out.println("issued book with id "+id+" removed successfully");
			}
			else
			{
				System.out.println("No issued book found with that id no:"+id);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
}
