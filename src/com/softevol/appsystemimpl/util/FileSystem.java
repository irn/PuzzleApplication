package com.softevol.appsystemimpl.util;

import android.os.Environment;

import java.io.File;

/**
 * User: antony
 * Date: 1/27/13
 * Time: 7:21 PM
 */
public class FileSystem {

    public static final File PROJECT_DIRECTORY = new File(Environment.getExternalStorageDirectory(), ".puzzles");

    static {
        if (!PROJECT_DIRECTORY.exists()) {
            PROJECT_DIRECTORY.mkdirs();
        }
    }

    public static File getPuzzleFileFromId(long id) {
        File puzzleFile = new File(PROJECT_DIRECTORY, id + ".jpg");
        if (puzzleFile.exists()) {
            return puzzleFile;
        } else {
            return null;
        }
    }

}
