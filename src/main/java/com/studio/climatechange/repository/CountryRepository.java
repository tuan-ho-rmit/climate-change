package com.studio.climatechange.repository;

import com.studio.climatechange.dto.CountryDto;
import com.studio.climatechange.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query(value = 
    "WITH Model AS (" +
    "    SELECT " +
    "        t.city_id, " +
    "        c.name AS City, " +
    "        AVG(t.average_temperature) AS AvgTemp, " +
    "        cn.name AS Country " +
    "    FROM temp t " +
    "    JOIN city c ON t.city_id = c.id " +
    "    JOIN country cn ON c.id = cn.id " +
    "    WHERE t.city_id = :cityId AND t.year BETWEEN 1990 AND :periodLength + 1990 " +
    "    GROUP BY t.city_id" +
    "), " +
    "Period AS (" +
    "    SELECT " +
    "        t.year, " +
    "        c.id, " +
    "        c.name AS City, " +
    "        AVG(t.average_temperature) AS AvgTemp, " +
    "        Cn.name AS Country " +
    "    FROM temp t " +
    "    JOIN city c ON t.city_id = c.id " +
    "    JOIN country cn ON c.id = cn.id " +
    "    WHERE t.city_id = :cityId " +
    "    GROUP BY t.year, t.year + :periodLength, t.city_id " +
    "    ORDER BY t.year" +
    ") " +
    "SELECT " +
    "    p.City, " +
    "    p.AvgTemp, " +
    "    p.Year AS StartYear, " +
    "    p.Year + :periodLength AS EndYear, " +
    "    p.AvgTemp - m.AvgTemp AS Difference " +
    "FROM Model m " +
    "CROSS JOIN Period p " +
    "ORDER BY p.Year",
    nativeQuery = true
)
Page<CountryDto> getListCountryLevel3Subtask3 (@Param("cityId") int cityId, @Param("periodLength") int periodLength, Pageable pageable);
}
