package com.studio.climatechange.repository;

import java.util.ArrayList;

import org.hibernate.mapping.List;
import org.hibernate.mapping.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studio.climatechange.viewModel.level3SubtaskA.Table;

public interface Level3SubtaskARepository {
    @Query(nativeQuery = true, value ="")

    Table processClimateChangeQuery(
            @Param("startYear1") int startYear1,
            @Param("startYear2") int startYear2,
            @Param("period2") int period);}

