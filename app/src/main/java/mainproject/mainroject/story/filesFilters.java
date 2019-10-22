package mainproject.mainroject.story;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.nearby.connection.Payload;

import java.io.File;
import java.io.FileFilter;

public class filesFilters implements FileFilter {
    File file;
    BitmapFactory.Options options = new BitmapFactory.Options();

    private final String[] imageExtensions = new String[]{"jpg","png","gif","jpeg"};
    public filesFilters(File newFile){
        this.file = newFile;
    }

    @Override
    public boolean accept(File file) {
        for (String extension:
                imageExtensions
             ) {
            if(file.getName().toLowerCase().endsWith(extension)){
                options.inJustDecodeBounds = true;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(),options);
                if(options.outWidth != -1 && options.outHeight != -1)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
