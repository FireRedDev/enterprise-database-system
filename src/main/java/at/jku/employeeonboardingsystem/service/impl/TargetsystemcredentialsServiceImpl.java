package at.jku.employeeonboardingsystem.service.impl;

import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import at.jku.employeeonboardingsystem.repository.TargetsystemcredentialsRepository;
import at.jku.employeeonboardingsystem.service.TargetsystemcredentialsService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Targetsystemcredentials}.
 */
@Service
@Transactional
public class TargetsystemcredentialsServiceImpl implements TargetsystemcredentialsService {

    private final Logger log = LoggerFactory.getLogger(TargetsystemcredentialsServiceImpl.class);

    private final TargetsystemcredentialsRepository targetsystemcredentialsRepository;

    public TargetsystemcredentialsServiceImpl(TargetsystemcredentialsRepository targetsystemcredentialsRepository) {
        this.targetsystemcredentialsRepository = targetsystemcredentialsRepository;
    }

    @Override
    public Targetsystemcredentials save(Targetsystemcredentials targetsystemcredentials) {
        log.debug("Request to save Targetsystemcredentials : {}", targetsystemcredentials);
        return targetsystemcredentialsRepository.save(targetsystemcredentials);
    }

    @Override
    public Optional<Targetsystemcredentials> partialUpdate(Targetsystemcredentials targetsystemcredentials) {
        log.debug("Request to partially update Targetsystemcredentials : {}", targetsystemcredentials);

        return targetsystemcredentialsRepository
            .findById(targetsystemcredentials.getId())
            .map(existingTargetsystemcredentials -> {
                if (targetsystemcredentials.getUsername() != null) {
                    existingTargetsystemcredentials.setUsername(targetsystemcredentials.getUsername());
                }
                if (targetsystemcredentials.getPassword() != null) {
                    existingTargetsystemcredentials.setPassword(targetsystemcredentials.getPassword());
                }

                return existingTargetsystemcredentials;
            })
            .map(targetsystemcredentialsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Targetsystemcredentials> findAll(Pageable pageable) {
        log.debug("Request to get all Targetsystemcredentials");
        return targetsystemcredentialsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Targetsystemcredentials> findOne(Long id) {
        log.debug("Request to get Targetsystemcredentials : {}", id);
        return targetsystemcredentialsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Targetsystemcredentials : {}", id);
        targetsystemcredentialsRepository.deleteById(id);
    }

    @Override
    public List<Targetsystemcredentials> listAll() {
        return targetsystemcredentialsRepository.findAll(Sort.by("targetsystem").ascending());
    }
}
