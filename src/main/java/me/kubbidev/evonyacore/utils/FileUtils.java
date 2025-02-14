package me.kubbidev.evonyacore.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileUtils {

    public static void recursiveCopy(File fSource, File fDest) {
        try {
            if (fSource.isDirectory()) {
                if (!fDest.exists())
                    fDest.mkdirs();
                final String[] fList = fSource.list();
                assert fList != null;
                for (String s : fList) {
                    File dest = new File(fDest, s);
                    File source = new File(fSource, s);
                    recursiveCopy(source, dest);
                }
            } else {
                final FileInputStream fInStream = new FileInputStream(fSource);
                final FileOutputStream fOutStream = new FileOutputStream(fDest);
                final byte[] buffer = new byte[2048];
                int iBytesReads;
                while ((iBytesReads = fInStream.read(buffer)) >= 0)
                    fOutStream.write(buffer, 0, iBytesReads);
                fInStream.close();
                fOutStream.close();
            }
        } catch (Exception ignored) {
        }
    }

    public static boolean deleteFile(File file) {
        if (file == null)
            return false;
        if (file.isFile())
            return file.delete();
        if (!file.isDirectory())
            return false;
        final File[] flist = file.listFiles();
        if (flist != null && flist.length > 0)
            for (File f : flist) {
                if (!deleteFile(f))
                    return false;
            }
        return file.delete();
    }
}
