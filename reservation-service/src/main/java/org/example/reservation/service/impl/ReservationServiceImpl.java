package org.example.reservation.service.impl;

import org.example.reservation.constant.BookingStatus;
import org.example.reservation.entity.ReservationEntity;
import org.example.reservation.mapper.ReservationMapper;
import org.example.reservation.model.Reservation;
import org.example.reservation.repository.ReservationRepository;
import org.example.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * The implementation class of ReservationService
 */
@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationMapper reservationMapper;

    /**
     * @see {@link ReservationService#getReservedRooms(Date, Date)}
     */
    @Override
    public List<Reservation> getReservedRooms(Date fromDate, Date toDate) {
        List<ReservationEntity> reservationEntities = reservationRepository.
                findByReservationFromDateAndReservationToDateAndBookingStatus(fromDate,
                        toDate, BookingStatus.BOOKED.getStatus());
        return reservationMapper.convertEntityToModel(reservationEntities);
    }

    /**
     * @see {@link ReservationService#saveReservation(Reservation)}
     */
    @Override
    public Reservation saveReservation(Reservation reservation) {
        ReservationEntity savedReservationEntity = reservationRepository.save(
                reservationMapper.convertModelToEntity(reservation));
        return reservationMapper.convertEntityToModel(savedReservationEntity);
    }

    /**
     * @see {@link ReservationService#getAvailableReservation(Integer, Date, Date)}
     */
    @Override
    public Boolean getAvailableReservation(Integer roomId, Date fromDate, Date toDate) {
        Optional<ReservationEntity> optionalReservationEntity =
                reservationRepository.findByRoomIdAndReservationFromDateAndReservationToDate(
                        roomId, fromDate, toDate);

       return optionalReservationEntity.isEmpty() ? Boolean.TRUE : Boolean.FALSE;
    }


}
