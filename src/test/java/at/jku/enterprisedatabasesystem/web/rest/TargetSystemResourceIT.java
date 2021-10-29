package at.jku.enterprisedatabasesystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.jku.enterprisedatabasesystem.IntegrationTest;
import at.jku.enterprisedatabasesystem.domain.TargetSystem;
import at.jku.enterprisedatabasesystem.repository.TargetSystemRepository;
import at.jku.enterprisedatabasesystem.service.criteria.TargetSystemCriteria;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TargetSystemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TargetSystemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/target-systems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TargetSystemRepository targetSystemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTargetSystemMockMvc;

    private TargetSystem targetSystem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TargetSystem createEntity(EntityManager em) {
        TargetSystem targetSystem = new TargetSystem().name(DEFAULT_NAME);
        return targetSystem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TargetSystem createUpdatedEntity(EntityManager em) {
        TargetSystem targetSystem = new TargetSystem().name(UPDATED_NAME);
        return targetSystem;
    }

    @BeforeEach
    public void initTest() {
        targetSystem = createEntity(em);
    }

    @Test
    @Transactional
    void createTargetSystem() throws Exception {
        int databaseSizeBeforeCreate = targetSystemRepository.findAll().size();
        // Create the TargetSystem
        restTargetSystemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isCreated());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeCreate + 1);
        TargetSystem testTargetSystem = targetSystemList.get(targetSystemList.size() - 1);
        assertThat(testTargetSystem.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTargetSystemWithExistingId() throws Exception {
        // Create the TargetSystem with an existing ID
        targetSystem.setId(1L);

        int databaseSizeBeforeCreate = targetSystemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetSystemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTargetSystems() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get all the targetSystemList
        restTargetSystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTargetSystem() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get the targetSystem
        restTargetSystemMockMvc
            .perform(get(ENTITY_API_URL_ID, targetSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(targetSystem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getTargetSystemsByIdFiltering() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        Long id = targetSystem.getId();

        defaultTargetSystemShouldBeFound("id.equals=" + id);
        defaultTargetSystemShouldNotBeFound("id.notEquals=" + id);

        defaultTargetSystemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTargetSystemShouldNotBeFound("id.greaterThan=" + id);

        defaultTargetSystemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTargetSystemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTargetSystemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get all the targetSystemList where name equals to DEFAULT_NAME
        defaultTargetSystemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the targetSystemList where name equals to UPDATED_NAME
        defaultTargetSystemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get all the targetSystemList where name not equals to DEFAULT_NAME
        defaultTargetSystemShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the targetSystemList where name not equals to UPDATED_NAME
        defaultTargetSystemShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get all the targetSystemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTargetSystemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the targetSystemList where name equals to UPDATED_NAME
        defaultTargetSystemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get all the targetSystemList where name is not null
        defaultTargetSystemShouldBeFound("name.specified=true");

        // Get all the targetSystemList where name is null
        defaultTargetSystemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetSystemsByNameContainsSomething() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get all the targetSystemList where name contains DEFAULT_NAME
        defaultTargetSystemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the targetSystemList where name contains UPDATED_NAME
        defaultTargetSystemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        // Get all the targetSystemList where name does not contain DEFAULT_NAME
        defaultTargetSystemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the targetSystemList where name does not contain UPDATED_NAME
        defaultTargetSystemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTargetSystemShouldBeFound(String filter) throws Exception {
        restTargetSystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetSystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restTargetSystemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTargetSystemShouldNotBeFound(String filter) throws Exception {
        restTargetSystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTargetSystemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTargetSystem() throws Exception {
        // Get the targetSystem
        restTargetSystemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTargetSystem() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();

        // Update the targetSystem
        TargetSystem updatedTargetSystem = targetSystemRepository.findById(targetSystem.getId()).get();
        // Disconnect from session so that the updates on updatedTargetSystem are not directly saved in db
        em.detach(updatedTargetSystem);
        updatedTargetSystem.name(UPDATED_NAME);

        restTargetSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTargetSystem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTargetSystem))
            )
            .andExpect(status().isOk());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
        TargetSystem testTargetSystem = targetSystemList.get(targetSystemList.size() - 1);
        assertThat(testTargetSystem.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTargetSystem() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();
        targetSystem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, targetSystem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTargetSystem() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();
        targetSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTargetSystem() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();
        targetSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTargetSystemWithPatch() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();

        // Update the targetSystem using partial update
        TargetSystem partialUpdatedTargetSystem = new TargetSystem();
        partialUpdatedTargetSystem.setId(targetSystem.getId());

        partialUpdatedTargetSystem.name(UPDATED_NAME);

        restTargetSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetSystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetSystem))
            )
            .andExpect(status().isOk());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
        TargetSystem testTargetSystem = targetSystemList.get(targetSystemList.size() - 1);
        assertThat(testTargetSystem.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTargetSystemWithPatch() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();

        // Update the targetSystem using partial update
        TargetSystem partialUpdatedTargetSystem = new TargetSystem();
        partialUpdatedTargetSystem.setId(targetSystem.getId());

        partialUpdatedTargetSystem.name(UPDATED_NAME);

        restTargetSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetSystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetSystem))
            )
            .andExpect(status().isOk());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
        TargetSystem testTargetSystem = targetSystemList.get(targetSystemList.size() - 1);
        assertThat(testTargetSystem.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTargetSystem() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();
        targetSystem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, targetSystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTargetSystem() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();
        targetSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTargetSystem() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemRepository.findAll().size();
        targetSystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetSystem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TargetSystem in the database
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTargetSystem() throws Exception {
        // Initialize the database
        targetSystemRepository.saveAndFlush(targetSystem);

        int databaseSizeBeforeDelete = targetSystemRepository.findAll().size();

        // Delete the targetSystem
        restTargetSystemMockMvc
            .perform(delete(ENTITY_API_URL_ID, targetSystem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TargetSystem> targetSystemList = targetSystemRepository.findAll();
        assertThat(targetSystemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
