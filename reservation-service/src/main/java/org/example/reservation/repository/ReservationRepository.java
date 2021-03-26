package org.example.reservation.repository;

import org.example.reservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The repository for ReservationEntity
 */
@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Integer> {

    List<ReservationEntity> findByReservationFromDateAndReservationToDateAndBookingStatus(Date fromDate,
                                                                                          Date toDate,
                                                                                          String bookingStatus);

    Optional<ReservationEntity> findByRoomIdAndReservationFromDateAndReservationToDate(Integer roomId,
                                                                                 Date fromDate,
                                                                                 Date toDate);
}
