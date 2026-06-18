package com.Project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Books 
{
	// Database connection
	private Connection connection;
	// Scanner for user input
	private Scanner scanner;
	// Constructor to initialize connection and scanner
	public Books(Connection connection,Scanner scanner)
	{
		this.connection=connection;
		this.scanner=scanner;
	}
	// Method to perform operations on books
	public void operationOnBooks()
	{
		try
		{
			while(true)
			{
				// Display options for book operations
				System.out.println("Performing operations on books");
				System.out.println("1. Add Book");
				System.out.println("2. View Books");
				System.out.println("3. search Book By Name");
				System.out.println("4. search Book By Author");
				System.out.println("5. Exit");
				System.out.println("Please enter your choice");
				int choice=scanner.nextInt();
				scanner.nextLine();
				// Handle user choice using a switch statement
				switch(choice)
				{
				case 1:
					// Call method to add a book
					addBook();
					System.out.println();
					break;
				case 2:
					// Call method to view all books
					viewBooks();
					System.out.println();
					break;
				case 3:
					// Call method to search book by name
					searchBookByName();
					System.out.println();
					break;
				case 4:
					// Call method to search book by author
					searchBookByAuthor();
					System.out.println();
					break;
				case 5:
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
	// Method to add a new book to the database
	public void addBook()
	{
		System.out.println("Enter the name of Book: ");
		String bookName=scanner.nextLine();
		System.out.println("Enter the author name of Book: ");
		String bookAuthor=scanner.nextLine();
		System.out.println("Enter the shelf number of Book: ");
		int bookShelfNo=scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter the no of copies of Book: ");
		int bookCopies=scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter the category of Book: ");
		String bookCategory=scanner.nextLine();
		try
		{
			// SQL query to insert a new book
			String Query="Insert into books(name,author,shelf_number,no_of_copies,category) values (?,?,?,?,?)";
			PreparedStatement preparedStatement=connection.prepareStatement(Query);
			preparedStatement.setString(1,bookName);
			preparedStatement.setString(2,bookAuthor);
			preparedStatement.setInt(3, bookShelfNo);
			preparedStatement.setInt(4, bookCopies);
			preparedStatement.setString(5,bookCategory);
			// Execute the update and check if the book was added successfully
			int affectedRows=preparedStatement.executeUpdate();
			if(affectedRows>0)
			{
				System.out.println("Book added successfully");
			}
			else
			{
				System.out.println("Failed to add Book!");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	// Method to view all books in the database
	public void viewBooks()
	{
		try
		{
			// SQL query to select all books
			String query="select * from books";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			ResultSet resultset=preparedStatement.executeQuery();
			// Display the books in a formatted table
			System.out.println("Books:");
			System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
			System.out.println("| Id   | Name           | Author         | Self number  | no of copies | category       |");
			System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
			while(resultset.next())
			{
				// Retrieve book details from the result set
				int id=resultset.getInt("id");
				String name=resultset.getString("name");
				String author=resultset.getString("author");
				int self_number=resultset.getInt("shelf_number");
				int no_of_copies=resultset.getInt("no_of_copies");
				String category=resultset.getString("category");
				System.out.printf("|%-6s|%-16s|%-16s|%-14s|%-14s|%-16s|\n",id,name,author,self_number,no_of_copies,category);
				System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	// Method to search for a book by its name
	public void searchBookByName()
	{
		try
		{
			// SQL query to search for a book by name
			String query="select * from books where name=?";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			System.out.println("Enter the name of the book: ");
			String name=scanner.nextLine();
			preparedStatement.setString(1,name);
			ResultSet resultset=preparedStatement.executeQuery();
			
			// Check if any books were found
			if(!resultset.isBeforeFirst())
			{
				System.out.println("Couldn't find the book with this name "+name);
			}
			else
			{
				// Display the found books in a formatted table
				System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
				System.out.println("| Id   | Name           | Author         | Self number  | no of copies | category       |");
				System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
				while(resultset.next())
				{
					System.out.printf("|%-6s|%-16s|%-16s|%-14s|%-14s|%-16s|\n",resultset.getInt("id"),name,resultset.getString("author"),resultset.getInt("shelf_number"),resultset.getInt("no_of_copies"),resultset.getString("category"));
					System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	// Method to search for a book by its author
	public void searchBookByAuthor()
	{
		try
		{
			// SQL query to search for a book by author
			String query="select * from books where author=?";
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			System.out.println("Enter the name of the author: ");
			String author=scanner.nextLine();
			preparedStatement.setString(1,author);
			ResultSet resultset=preparedStatement.executeQuery();
			
			// Check if any books were found
			if(!resultset.isBeforeFirst())
			{
				System.out.println("Couldn't find the book with this name "+author);
			}
			else
			{
				// Display the found books in a formatted table
				System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
				System.out.println("| Id   | Name           | Author         | Self number  | no of copies | category       |");
				System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
				while(resultset.next())
				{
					System.out.printf("|%-6s|%-16s|%-16s|%-14s|%-14s|%-16s|\n",resultset.getInt("id"),resultset.getString("name"),author,resultset.getInt("shelf_number"),resultset.getInt("no_of_copies"),resultset.getString("category"));
					System.out.println("+------+----------------+----------------+--------------+--------------+----------------+");
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	// Method to check if a book exists by its ID
	public boolean getBookByID(int id)
	{
		String query="select * from books where id=?";
		try
		{
			PreparedStatement preparedStatement=connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet resultset=preparedStatement.executeQuery();
			if(resultset.next())
			{
				// Return true if a book with the given ID exists
				return true;
			}
			else
			{
				// Return false if no book is found
				return false;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		// Return false if an exception occurs
		return false;
	}
}

