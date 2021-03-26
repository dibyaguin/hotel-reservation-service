package org.example.guest.service.impl;

import org.example.guest.entity.GuestEntity;
import org.example.guest.exception.BookingNotAvailableException;
import org.example.guest.exception.GuestNotFoundException;
import org.example.guest.mapper.GuestMapper;
import org.example.guest.model.Guest;
import org.example.guest.model.Hotel;
import org.example.guest.model.Reservation;
import org.example.guest.model.Room;
import org.example.guest.repository.GuestRepository;
import org.example.guest.service.helper.HotelFeignClient;
import org.example.guest.service.helper.ReservationFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
class GuestServiceImplTest {

    @InjectMocks
    private GuestServiceImpl guestServiceImpl;

    @Mock
    private GuestRepository guestRepository;

    @Mock
    private GuestMapper guestMapper;

    @Mock
    private HotelFeignClient hotelFeignClient;

    @Mock
    private ReservationFeignClient reservationFeignClient;

    @Mock
    private CircuitBreakerFactory circuitBreakerFactory;

    @Mock
    private CircuitBreaker mockCircuitBreaker;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerGuestTest() {
        Mockito.when(guestMapper.convertModelToEntity(any(Guest.class), any(GuestEntity.class)))
                .thenReturn(new GuestEntity());
        Mockito.when(guestRepository.save(any(GuestEntity.class))).thenReturn(new GuestEntity());
        Mockito.when(guestMapper.convertEntityToModel(any(GuestEntity.class))).thenReturn(new Guest());
        assertNotNull(guestServiceImpl.registerGuest(new Guest()));
    }

    @Test
    void getGuestTest() {
        Mockito.when(guestRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(new GuestEntity()));
        Mockito.when(guestMapper.convertEntityToModel(any(GuestEntity.class))).thenReturn(new Guest());
        assertNotNull(guestServiceImpl.getGuest(1));
    }

    @Test
    void getGuestNotFoundExceptionTest() {
        Mockito.when(guestRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(null));
        assertThrows(GuestNotFoundException.class, () -> {
            guestServiceImpl.getGuest(1);
        });
    }

    @Test
    void updateGuestTest() {
        Mockito.when(guestRepository.findById(any(Integer.class)))
                .thenReturn(Optional.of(new GuestEntity()));
        Mockito.when(guestMapper.convertModelToEntity(any(Guest.class), any(GuestEntity.class)))
                .thenReturn(new GuestEntity());
        Mockito.when(guestRepository.save(any(GuestEntity.class))).thenReturn(new GuestEntity());
        Mockito.when(guestMapper.convertEntityToModel(any(GuestEntity.class))).thenReturn(new Guest());
        assertNotNull(guestServiceImpl.updateGuest(new Guest(), 1));

    }

    @Test
    void getGuestNotFoundExceptionTestForUpdateGuest() {
        Mockito.when(guestRepository.findById(any(Integer.class)))
                .thenReturn(Optional.ofNullable(null));
        assertThrows(GuestNotFoundException.class, () -> {
            guestServiceImpl.updateGuest(new Guest(), 1);
        });
    }

    @Test
    void deleteGuestTest() {
        Mockito.when(guestRepository.existsById(anyInt())).thenReturn(true);
        Mockito.doNothing().when(guestRepository).deleteById(anyInt());
        assertTrue(guestServiceImpl.deleteGuest(1));

    }

    @Test
    void viewHotelsTest() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel());

        Mockito.when(circuitBreakerFactory.create(anyString())).thenReturn(mockCircuitBreaker);
        Mockito.when(hotelFeignClient.viewHotels()).thenReturn(hotels);
        Mockito.when( mockCircuitBreaker.run(any(), any())).thenReturn(hotels);

        assertNotNull(guestServiceImpl.viewHotels());
    }

    @Test
    void viewAvailableRoomsTest() {
        List<Room> rooms = new ArrayList<>();
        rooms.add(new Room());

        Mockito.when(hotelFeignClient.getAvailableRooms(anyString(), anyString()))
                .thenReturn(rooms);
        assertNotNull(guestServiceImpl.viewAvailableRooms("2021-04-12", "2021-04-15"));
    }

    @Test
    void bookReservationTest() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(1);
        reservation.setReservationFromDate(new Date());
        reservation.setReservationToDate(new Date());

        Mockito.when(reservationFeignClient.getAvailableReservation(anyInt(), anyString(), anyString()))
                .thenReturn(true);

        Mockito.when(reservationFeignClient.bookReservation(any(Reservation.class)))
                .thenReturn(reservation);

        assertNotNull(guestServiceImpl.bookReservation(reservation));
    }

    @Test
    void bookReservationNotAvailableExceptionTest() {
        Reservation reservation = new Reservation();
        reservation.setRoomId(1);
        reservation.setReservationFromDate(new Date());
        reservation.setReservationToDate(new Date());

        Mockito.when(reservationFeignClient.getAvailableReservation(anyInt(), anyString(), anyString()))
                .thenReturn(false);

        assertThrows(BookingNotAvailableException.class, () -> {
            guestServiceImpl.bookReservation(reservation);
        });
    }

    @Test
    void viewHotelsFallbackTest() {
        assertNotNull(guestServiceImpl.fallbackViewHotels());
    }
}