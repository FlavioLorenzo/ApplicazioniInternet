package com.internet_application.backend.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@SuppressWarnings("Duplicates")
public class MiscUtils {
    public static Date dateParser(String date) throws ResponseStatusException {
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d = ft.parse(date);
            return d;
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static Date timeParser(String date) throws ResponseStatusException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        try {
            Date d = dateFormat.parse(date);
            return d;
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public static String timeToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(date);
    }

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
