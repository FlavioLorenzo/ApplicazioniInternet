package com.internet_application.backend.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.ReservationEntity;
import com.internet_application.backend.PostBodies.ReservationPostBody;

public interface ReservationService {
    JsonNode getAllReservationForLineAndData(Long lineId, String date);
    ReservationEntity addReservation(Long lineId, String date, ReservationPostBody rpb);
    ReservationEntity updateReservation(Long lineId, String date, Long reservationId, ReservationPostBody rpb);
    ReservationEntity getReservation(Long lineId, String date, Long reservationId);
    void deleteReservation(Long lineId, String date, Long reservationId);
}
