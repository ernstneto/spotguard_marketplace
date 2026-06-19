package com.spotguard.marketplace.modules.space.repository;

import com.spotguard.marketplace.modules.space.entity.Space;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.spotguard.marketplace.core.enums.SpaceStatus;

public interface SpaceRepository extends JpaRepository<Space, Long> {

    List<Space> findByStatus(SpaceStatus status);

    List<Space> findByOwnerId(Long ownerId);

    @Query(value = """
            SELECT s.* FROM spaces s
            WHERE s.status = 'AVAILABLE'
            AND ST_DWithin(
                s.location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326),
                :radiusInMeters
            )
            ORDER BY ST_Distance(
                s.location,
                ST_SetSRID(ST_MakePoint(:longitude, :latitude), 4326)
            )
            """, nativeQuery = true)
    List<Space> findNearby(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radiusInMeters") double radiusInMeters);
}
