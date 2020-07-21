package org.example.context;

import lombok.Getter;
import org.example.service.BookService;
import org.example.service.UserService;
import org.example.service.impl.BookServiceImpl;
import org.example.service.impl.UserServiceImpl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

@Getter
public class ApplicationContext {
    private static ApplicationContext context = null;
    private BookService bookService;
    private UserService userService;

    private ApplicationContext() {
        bookService = new BookServiceImpl();
        userService = new UserServiceImpl();
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            context = new ApplicationContext();
        }
        return context;
    }

    public void clear() {
        try {
            FileOutputStream writeData = new FileOutputStream("src/main/resources/db/BookRepository.txt");
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);
            writeStream.reset();
            writeStream.close();

            writeData = new FileOutputStream("src/main/resources/db/UserRepository.txt");
            writeStream = new ObjectOutputStream(writeData);
            writeStream.reset();
            writeStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
