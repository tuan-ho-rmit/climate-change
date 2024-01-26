package com.studio.climatechange.services.impl;


import com.studio.climatechange.dto.PersonaDto;
import com.studio.climatechange.models.Persona;
import com.studio.climatechange.repository.PersonaRepository;
import com.studio.climatechange.services.PersonaServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaServiceImpl implements PersonaServiceInterface {
    private PersonaRepository personaRepository;

    @Autowired
    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

     @Override
    public List<PersonaDto> findAllPersonas() {
        List<Persona> personas = personaRepository.findAll();
        return personas.stream().map(persona-> mapToPersonaDto(persona)).collect(Collectors.toList());
    }

    private PersonaDto mapToPersonaDto (Persona persona) {
        PersonaDto personaDto = new PersonaDto();

        personaDto.setSkillsAndExperiences(persona.getSkillsAndExperiences());
        personaDto.setName(persona.getName());
        personaDto.setBackground(persona.getBackground());
        personaDto.setGoals(persona.getGoals());
        personaDto.setNeeds(persona.getNeeds());
        personaDto.setFrustration(persona.getFrustration());

        return personaDto;
    }
}
