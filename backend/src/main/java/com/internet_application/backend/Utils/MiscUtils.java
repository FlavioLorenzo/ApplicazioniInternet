package com.internet_application.backend.Utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class MiscUtils {

    /**
     * Translate the direction into a human comprehensible information
     * @param direction The boolean representing the direction
     * @return The string version of the direction
     */
    public static String directionBoolToString(boolean direction) {
        if(direction)
            return "Ritorno";
        else
            return "Andata";
    }

    public static File[] getResourceFolderFiles (String folder) {
        /* ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath(); */

        URL url = TypeReference.class.getResource(folder);
        String path = url.getPath();

        return new File(path).listFiles();
    }
}
