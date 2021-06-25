package com.sheng.mvvm;

import android.text.TextUtils;

import androidx.annotation.NonNull;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author GuoFeng
 * @date :2019/1/17 16:47
 * @description:
 */
public class TUtil {

    public static <T> T getNewInstance(Object object, int i) {
        if (object != null) {
            try {
                return ((Class<T>) ((ParameterizedType) (object.getClass()
                        .getGenericSuperclass())).getActualTypeArguments()[i])
                        .newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassCastException e) {
                e.printStackTrace();
            }

        }
        return null;

    }

    public static <T> T getInstance(Object object, int i) {
        if (object != null) {
            try {
                return (T) ((ParameterizedType) object.getClass().getGenericSuperclass()).getActualTypeArguments()[i];
            } catch (Exception e) {
                return getInstance(object, 0);
            }
        }
        return null;
    }

    public static @NonNull
    <T> T checkNotNull(final T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <E> List<E> deepCopy(List<E> src) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            @SuppressWarnings("unchecked")
            List<E> dest = (List<E>) in.readObject();
            return dest;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<E>();
        }
    }

}
