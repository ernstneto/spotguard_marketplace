package com.spotguard.marketplace.modules.space.entity;

import com.spotguard.marketplace.core.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;
import com.spotguard.marketplace.core.enums.SpaceType;
import com.spotguard.marketplace.core.enums.SpaceStatus;

import java.math.BigDecimal;

@Entity
@Table(name = "spaces")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Space extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Column(nullable = false)
    private String address;

    @Column(columnDefinition = "geometry(Point, 4326)")
    private Point location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SpaceStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /**
     * Cria um Point PostGIS a partir de latitude e longitude.
     * SRID 4326 = WGS84 (padrão GPS).
     *
     * @param latitude  latitude (-90 a 90)
     * @param longitude longitude (-180 a 180)
     * @return Point JTS ou null se coordenadas inválidas
     */
    public static Point createLocation(Double latitude, Double longitude) {
        if (latitude == null || longitude == null) {
            return null;
        }
        org.locationtech.jts.geom.GeometryFactory factory =
                new org.locationtech.jts.geom.GeometryFactory(
                        new org.locationtech.jts.geom.PrecisionModel(), 4326);
        return factory.createPoint(
                new org.locationtech.jts.geom.Coordinate(longitude, latitude));
    }

    /**
     * Extrai latitude do Point armazenado (coordenada Y).
     */
    public Double getLatitude() {
        return location != null ? location.getY() : null;
    }

    /**
     * Extrai longitude do Point armazenado (coordenada X).
     */
    public Double getLongitude() {
        return location != null ? location.getX() : null;
    }

}
