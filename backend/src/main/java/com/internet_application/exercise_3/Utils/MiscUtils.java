package com.internet_application.exercise_3.Utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    /**
     * Translate the direction into a human comprehensible information
     * @param direction The boolean representing the direction
     * @return The string version of the direction
     */
    public static String directionBoolToString(boolean direction) {
        if(direction)
            return "Return";
        else
            return "Outward";
    }
}
