package at.jku.enterprisedatabasesystem.service.impl;

import at.jku.enterprisedatabasesystem.domain.TargetSystem;
import at.jku.enterprisedatabasesystem.repository.TargetSystemRepository;
import at.jku.enterprisedatabasesystem.service.TargetSystemService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TargetSystem}.
 */
@Service
@Transactional
public class TargetSystemServiceImpl implements TargetSystemService {

    private final Logger log = LoggerFactory.getLogger(TargetSystemServiceImpl.class);

    private final TargetSystemRepository targetSystemRepository;

    public TargetSystemServiceImpl(TargetSystemRepository targetSystemRepository) {
        this.targetSystemRepository = targetSystemRepository;
    }

    @Override
    public TargetSystem save(TargetSystem targetSystem) {
        log.debug("Request to save TargetSystem : {}", targetSystem);
        return targetSystemRepository.save(targetSystem);
    }

    @Override
    public Optional<TargetSystem> partialUpdate(TargetSystem targetSystem) {
        log.debug("Request to partially update TargetSystem : {}", targetSystem);

        return targetSystemRepository
            .findById(targetSystem.getId())
            .map(existingTargetSystem -> {
                if (targetSystem.getName() != null) {
                    existingTargetSystem.setName(targetSystem.getName());
                }

                return existingTargetSystem;
            })
            .map(targetSystemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TargetSystem> findAll(Pageable pageable) {
        log.debug("Request to get all TargetSystems");
        return targetSystemRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TargetSystem> findOne(Long id) {
        log.debug("Request to get TargetSystem : {}", id);
        return targetSystemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TargetSystem : {}", id);
        targetSystemRepository.deleteById(id);
    }
}
