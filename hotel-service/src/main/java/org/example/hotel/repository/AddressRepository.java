package org.example.hotel.repository;

import org.example.hotel.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The repository for AddressEntity
 */
@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {
}
