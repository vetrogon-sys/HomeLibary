package org.example.reposytory.impl;

import org.example.entity.Book;
import org.example.exception.CustomIOException;
import org.example.reposytory.BookRepository;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookRepositoryImpl implements BookRepository {
    private static final String BOOK_REPOSITORY_PATH = "src/main/resources/db/BookRepository.txt";

    @Override
    public List<Optional<Book>> getAll() throws CustomIOException {
        try {
            FileInputStream readData = new FileInputStream(BOOK_REPOSITORY_PATH);
            ObjectInputStream readStream = new ObjectInputStream(readData);
            ArrayList<Book> bookArrayList = (ArrayList<Book>) readStream.readObject();

            readStream.close();

            return bookArrayList.stream()
                    .map(Optional::of)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new CustomIOException("Can't read data");
        }
    }

    @Override
    public void save(Book book) throws CustomIOException {
        List<Book> bookList = getBookList();
        bookList.add(book);
        dbRefresh(bookList);
    }

    @Override
    public void remove(Book book) throws CustomIOException {
        List<Book> bookList = getBookList();
        bookList.remove(book);
        dbRefresh(bookList);
    }

    @Override
    public void update(Book book) throws CustomIOException {
        List<Book> bookList = getBookList();
        bookList = bookList.stream()
                .filter(e -> !e.getName().equals(book.getName()))
                .collect(Collectors.toList());
        bookList.add(book);
        dbRefresh(bookList);
    }

    private List<Book> getBookList() {
        List<Book> bookList;
        try {
            bookList = getAll().stream()
                    .map(Optional::get)
                    .collect(Collectors.toList());
        } catch (CustomIOException ignored) {
            bookList = new ArrayList<>();
        }
        return bookList;
    }

    private void dbRefresh(List<Book> bookList) {
        try {
            FileOutputStream writeData = new FileOutputStream(BOOK_REPOSITORY_PATH);
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(bookList);
            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
