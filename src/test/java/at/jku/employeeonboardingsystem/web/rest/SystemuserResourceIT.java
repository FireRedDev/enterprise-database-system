package at.jku.employeeonboardingsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.jku.employeeonboardingsystem.IntegrationTest;
import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.domain.User;
import at.jku.employeeonboardingsystem.repository.SystemuserRepository;
import at.jku.employeeonboardingsystem.service.criteria.SystemuserCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link SystemuserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemuserResourceIT {

    private static final LocalDate DEFAULT_ENTRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ENTRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/systemusers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemuserRepository systemuserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemuserMockMvc;

    private Systemuser systemuser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systemuser createEntity(EntityManager em) {
        Systemuser systemuser = new Systemuser().entryDate(DEFAULT_ENTRY_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        systemuser.setUser(user);
        return systemuser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systemuser createUpdatedEntity(EntityManager em) {
        Systemuser systemuser = new Systemuser().entryDate(UPDATED_ENTRY_DATE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        systemuser.setUser(user);
        return systemuser;
    }

    @BeforeEach
    public void initTest() {
        systemuser = createEntity(em);
    }

    @Test
    @Transactional
    void createSystemuser() throws Exception {
        int databaseSizeBeforeCreate = systemuserRepository.findAll().size();
        // Create the Systemuser
        restSystemuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isCreated());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeCreate + 1);
        Systemuser testSystemuser = systemuserList.get(systemuserList.size() - 1);
        assertThat(testSystemuser.getEntryDate()).isEqualTo(DEFAULT_ENTRY_DATE);
    }

    @Test
    @Transactional
    void createSystemuserWithExistingId() throws Exception {
        // Create the Systemuser with an existing ID
        systemuser.setId(1L);

        int databaseSizeBeforeCreate = systemuserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemuserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSystemusers() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList
        restSystemuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())));
    }

    @Test
    @Transactional
    void getSystemuser() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get the systemuser
        restSystemuserMockMvc
            .perform(get(ENTITY_API_URL_ID, systemuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemuser.getId().intValue()))
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()));
    }

    @Test
    @Transactional
    void getSystemusersByIdFiltering() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        Long id = systemuser.getId();

        defaultSystemuserShouldBeFound("id.equals=" + id);
        defaultSystemuserShouldNotBeFound("id.notEquals=" + id);

        defaultSystemuserShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSystemuserShouldNotBeFound("id.greaterThan=" + id);

        defaultSystemuserShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSystemuserShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate equals to DEFAULT_ENTRY_DATE
        defaultSystemuserShouldBeFound("entryDate.equals=" + DEFAULT_ENTRY_DATE);

        // Get all the systemuserList where entryDate equals to UPDATED_ENTRY_DATE
        defaultSystemuserShouldNotBeFound("entryDate.equals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate not equals to DEFAULT_ENTRY_DATE
        defaultSystemuserShouldNotBeFound("entryDate.notEquals=" + DEFAULT_ENTRY_DATE);

        // Get all the systemuserList where entryDate not equals to UPDATED_ENTRY_DATE
        defaultSystemuserShouldBeFound("entryDate.notEquals=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsInShouldWork() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate in DEFAULT_ENTRY_DATE or UPDATED_ENTRY_DATE
        defaultSystemuserShouldBeFound("entryDate.in=" + DEFAULT_ENTRY_DATE + "," + UPDATED_ENTRY_DATE);

        // Get all the systemuserList where entryDate equals to UPDATED_ENTRY_DATE
        defaultSystemuserShouldNotBeFound("entryDate.in=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate is not null
        defaultSystemuserShouldBeFound("entryDate.specified=true");

        // Get all the systemuserList where entryDate is null
        defaultSystemuserShouldNotBeFound("entryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate is greater than or equal to DEFAULT_ENTRY_DATE
        defaultSystemuserShouldBeFound("entryDate.greaterThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the systemuserList where entryDate is greater than or equal to UPDATED_ENTRY_DATE
        defaultSystemuserShouldNotBeFound("entryDate.greaterThanOrEqual=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate is less than or equal to DEFAULT_ENTRY_DATE
        defaultSystemuserShouldBeFound("entryDate.lessThanOrEqual=" + DEFAULT_ENTRY_DATE);

        // Get all the systemuserList where entryDate is less than or equal to SMALLER_ENTRY_DATE
        defaultSystemuserShouldNotBeFound("entryDate.lessThanOrEqual=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsLessThanSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate is less than DEFAULT_ENTRY_DATE
        defaultSystemuserShouldNotBeFound("entryDate.lessThan=" + DEFAULT_ENTRY_DATE);

        // Get all the systemuserList where entryDate is less than UPDATED_ENTRY_DATE
        defaultSystemuserShouldBeFound("entryDate.lessThan=" + UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllSystemusersByEntryDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where entryDate is greater than DEFAULT_ENTRY_DATE
        defaultSystemuserShouldNotBeFound("entryDate.greaterThan=" + DEFAULT_ENTRY_DATE);

        // Get all the systemuserList where entryDate is greater than SMALLER_ENTRY_DATE
        defaultSystemuserShouldBeFound("entryDate.greaterThan=" + SMALLER_ENTRY_DATE);
    }

    @Test
    @Transactional
    void getAllSystemusersByUserIsEqualToSomething() throws Exception {
        // Get already existing entity
        User user = systemuser.getUser();
        systemuserRepository.saveAndFlush(systemuser);
        Long userId = user.getId();

        // Get all the systemuserList where user equals to userId
        defaultSystemuserShouldBeFound("userId.equals=" + userId);

        // Get all the systemuserList where user equals to (userId + 1)
        defaultSystemuserShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSystemuserShouldBeFound(String filter) throws Exception {
        restSystemuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())));

        // Check, that the count call also returns 1
        restSystemuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSystemuserShouldNotBeFound(String filter) throws Exception {
        restSystemuserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSystemuserMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSystemuser() throws Exception {
        // Get the systemuser
        restSystemuserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSystemuser() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();

        // Update the systemuser
        Systemuser updatedSystemuser = systemuserRepository.findById(systemuser.getId()).get();
        // Disconnect from session so that the updates on updatedSystemuser are not directly saved in db
        em.detach(updatedSystemuser);
        updatedSystemuser.entryDate(UPDATED_ENTRY_DATE);

        restSystemuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSystemuser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSystemuser))
            )
            .andExpect(status().isOk());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
        Systemuser testSystemuser = systemuserList.get(systemuserList.size() - 1);
        assertThat(testSystemuser.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void putNonExistingSystemuser() throws Exception {
        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();
        systemuser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemuser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemuser() throws Exception {
        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();
        systemuser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemuserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemuser() throws Exception {
        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();
        systemuser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemuserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemuserWithPatch() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();

        // Update the systemuser using partial update
        Systemuser partialUpdatedSystemuser = new Systemuser();
        partialUpdatedSystemuser.setId(systemuser.getId());

        partialUpdatedSystemuser.entryDate(UPDATED_ENTRY_DATE);

        restSystemuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemuser))
            )
            .andExpect(status().isOk());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
        Systemuser testSystemuser = systemuserList.get(systemuserList.size() - 1);
        assertThat(testSystemuser.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void fullUpdateSystemuserWithPatch() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();

        // Update the systemuser using partial update
        Systemuser partialUpdatedSystemuser = new Systemuser();
        partialUpdatedSystemuser.setId(systemuser.getId());

        partialUpdatedSystemuser.entryDate(UPDATED_ENTRY_DATE);

        restSystemuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSystemuser))
            )
            .andExpect(status().isOk());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
        Systemuser testSystemuser = systemuserList.get(systemuserList.size() - 1);
        assertThat(testSystemuser.getEntryDate()).isEqualTo(UPDATED_ENTRY_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingSystemuser() throws Exception {
        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();
        systemuser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemuser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemuser() throws Exception {
        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();
        systemuser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemuserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isBadRequest());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemuser() throws Exception {
        int databaseSizeBeforeUpdate = systemuserRepository.findAll().size();
        systemuser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemuserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(systemuser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Systemuser in the database
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemuser() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        int databaseSizeBeforeDelete = systemuserRepository.findAll().size();

        // Delete the systemuser
        restSystemuserMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemuser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Systemuser> systemuserList = systemuserRepository.findAll();
        assertThat(systemuserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
