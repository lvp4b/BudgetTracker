package edu.umkc.lvp4b.budgettracker.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;

public class ImageSerializer {
    @Nullable
    public static Bitmap deserialize(@Nullable byte[] data){
        if (data == null) {
            return null;
        }

        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Nullable
    public static byte[] serialize(@Nullable Bitmap image){
        if (image == null){
            return null;
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
