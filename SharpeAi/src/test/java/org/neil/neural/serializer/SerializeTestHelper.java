package org.neil.neural.serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class SerializeTestHelper {

    public static <T> T serializeThenUnserialize(T obj) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(obj);
        } catch (Exception e) {
            throw new IllegalStateException("class" + obj.getClass(),e);
        }
        try {

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return (T)assertInstanceOf( obj.getClass(), objectInputStream.readObject() );
        } catch (Exception e) {
            throw new IllegalStateException("class" + obj.getClass(),e);
        }

    }
}
