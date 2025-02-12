package com.elibrary.inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.elibrary.exception.ISBNAlreadyExistsException;
import com.elibrary.models.Book;

public class Inventory {
	public List<Book> books = new ArrayList<>();

	public void addBook(Book book) throws ISBNAlreadyExistsException {
		boolean isbnExists = books.stream().anyMatch(existingBook -> existingBook.getIsbn().equals(book.getIsbn()));
		if (isbnExists) {
			throw new ISBNAlreadyExistsException("Book with the same ISBN already exists.");
		}
		books.add(book);
	}

	public Optional<Book> getBookByName(String name) {
		return books.stream().filter(book -> book.getTitle().toLowerCase().contains(name.toLowerCase())).findFirst();
	}

	public Book updateBook(Book updatedBook) {
		Optional<Book> bookToUpdate = books.stream().filter(book -> book.getIsbn().equals(updatedBook.getIsbn()))
				.findFirst();
		if (bookToUpdate.isPresent()) {
			Book book = bookToUpdate.get();
			book.setTitle(updatedBook.getTitle());
			book.setAuthor(updatedBook.getAuthor());
			book.setPublisher(updatedBook.getPublisher());
			book.setAvailable(updatedBook.isAvailable());
			book.setDueDate(updatedBook.getDueDate());
			book.setIssuedDate(updatedBook.getIssuedDate());
			book.setUsername(updatedBook.getUsername());
			return book;
		} else {
			return null;
		}
	}

	public List<Book> getAllBooks() {
		return books;
	}

	public boolean issueBook(String isbn, String username, LocalDate dueDate) {
		Book book = books.stream().filter(b -> b.getIsbn().equals(isbn) && b.isAvailable()).findFirst().orElse(null);
		if (book != null) {
			book.setAvailable(false);
			book.setIssuedDate(LocalDate.now());
			book.setDueDate(dueDate);
			book.setUsername(username);
			return true;
		}
		return false;
	}

	public boolean isBookAvailable(String isbn) {
		return books.stream().anyMatch(book -> book.getIsbn().equals(isbn) && book.isAvailable());
	}

	public List<Book> getBorrowedBooks() {
		return books.stream().filter(book -> !book.isAvailable()).collect(Collectors.toList());
	}
}
