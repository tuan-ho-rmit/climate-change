package com.studio.climatechange.repository;

import com.studio.climatechange.dto.CountryDto;
import com.studio.climatechange.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface CountryRepository extends JpaRepository<Country, Long>{

    @Query("SELECT c FROM country c WHERE c.someField = :someValue")
    Page<CountryDto> findBySomeField(@Param("someValue") String someValue, Pageable pageable);
}
