package org.example.reposytory.impl;

import org.example.entity.Book;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class BookRepositoryTest {

    private BookRepositoryImpl bookRepository;

    @Before
    public void setUp() throws Exception {
        bookRepository = new BookRepositoryImpl();
        List<Book> bookList = Arrays.asList(
                new Book("book_1_name"),
                new Book("book_2_name"),
                new Book("book_3_name"),
                new Book("book_4_name")
        );
        bookRepository.setBookList(bookList.stream()
                .map(Optional::of)
                .collect(Collectors.toList()));
    }

    @Test
    public void save_Test() {
        Book newBook = new Book("book_5_name");
        bookRepository.save(newBook);
        assertTrue(bookRepository.getBookList().contains(Optional.of(newBook)));
    }

    @Test
    public void remove_Test() {
        Book removeBook = new Book("book_4_name");
        bookRepository.remove(removeBook);
        assertFalse(bookRepository.getBookList().contains(Optional.of(removeBook)));
    }

    @Test
    public void update_ExistBook_Test() {
        Book updatedBook = new Book("book_4_name", "book_4_description");
        bookRepository.update(updatedBook);

        assertEquals(Optional.of(updatedBook), bookRepository.getBookList().get(3));
    }

    @Test
    public void update_NonExistBook_Test() {
        Book updatedBook = new Book("book_5_name", "book_5_description");
        bookRepository.update(updatedBook);

        assertFalse(bookRepository.getBookList().contains(Optional.of(new Book("book_5_name", "book_5_description"))));
    }
}