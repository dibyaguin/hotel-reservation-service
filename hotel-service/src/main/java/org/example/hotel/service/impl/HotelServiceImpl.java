package org.example.hotel.service.impl;

import org.apache.commons.collections4.CollectionUtils;
import org.example.hotel.entity.HotelEntity;
import org.example.hotel.mapper.HotelMapper;
import org.example.hotel.mapper.RoomMapper;
import org.example.hotel.model.Hotel;
import org.example.hotel.model.Reservation;
import org.example.hotel.model.Room;
import org.example.hotel.repository.HotelRepository;
import org.example.hotel.repository.RoomRepository;
import org.example.hotel.service.HotelService;
import org.example.hotel.service.helper.ReservationFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Hotel Service Implementation
 */
@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private HotelMapper hotelMapper;

    @Autowired
    private RoomMapper roomMapper;

    @Autowired
    private ReservationFeignClient reservationFeignClient;

    /**
     * @see {@link HotelService#getHotels()}
     */
    @Override
    public List<Hotel> getHotels() {
        return hotelMapper.convertEntityToModel(hotelRepository.findAll());
    }

    /**
     * ,@see{@link HotelService#getHotel(Integer)}
     */
    @Override
    public Hotel getHotel(Integer hotelId) {
        Hotel hotel = null;
        Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(hotelId);
        if (optionalHotelEntity.isPresent()) {
            hotel = hotelMapper.convertEntityToModel(optionalHotelEntity.get());
        }
        return hotel;
    }

    /**
     * @see {@link HotelService#createHotel(Hotel)}
     */
    @Override
    public Hotel createHotel(Hotel hotel) {
        HotelEntity savedHotelEntity = hotelRepository.save(hotelMapper.convertModelToEntity(hotel, new HotelEntity()));
        return hotelMapper.convertEntityToModel(savedHotelEntity);
    }

    /**
     * @see {@link HotelService#updateHotel(Hotel, Integer)}
     */
    @Override
    public Hotel updateHotel(Hotel hotel, Integer hotelId) {
        Hotel updatedHotel = null;
        if (Objects.nonNull(hotelId)) {
            Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(hotelId);
            if (optionalHotelEntity.isPresent()) {
                HotelEntity hotelEntity = optionalHotelEntity.get();
                HotelEntity updatedHotelEntity = hotelRepository.save(hotelMapper.convertModelToEntity(hotel, hotelEntity));
                updatedHotel = hotelMapper.convertEntityToModel(updatedHotelEntity);
            }
        }

        return updatedHotel;
    }

    /**
     * @see {@link HotelService#deleteHotel(Integer)}
     */
    @Override
    public boolean deleteHotel(Integer hotelId) {
        boolean isDeleted = false;
        if (Objects.nonNull(hotelId) && hotelRepository.existsById(hotelId)) {
            hotelRepository.deleteById(hotelId);
            isDeleted = true;
        }
        return isDeleted;
    }

    /**
     * @see {@link HotelService#getAvailableRooms(String, String)}
     */
    @Override
    public List<Room> getAvailableRooms(String fromDate, String toDate) {

        List<Reservation> reservations = reservationFeignClient.getReservedRooms(fromDate, toDate);

        List<Integer> reservedRoomIds = CollectionUtils.emptyIfNull(reservations)
                .stream()
                .filter(Objects::nonNull)
                .map(Reservation::getRoomId)
                .collect(Collectors.toList());

        return roomMapper.convertEntityToModel(
                roomRepository.findByIdNotIn(reservedRoomIds));

    }
}
