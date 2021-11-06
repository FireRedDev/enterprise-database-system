package at.jku.employeeonboardingsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.jku.employeeonboardingsystem.IntegrationTest;
import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import at.jku.employeeonboardingsystem.repository.TargetsystemcredentialsRepository;
import at.jku.employeeonboardingsystem.service.criteria.TargetsystemcredentialsCriteria;
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
 * Integration tests for the {@link TargetsystemcredentialsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TargetsystemcredentialsResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/targetsystemcredentials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TargetsystemcredentialsRepository targetsystemcredentialsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTargetsystemcredentialsMockMvc;

    private Targetsystemcredentials targetsystemcredentials;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Targetsystemcredentials createEntity(EntityManager em) {
        Targetsystemcredentials targetsystemcredentials = new Targetsystemcredentials()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD);
        return targetsystemcredentials;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Targetsystemcredentials createUpdatedEntity(EntityManager em) {
        Targetsystemcredentials targetsystemcredentials = new Targetsystemcredentials()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);
        return targetsystemcredentials;
    }

    @BeforeEach
    public void initTest() {
        targetsystemcredentials = createEntity(em);
    }

    @Test
    @Transactional
    void createTargetsystemcredentials() throws Exception {
        int databaseSizeBeforeCreate = targetsystemcredentialsRepository.findAll().size();
        // Create the Targetsystemcredentials
        restTargetsystemcredentialsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isCreated());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeCreate + 1);
        Targetsystemcredentials testTargetsystemcredentials = targetsystemcredentialsList.get(targetsystemcredentialsList.size() - 1);
        assertThat(testTargetsystemcredentials.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTargetsystemcredentials.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createTargetsystemcredentialsWithExistingId() throws Exception {
        // Create the Targetsystemcredentials with an existing ID
        targetsystemcredentials.setId(1L);

        int databaseSizeBeforeCreate = targetsystemcredentialsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetsystemcredentialsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetsystemcredentialsRepository.findAll().size();
        // set the field null
        targetsystemcredentials.setPassword(null);

        // Create the Targetsystemcredentials, which fails.

        restTargetsystemcredentialsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isBadRequest());

        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentials() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList
        restTargetsystemcredentialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetsystemcredentials.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getTargetsystemcredentials() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get the targetsystemcredentials
        restTargetsystemcredentialsMockMvc
            .perform(get(ENTITY_API_URL_ID, targetsystemcredentials.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(targetsystemcredentials.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getTargetsystemcredentialsByIdFiltering() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        Long id = targetsystemcredentials.getId();

        defaultTargetsystemcredentialsShouldBeFound("id.equals=" + id);
        defaultTargetsystemcredentialsShouldNotBeFound("id.notEquals=" + id);

        defaultTargetsystemcredentialsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTargetsystemcredentialsShouldNotBeFound("id.greaterThan=" + id);

        defaultTargetsystemcredentialsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTargetsystemcredentialsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where username equals to DEFAULT_USERNAME
        defaultTargetsystemcredentialsShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the targetsystemcredentialsList where username equals to UPDATED_USERNAME
        defaultTargetsystemcredentialsShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where username not equals to DEFAULT_USERNAME
        defaultTargetsystemcredentialsShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the targetsystemcredentialsList where username not equals to UPDATED_USERNAME
        defaultTargetsystemcredentialsShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultTargetsystemcredentialsShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the targetsystemcredentialsList where username equals to UPDATED_USERNAME
        defaultTargetsystemcredentialsShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where username is not null
        defaultTargetsystemcredentialsShouldBeFound("username.specified=true");

        // Get all the targetsystemcredentialsList where username is null
        defaultTargetsystemcredentialsShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where username contains DEFAULT_USERNAME
        defaultTargetsystemcredentialsShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the targetsystemcredentialsList where username contains UPDATED_USERNAME
        defaultTargetsystemcredentialsShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where username does not contain DEFAULT_USERNAME
        defaultTargetsystemcredentialsShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the targetsystemcredentialsList where username does not contain UPDATED_USERNAME
        defaultTargetsystemcredentialsShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where password equals to DEFAULT_PASSWORD
        defaultTargetsystemcredentialsShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the targetsystemcredentialsList where password equals to UPDATED_PASSWORD
        defaultTargetsystemcredentialsShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where password not equals to DEFAULT_PASSWORD
        defaultTargetsystemcredentialsShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the targetsystemcredentialsList where password not equals to UPDATED_PASSWORD
        defaultTargetsystemcredentialsShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultTargetsystemcredentialsShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the targetsystemcredentialsList where password equals to UPDATED_PASSWORD
        defaultTargetsystemcredentialsShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where password is not null
        defaultTargetsystemcredentialsShouldBeFound("password.specified=true");

        // Get all the targetsystemcredentialsList where password is null
        defaultTargetsystemcredentialsShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByPasswordContainsSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where password contains DEFAULT_PASSWORD
        defaultTargetsystemcredentialsShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the targetsystemcredentialsList where password contains UPDATED_PASSWORD
        defaultTargetsystemcredentialsShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        // Get all the targetsystemcredentialsList where password does not contain DEFAULT_PASSWORD
        defaultTargetsystemcredentialsShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the targetsystemcredentialsList where password does not contain UPDATED_PASSWORD
        defaultTargetsystemcredentialsShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsBySystemuserIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);
        Systemuser systemuser;
        if (TestUtil.findAll(em, Systemuser.class).isEmpty()) {
            systemuser = SystemuserResourceIT.createEntity(em);
            em.persist(systemuser);
            em.flush();
        } else {
            systemuser = TestUtil.findAll(em, Systemuser.class).get(0);
        }
        em.persist(systemuser);
        em.flush();
        targetsystemcredentials.setSystemuser(systemuser);
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);
        Long systemuserId = systemuser.getId();

        // Get all the targetsystemcredentialsList where systemuser equals to systemuserId
        defaultTargetsystemcredentialsShouldBeFound("systemuserId.equals=" + systemuserId);

        // Get all the targetsystemcredentialsList where systemuser equals to (systemuserId + 1)
        defaultTargetsystemcredentialsShouldNotBeFound("systemuserId.equals=" + (systemuserId + 1));
    }

    @Test
    @Transactional
    void getAllTargetsystemcredentialsByTargetsystemIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);
        Targetsystem targetsystem;
        if (TestUtil.findAll(em, Targetsystem.class).isEmpty()) {
            targetsystem = TargetsystemResourceIT.createEntity(em);
            em.persist(targetsystem);
            em.flush();
        } else {
            targetsystem = TestUtil.findAll(em, Targetsystem.class).get(0);
        }
        em.persist(targetsystem);
        em.flush();
        targetsystemcredentials.setTargetsystem(targetsystem);
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);
        Long targetsystemId = targetsystem.getId();

        // Get all the targetsystemcredentialsList where targetsystem equals to targetsystemId
        defaultTargetsystemcredentialsShouldBeFound("targetsystemId.equals=" + targetsystemId);

        // Get all the targetsystemcredentialsList where targetsystem equals to (targetsystemId + 1)
        defaultTargetsystemcredentialsShouldNotBeFound("targetsystemId.equals=" + (targetsystemId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTargetsystemcredentialsShouldBeFound(String filter) throws Exception {
        restTargetsystemcredentialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetsystemcredentials.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restTargetsystemcredentialsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTargetsystemcredentialsShouldNotBeFound(String filter) throws Exception {
        restTargetsystemcredentialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTargetsystemcredentialsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTargetsystemcredentials() throws Exception {
        // Get the targetsystemcredentials
        restTargetsystemcredentialsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTargetsystemcredentials() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();

        // Update the targetsystemcredentials
        Targetsystemcredentials updatedTargetsystemcredentials = targetsystemcredentialsRepository
            .findById(targetsystemcredentials.getId())
            .get();
        // Disconnect from session so that the updates on updatedTargetsystemcredentials are not directly saved in db
        em.detach(updatedTargetsystemcredentials);
        updatedTargetsystemcredentials.username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restTargetsystemcredentialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTargetsystemcredentials.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTargetsystemcredentials))
            )
            .andExpect(status().isOk());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
        Targetsystemcredentials testTargetsystemcredentials = targetsystemcredentialsList.get(targetsystemcredentialsList.size() - 1);
        assertThat(testTargetsystemcredentials.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTargetsystemcredentials.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingTargetsystemcredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();
        targetsystemcredentials.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetsystemcredentialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, targetsystemcredentials.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTargetsystemcredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();
        targetsystemcredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemcredentialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTargetsystemcredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();
        targetsystemcredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemcredentialsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTargetsystemcredentialsWithPatch() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();

        // Update the targetsystemcredentials using partial update
        Targetsystemcredentials partialUpdatedTargetsystemcredentials = new Targetsystemcredentials();
        partialUpdatedTargetsystemcredentials.setId(targetsystemcredentials.getId());

        partialUpdatedTargetsystemcredentials.username(UPDATED_USERNAME);

        restTargetsystemcredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetsystemcredentials.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetsystemcredentials))
            )
            .andExpect(status().isOk());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
        Targetsystemcredentials testTargetsystemcredentials = targetsystemcredentialsList.get(targetsystemcredentialsList.size() - 1);
        assertThat(testTargetsystemcredentials.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTargetsystemcredentials.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateTargetsystemcredentialsWithPatch() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();

        // Update the targetsystemcredentials using partial update
        Targetsystemcredentials partialUpdatedTargetsystemcredentials = new Targetsystemcredentials();
        partialUpdatedTargetsystemcredentials.setId(targetsystemcredentials.getId());

        partialUpdatedTargetsystemcredentials.username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restTargetsystemcredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetsystemcredentials.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetsystemcredentials))
            )
            .andExpect(status().isOk());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
        Targetsystemcredentials testTargetsystemcredentials = targetsystemcredentialsList.get(targetsystemcredentialsList.size() - 1);
        assertThat(testTargetsystemcredentials.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTargetsystemcredentials.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingTargetsystemcredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();
        targetsystemcredentials.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetsystemcredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, targetsystemcredentials.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTargetsystemcredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();
        targetsystemcredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemcredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTargetsystemcredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemcredentialsRepository.findAll().size();
        targetsystemcredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemcredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetsystemcredentials))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Targetsystemcredentials in the database
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTargetsystemcredentials() throws Exception {
        // Initialize the database
        targetsystemcredentialsRepository.saveAndFlush(targetsystemcredentials);

        int databaseSizeBeforeDelete = targetsystemcredentialsRepository.findAll().size();

        // Delete the targetsystemcredentials
        restTargetsystemcredentialsMockMvc
            .perform(delete(ENTITY_API_URL_ID, targetsystemcredentials.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Targetsystemcredentials> targetsystemcredentialsList = targetsystemcredentialsRepository.findAll();
        assertThat(targetsystemcredentialsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
