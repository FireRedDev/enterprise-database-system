package at.jku.employeeonboardingsystem.service.impl;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.repository.SystemuserRepository;
import at.jku.employeeonboardingsystem.service.SystemuserService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Systemuser}.
 */
@Service
@Transactional
public class SystemuserServiceImpl implements SystemuserService {

    private final Logger log = LoggerFactory.getLogger(SystemuserServiceImpl.class);

    private final SystemuserRepository systemuserRepository;

    public SystemuserServiceImpl(SystemuserRepository systemuserRepository) {
        this.systemuserRepository = systemuserRepository;
    }

    @Override
    public Systemuser save(Systemuser systemuser) {
        log.debug("Request to save Systemuser : {}", systemuser);
        return systemuserRepository.save(systemuser);
    }

    @Override
    public Optional<Systemuser> partialUpdate(Systemuser systemuser) {
        log.debug("Request to partially update Systemuser : {}", systemuser);

        return systemuserRepository
            .findById(systemuser.getId())
            .map(existingSystemuser -> {
                if (systemuser.getEntryDate() != null) {
                    existingSystemuser.setEntryDate(systemuser.getEntryDate());
                }

                return existingSystemuser;
            })
            .map(systemuserRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Systemuser> findAll(Pageable pageable) {
        log.debug("Request to get all Systemusers");
        return systemuserRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Systemuser> findOne(Long id) {
        log.debug("Request to get Systemuser : {}", id);
        return systemuserRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Systemuser : {}", id);
        systemuserRepository.deleteById(id);
    }
}
