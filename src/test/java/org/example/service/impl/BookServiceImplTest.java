package org.example.service.impl;

import org.example.entity.Book;
import org.example.entity.Page;
import org.example.reposytory.impl.BookRepositoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServiceImplTest {

    private BookRepositoryImpl bookRepository = mock(BookRepositoryImpl.class);
    private BookServiceImpl bookService;

    @Before
    public void setUp() throws Exception {
        bookService = new BookServiceImpl();
        bookService.setBookRepository(bookRepository);
    }

    @Test
    public void getPageOfBook_Test() {
        when(bookRepository.getBookList()).thenReturn(Arrays.asList(
                Optional.of(new Book("name_1")),
                Optional.of(new Book("name_2")),
                Optional.of(new Book("name_3")),
                Optional.of(new Book("name_4")),
                Optional.of(new Book("name_5")),
                Optional.of(new Book("name_6")),
                Optional.of(new Book("name_7")),
                Optional.of(new Book("name_8")),
                Optional.of(new Book("name_9")),
                Optional.of(new Book("name_10"))
        ));

        assertEquals(5L, bookService.getPageOfBook(new Page(5L, 0)).size());
        assertFalse(bookService.getPageOfBook(new Page(5L, 1)).contains(Optional.of(new Book("name_5"))));
    }

    @Test
    public void findByName_Test() {
        when(bookRepository.getBookList()).thenReturn(Arrays.asList(
                Optional.of(new Book("name_1")),
                Optional.of(new Book("name_2")),
                Optional.of(new Book("name_3"))
        ));

        assertEquals(Optional.of(new Book("name_1")), bookService.findByName("name_1"));
        assertFalse(bookService.findByName("qweAsd").isPresent());
    }

    @Test
    public void save_Test() {
        when(bookRepository.getBookList()).thenReturn(Arrays.asList(
                Optional.of(new Book("name_1", "book_description")),
                Optional.of(new Book("name_2", "book_description")),
                Optional.of(new Book("name_3", "book_description"))
        ));

        assertFalse(bookService.save(new Book("name_1", "book_description")));
        assertTrue(bookService.save(new Book("name_4", "book_description")));
    }

    @Test
    public void remove_Test() {
        when(bookRepository.getBookList()).thenReturn(Arrays.asList(
                Optional.of(new Book("name_1", "book_description")),
                Optional.of(new Book("name_2", "book_description")),
                Optional.of(new Book("name_3", "book_description"))
        ));

        assertTrue(bookService.remove(new Book("name_1", "book_description")));
        assertFalse(bookService.remove(new Book("name_4", "book_description")));
    }

    @Test
    public void update_Test() {
        when(bookRepository.getBookList()).thenReturn(Arrays.asList(
                Optional.of(new Book("name_1", "book_description")),
                Optional.of(new Book("name_2", "book_description")),
                Optional.of(new Book("name_3", "book_description"))
        ));

        assertTrue(bookService.update(new Book("name_1", "book_description_1")));
        assertFalse(bookService.update(new Book("name_4", "book_description")));
    }
}