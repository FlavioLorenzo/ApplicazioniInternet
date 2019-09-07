package com.internet_application.backend.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.internet_application.backend.Entities.ReservationEntity;
import com.internet_application.backend.PostBodies.ReservationPostBody;

import java.util.List;

public interface ReservationService {
    ReservationEntity getReservationById(Long reservationId);
    JsonNode getAllReservationForLineAndData(Long lineId, String date);
    List<ReservationEntity> getNReservationsByChildFromDate(Long lineId, Long childId, String date, Integer n);
    ReservationEntity addReservation(Long lineId, String date, ReservationPostBody rpb);
    ReservationEntity updateReservation(Long lineId, String date, Long reservationId, ReservationPostBody rpb);
    ReservationEntity getReservation(Long lineId, String date, Long reservationId);
    void deleteReservation(Long lineId, String date, Long reservationId);
}
