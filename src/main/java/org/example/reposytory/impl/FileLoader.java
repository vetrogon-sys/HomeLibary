package org.example.reposytory.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class FileLoader {

    public <T> ArrayList<T> getObjectList(Class<T> type, String path) {
        ArrayList<T> list = null;
        try {
            FileInputStream readData = new FileInputStream(path);
            ObjectInputStream readStream = new ObjectInputStream(readData);

            try {
                list = (ArrayList<T>) readStream.readObject();
            } catch (ClassNotFoundException ignored) {
            }

            readStream.close();
        } catch (IOException ignored) {
        }

        if (list != null) {
            return list;
        } else {
            return new ArrayList<>();
        }
    }
}
