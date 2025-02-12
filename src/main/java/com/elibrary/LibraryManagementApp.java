package com.elibrary;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.elibrary.exception.ISBNAlreadyExistsException;
import com.elibrary.inventory.Inventory;
import com.elibrary.models.Book;

public class LibraryManagementApp {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Inventory inventory = new Inventory();

		while (true) {
			System.out.println("Options:");
			System.out.println("1. Add Book");
			System.out.println("2. Get Book by Name");
			System.out.println("3. Issue a Book");
			System.out.println("4. Check Availability");
			System.out.println("5. List Borrowed Books");
			System.out.println("6. Update Book");
			System.out.println("7. Get All Books");
			System.out.println("8. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline

			switch (choice) {
			case 1:
				addBook(inventory);
				break;
			case 2:
				getBookByName(inventory);
				break;
			case 3:
				issueBook(inventory);
				break;
			case 4:
				checkAvailability(inventory);
				break;
			case 5:
				listBorrowedBooks(inventory);
				break;
			case 6:
				updateBook(inventory);
				break;
			case 7:
				getAllBooks(inventory);
				break;
			case 8:
				System.out.println("Exiting...");
				scanner.close();
				System.exit(0);
			default:
				System.out.println("Invalid choice. Please enter a valid option.");
			}
		}
	}

	private static void addBook(Inventory inventory) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter ISBN: ");
		String isbn = scanner.nextLine();
		System.out.print("Enter Title: ");
		String title = scanner.nextLine();
		System.out.print("Enter Author: ");
		String author = scanner.nextLine();
		System.out.print("Enter Publisher: ");
		String publisher = scanner.nextLine();
		Book newBook = new Book(isbn, title, author, publisher, true, null, null, null);
		try {
			inventory.addBook(newBook);
			System.out.println("Book added successfully.");
		} catch (ISBNAlreadyExistsException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	private static void getBookByName(Inventory inventory) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the name of the book: ");
		String bookName = scanner.nextLine();
		Optional<Book> bookOptional = inventory.getBookByName(bookName);
		if (bookOptional.isPresent()) {
			Book book = bookOptional.get();
			System.out.println("Book found:");
			System.out.println("Title: " + book.getTitle());
			System.out.println("Author: " + book.getAuthor());
			System.out.println("Publisher: " + book.getPublisher());
			System.out.println("ISBN: " + book.getIsbn());
			if (book.isAvailable()) {
				System.out.println("Status: Available");
			} else {
				System.out.println("Status: Issued");
				System.out.println("Issued Date: " + book.getIssuedDate());
				System.out.println("Due Date: " + book.getDueDate());
			}
		} else {
			System.out.println("Book not found.");
		}
	}

	private static void issueBook(Inventory inventory) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter ISBN of the book: ");
		String isbn = scanner.nextLine();

		if (!inventory.isBookAvailable(isbn)) {
			System.out.println("The book with ISBN " + isbn + " is not available for issuance.");
			return;
		}

		System.out.print("Enter username: ");
		String username = scanner.nextLine();

		System.out.print("Enter due date (YYYY-MM-DD): ");
		String dueDateStr = scanner.nextLine();
		LocalDate dueDate = LocalDate.parse(dueDateStr);

		boolean success = inventory.issueBook(isbn, username, dueDate);

		if (success) {
			System.out.println("Book issued successfully to user " + username + ".");
		} else {
			System.out.println("Failed to issue the book. Please check the book availability and user details.");
		}
	}

	private static void checkAvailability(Inventory inventory) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter ISBN of the book: ");
		String isbn = scanner.nextLine();

		if (inventory.isBookAvailable(isbn)) {
			System.out.println("The book with ISBN " + isbn + " is available.");
		} else {
			System.out.println("The book with ISBN " + isbn + " is not available.");
		}
	}

	private static void listBorrowedBooks(Inventory inventory) {
		List<Book> borrowedBooks = inventory.getBorrowedBooks();

		if (!borrowedBooks.isEmpty()) {
			System.out.println("Borrowed Books:");
			for (Book book : borrowedBooks) {
				System.out.println("Title: " + book.getTitle());
				System.out.println("ISBN: " + book.getIsbn());
				System.out.println("Username: " + book.getUsername());
				System.out.println("Due Date: " + book.getDueDate());
				System.out.println();
			}
		} else {
			System.out.println("No books are currently borrowed.");
		}
	}

	private static void updateBook(Inventory inventory) {
		Scanner scanner = new Scanner(System.in);

		System.out.print("Enter ISBN of the book to update: ");
		String isbn = scanner.nextLine();

		Book bookToUpdate = inventory.getAllBooks().stream().filter(book -> book.getIsbn().equals(isbn)).findFirst()
				.orElse(null);

		if (bookToUpdate != null) {
			System.out.print("Enter updated title: ");
			String updatedTitle = scanner.nextLine();
			System.out.print("Enter updated author: ");
			String updatedAuthor = scanner.nextLine();
			System.out.print("Enter updated publisher: ");
			String updatedPublisher = scanner.nextLine();

			Book updatedBook = new Book(isbn, updatedTitle, updatedAuthor, updatedPublisher, bookToUpdate.isAvailable(),
					bookToUpdate.getIssuedDate(), bookToUpdate.getDueDate(), bookToUpdate.getUsername());
			inventory.updateBook(updatedBook);
			System.out.println("Book updated successfully.");
		} else {
			System.out.println("No book found with ISBN " + isbn + ".");
		}
	}

	private static void getAllBooks(Inventory inventory) {
		List<Book> allBooks = inventory.getAllBooks();

		if (!allBooks.isEmpty()) {
			System.out.println("All Books:");
			for (Book book : allBooks) {
				System.out.println("Title: " + book.getTitle());
				System.out.println("ISBN: " + book.getIsbn());
				System.out.println("Author: " + book.getAuthor());
				System.out.println("Publisher: " + book.getPublisher());
				System.out.println("Available: " + (book.isAvailable() ? "Yes" : "No"));
				System.out.println();
			}
		} else {
			System.out.println("No books available.");
		}
	}

}
