package org.example.reservation.service.impl;

import org.example.reservation.entity.ReservationEntity;
import org.example.reservation.mapper.ReservationMapper;
import org.example.reservation.model.Reservation;
import org.example.reservation.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationServiceImpl;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getReservedRoomsTest() {
        List<ReservationEntity> reservationEntities = new ArrayList<>();
        reservationEntities.add(new ReservationEntity());

        List<Reservation> reservations = new ArrayList<>();
        reservations.add(new Reservation());

        Mockito.when(reservationRepository.
                findByReservationFromDateAndReservationToDateAndBookingStatus(
                        any(Date.class), any(Date.class), anyString()))
                .thenReturn(reservationEntities);

        Mockito.when(reservationMapper.convertEntityToModel(anyList())).thenReturn(reservations);
        assertNotNull(reservationServiceImpl.getReservedRooms(new Date(), new Date()));
    }

    @Test
    void saveReservationTest() {
        Mockito.when(reservationMapper.convertModelToEntity(any(Reservation.class)))
                .thenReturn(new ReservationEntity());
        Mockito.when(reservationRepository.save(any(ReservationEntity.class)))
                .thenReturn(new ReservationEntity());
        Mockito.when(reservationMapper.convertEntityToModel(any(ReservationEntity.class)))
                .thenReturn(new Reservation());
        assertNotNull(reservationServiceImpl.saveReservation(new Reservation()));
    }

    @Test
    void getAvailableReservationTest() {
        Mockito.when(reservationRepository.findByRoomIdAndReservationFromDateAndReservationToDate(
                anyInt(), any(Date.class), any(Date.class)))
                .thenReturn(Optional.empty());
        assertTrue(reservationServiceImpl.getAvailableReservation(1, new Date(), new Date()));

    }
}