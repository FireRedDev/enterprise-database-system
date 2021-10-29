package at.jku.enterprisedatabasesystem.service.impl;

import at.jku.enterprisedatabasesystem.domain.TargetSystemCredentials;
import at.jku.enterprisedatabasesystem.repository.TargetSystemCredentialsRepository;
import at.jku.enterprisedatabasesystem.service.TargetSystemCredentialsService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TargetSystemCredentials}.
 */
@Service
@Transactional
public class TargetSystemCredentialsServiceImpl implements TargetSystemCredentialsService {

    private final Logger log = LoggerFactory.getLogger(TargetSystemCredentialsServiceImpl.class);

    private final TargetSystemCredentialsRepository targetSystemCredentialsRepository;

    public TargetSystemCredentialsServiceImpl(TargetSystemCredentialsRepository targetSystemCredentialsRepository) {
        this.targetSystemCredentialsRepository = targetSystemCredentialsRepository;
    }

    @Override
    public TargetSystemCredentials save(TargetSystemCredentials targetSystemCredentials) {
        log.debug("Request to save TargetSystemCredentials : {}", targetSystemCredentials);
        return targetSystemCredentialsRepository.save(targetSystemCredentials);
    }

    @Override
    public Optional<TargetSystemCredentials> partialUpdate(TargetSystemCredentials targetSystemCredentials) {
        log.debug("Request to partially update TargetSystemCredentials : {}", targetSystemCredentials);

        return targetSystemCredentialsRepository
            .findById(targetSystemCredentials.getId())
            .map(existingTargetSystemCredentials -> {
                if (targetSystemCredentials.getUsername() != null) {
                    existingTargetSystemCredentials.setUsername(targetSystemCredentials.getUsername());
                }
                if (targetSystemCredentials.getPassword() != null) {
                    existingTargetSystemCredentials.setPassword(targetSystemCredentials.getPassword());
                }

                return existingTargetSystemCredentials;
            })
            .map(targetSystemCredentialsRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TargetSystemCredentials> findAll(Pageable pageable) {
        log.debug("Request to get all TargetSystemCredentials");
        return targetSystemCredentialsRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TargetSystemCredentials> findOne(Long id) {
        log.debug("Request to get TargetSystemCredentials : {}", id);
        return targetSystemCredentialsRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TargetSystemCredentials : {}", id);
        targetSystemCredentialsRepository.deleteById(id);
    }
}
