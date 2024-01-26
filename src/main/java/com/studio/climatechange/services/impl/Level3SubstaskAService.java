package com.studio.climatechange.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;

import com.studio.climatechange.dto.StudentDto;
import com.studio.climatechange.models.Student;
import com.studio.climatechange.repository.Level3SubtaskARepository;
import com.studio.climatechange.services.Level3SubstaskAServiceInterface;
import com.studio.climatechange.viewModel.level3SubtaskA.Table;

public class Level3SubstaskAService implements Level3SubstaskAServiceInterface {
    private Level3SubtaskARepository level3SubtaskARepository;

    @Autowired
    public Level3SubstaskAService(Level3SubtaskARepository level3SubtaskARepository) {
        this.level3SubtaskARepository = level3SubtaskARepository;
    }

    @Override
    public Table processClimateChangeQuery(int startYear1, int startYear2, int period) {
        Table table = level3SubtaskARepository.processClimateChangeQuery(startYear1, startYear2, period);

        return table;
    }

}
