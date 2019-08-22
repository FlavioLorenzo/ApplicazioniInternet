package com.internet_application.backend.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
}
