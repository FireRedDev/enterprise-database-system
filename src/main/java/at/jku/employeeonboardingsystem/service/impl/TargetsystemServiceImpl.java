package at.jku.employeeonboardingsystem.service.impl;

import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.jdbc.TargetSystemJdbc;
import at.jku.employeeonboardingsystem.repository.TargetsystemRepository;
import at.jku.employeeonboardingsystem.service.TargetsystemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Targetsystem}.
 */
@Service
@Transactional
public class TargetsystemServiceImpl implements TargetsystemService {

    private final Logger log = LoggerFactory.getLogger(TargetsystemServiceImpl.class);

    private final TargetsystemRepository targetsystemRepository;

    public TargetsystemServiceImpl(TargetsystemRepository targetsystemRepository) {
        this.targetsystemRepository = targetsystemRepository;
    }

    @Override
    public Targetsystem save(Targetsystem targetsystem) {
        log.debug("Request to save Targetsystem : {}", targetsystem);
        if (targetsystem.getType().equals("db")) {
            try {
                TargetSystemJdbc.copyDatabaseData(targetsystem.getUrl(), targetsystem.getUsername(), targetsystem.getPassword());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targetsystemRepository.save(targetsystem);
    }

    @Override
    public Optional<Targetsystem> partialUpdate(Targetsystem targetsystem) {
        log.debug("Request to partially update Targetsystem : {}", targetsystem);

        return targetsystemRepository
            .findById(targetsystem.getId())
            .map(existingTargetsystem -> {
                if (targetsystem.getName() != null) {
                    existingTargetsystem.setName(targetsystem.getName());
                }
                if (targetsystem.getType() != null) {
                    existingTargetsystem.setType(targetsystem.getType());
                }
                if (targetsystem.getUrl() != null) {
                    existingTargetsystem.setUrl(targetsystem.getUrl());
                }
                if (targetsystem.getPassword() != null) {
                    existingTargetsystem.setPassword(targetsystem.getPassword());
                }
                if (targetsystem.getUsername() != null) {
                    existingTargetsystem.setUsername(targetsystem.getUsername());
                }
                return existingTargetsystem;
            })
            .map(targetsystemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Targetsystem> findAll(Pageable pageable) {
        log.debug("Request to get all Targetsystems");
        return targetsystemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Targetsystem> findOne(Long id) {
        log.debug("Request to get Targetsystem : {}", id);
        return targetsystemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Targetsystem : {}", id);
        targetsystemRepository.deleteById(id);
    }
}
