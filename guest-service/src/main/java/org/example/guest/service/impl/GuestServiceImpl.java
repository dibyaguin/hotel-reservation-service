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
import org.example.guest.service.GuestService;
import org.example.guest.service.helper.HotelFeignClient;
import org.example.guest.service.helper.ReservationFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The implementation class of GuestService
 */
@Service
public class GuestServiceImpl implements GuestService {

    @Autowired
    private GuestRepository guestRepository;

    @Autowired
    private GuestMapper guestMapper;

    @Autowired
    private HotelFeignClient hotelFeignClient;

    @Autowired
    private ReservationFeignClient reservationFeignClient;

    @Autowired
    private CircuitBreakerFactory circuitBreakerFactory;

    private static final String CIRCUIT_BREAKER_ID = "circuitbreaker";

    /**
     * @see {@link GuestService#registerGuest(Guest)}
     */
    @Override
    public Guest registerGuest(Guest guest) {
        GuestEntity savedGuestEntity = guestRepository.save(guestMapper.convertModelToEntity(guest, new GuestEntity()));
        return guestMapper.convertEntityToModel(savedGuestEntity);
    }

    /**
     * @see {@link GuestService#getGuest(Integer)}
     */
    @Override
    public Guest getGuest(Integer guestId) {
        Guest guest = null;
        Optional<GuestEntity> optionalGuestEntity = guestRepository.findById(guestId);
        if (optionalGuestEntity.isPresent()) {
            guest = guestMapper.convertEntityToModel(optionalGuestEntity.get());
        } else {
            throw new GuestNotFoundException("Guest Id: " + guestId + " not found");
        }
        return guest;
    }

    /**
     * @see {@link GuestService#updateGuest(Guest, Integer)}
     */
    @Override
    public Guest updateGuest(Guest guest, Integer guestId) {
        Guest updatedGuest = null;
        if (Objects.nonNull(guestId)) {
            Optional<GuestEntity> optionalGuestEntity = guestRepository.findById(guestId);
            if (optionalGuestEntity.isPresent()) {
                GuestEntity guestEntity = optionalGuestEntity.get();
                GuestEntity updatedGuestEntity = guestRepository.save(guestMapper.convertModelToEntity(guest, guestEntity));
                updatedGuest = guestMapper.convertEntityToModel(updatedGuestEntity);
            } else {
                throw new GuestNotFoundException("Guest Id: " + guestId + " not found");
            }
        }

        return updatedGuest;
    }

    /**
     * @see {@link GuestService#deleteGuest(Integer)}
     */
    @Override
    public boolean deleteGuest(Integer guestId) {
        boolean isDeleted = false;
        if (Objects.nonNull(guestId) && guestRepository.existsById(guestId)) {
            guestRepository.deleteById(guestId);
            isDeleted = true;
        }
        return isDeleted;
    }

    /**
     * @see {@link GuestService#viewHotels()}
     */
    @Override
    public List<Hotel> viewHotels() {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create(CIRCUIT_BREAKER_ID);
        return  circuitBreaker.run(() -> hotelFeignClient.viewHotels(),
                throwable -> fallbackViewHotels());
    }

    /**
     * This is a fallback method for viewHotels()
     *
     * @return
     */
    public List<Hotel> fallbackViewHotels() {
        List<Hotel> hotels = new ArrayList<>();
        hotels.add(new Hotel());
        return hotels;
    }

    /**
     * @see {@link GuestService#viewAvailableRooms(String, String)}
     */
    @Override
    public List<Room> viewAvailableRooms(String startDate, String endDate) {
        return hotelFeignClient.getAvailableRooms(startDate, endDate);
    }

    /**
     * @see {@link GuestService#bookReservation(Reservation)}
     */
    @Override
    public Reservation bookReservation(Reservation reservation) {
        if (reservationFeignClient.getAvailableReservation(reservation.getRoomId(),
                reservation.getReservationFromDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().toString(),
                reservation.getReservationToDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().toString()).booleanValue()) {

            return reservationFeignClient.bookReservation(reservation);

        } else {
            throw new BookingNotAvailableException("Room id: " + reservation.getRoomId() +
                    " not available.");
        }
    }

}
