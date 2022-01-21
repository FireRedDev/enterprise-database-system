package at.jku.employeeonboardingsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.jku.employeeonboardingsystem.IntegrationTest;
import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.repository.TargetsystemRepository;
import at.jku.employeeonboardingsystem.service.criteria.TargetsystemCriteria;
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
 * Integration tests for the {@link TargetsystemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TargetsystemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/targetsystems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TargetsystemRepository targetsystemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTargetsystemMockMvc;

    private Targetsystem targetsystem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Targetsystem createEntity(EntityManager em) {
        Targetsystem targetsystem = new Targetsystem()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .url(DEFAULT_URL)
            .password(DEFAULT_PASSWORD)
            .username(DEFAULT_USERNAME);
        return targetsystem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Targetsystem createUpdatedEntity(EntityManager em) {
        Targetsystem targetsystem = new Targetsystem()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .password(UPDATED_PASSWORD)
            .username(UPDATED_USERNAME);
        return targetsystem;
    }

    @BeforeEach
    public void initTest() {
        targetsystem = createEntity(em);
    }

    @Test
    @Transactional
    void createTargetsystem() throws Exception {
        int databaseSizeBeforeCreate = targetsystemRepository.findAll().size();
        // Create the Targetsystem
        restTargetsystemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isCreated());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeCreate + 1);
        Targetsystem testTargetsystem = targetsystemList.get(targetsystemList.size() - 1);
        assertThat(testTargetsystem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTargetsystem.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTargetsystem.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testTargetsystem.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testTargetsystem.getUsername()).isEqualTo(DEFAULT_USERNAME);
    }

    @Test
    @Transactional
    void createTargetsystemWithExistingId() throws Exception {
        // Create the Targetsystem with an existing ID
        targetsystem.setId(1L);

        int databaseSizeBeforeCreate = targetsystemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetsystemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTargetsystems() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList
        restTargetsystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetsystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));
    }

    @Test
    @Transactional
    void getTargetsystem() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get the targetsystem
        restTargetsystemMockMvc
            .perform(get(ENTITY_API_URL_ID, targetsystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(targetsystem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME));
    }

    @Test
    @Transactional
    void getTargetsystemsByIdFiltering() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        Long id = targetsystem.getId();

        defaultTargetsystemShouldBeFound("id.equals=" + id);
        defaultTargetsystemShouldNotBeFound("id.notEquals=" + id);

        defaultTargetsystemShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTargetsystemShouldNotBeFound("id.greaterThan=" + id);

        defaultTargetsystemShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTargetsystemShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where name equals to DEFAULT_NAME
        defaultTargetsystemShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the targetsystemList where name equals to UPDATED_NAME
        defaultTargetsystemShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where name not equals to DEFAULT_NAME
        defaultTargetsystemShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the targetsystemList where name not equals to UPDATED_NAME
        defaultTargetsystemShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTargetsystemShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the targetsystemList where name equals to UPDATED_NAME
        defaultTargetsystemShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where name is not null
        defaultTargetsystemShouldBeFound("name.specified=true");

        // Get all the targetsystemList where name is null
        defaultTargetsystemShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetsystemsByNameContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where name contains DEFAULT_NAME
        defaultTargetsystemShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the targetsystemList where name contains UPDATED_NAME
        defaultTargetsystemShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where name does not contain DEFAULT_NAME
        defaultTargetsystemShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the targetsystemList where name does not contain UPDATED_NAME
        defaultTargetsystemShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where type equals to DEFAULT_TYPE
        defaultTargetsystemShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the targetsystemList where type equals to UPDATED_TYPE
        defaultTargetsystemShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where type not equals to DEFAULT_TYPE
        defaultTargetsystemShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the targetsystemList where type not equals to UPDATED_TYPE
        defaultTargetsystemShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTargetsystemShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the targetsystemList where type equals to UPDATED_TYPE
        defaultTargetsystemShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where type is not null
        defaultTargetsystemShouldBeFound("type.specified=true");

        // Get all the targetsystemList where type is null
        defaultTargetsystemShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetsystemsByTypeContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where type contains DEFAULT_TYPE
        defaultTargetsystemShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the targetsystemList where type contains UPDATED_TYPE
        defaultTargetsystemShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where type does not contain DEFAULT_TYPE
        defaultTargetsystemShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the targetsystemList where type does not contain UPDATED_TYPE
        defaultTargetsystemShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where url equals to DEFAULT_URL
        defaultTargetsystemShouldBeFound("url.equals=" + DEFAULT_URL);

        // Get all the targetsystemList where url equals to UPDATED_URL
        defaultTargetsystemShouldNotBeFound("url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUrlIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where url not equals to DEFAULT_URL
        defaultTargetsystemShouldNotBeFound("url.notEquals=" + DEFAULT_URL);

        // Get all the targetsystemList where url not equals to UPDATED_URL
        defaultTargetsystemShouldBeFound("url.notEquals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where url in DEFAULT_URL or UPDATED_URL
        defaultTargetsystemShouldBeFound("url.in=" + DEFAULT_URL + "," + UPDATED_URL);

        // Get all the targetsystemList where url equals to UPDATED_URL
        defaultTargetsystemShouldNotBeFound("url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where url is not null
        defaultTargetsystemShouldBeFound("url.specified=true");

        // Get all the targetsystemList where url is null
        defaultTargetsystemShouldNotBeFound("url.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUrlContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where url contains DEFAULT_URL
        defaultTargetsystemShouldBeFound("url.contains=" + DEFAULT_URL);

        // Get all the targetsystemList where url contains UPDATED_URL
        defaultTargetsystemShouldNotBeFound("url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where url does not contain DEFAULT_URL
        defaultTargetsystemShouldNotBeFound("url.doesNotContain=" + DEFAULT_URL);

        // Get all the targetsystemList where url does not contain UPDATED_URL
        defaultTargetsystemShouldBeFound("url.doesNotContain=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where password equals to DEFAULT_PASSWORD
        defaultTargetsystemShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the targetsystemList where password equals to UPDATED_PASSWORD
        defaultTargetsystemShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where password not equals to DEFAULT_PASSWORD
        defaultTargetsystemShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the targetsystemList where password not equals to UPDATED_PASSWORD
        defaultTargetsystemShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultTargetsystemShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the targetsystemList where password equals to UPDATED_PASSWORD
        defaultTargetsystemShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where password is not null
        defaultTargetsystemShouldBeFound("password.specified=true");

        // Get all the targetsystemList where password is null
        defaultTargetsystemShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetsystemsByPasswordContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where password contains DEFAULT_PASSWORD
        defaultTargetsystemShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the targetsystemList where password contains UPDATED_PASSWORD
        defaultTargetsystemShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where password does not contain DEFAULT_PASSWORD
        defaultTargetsystemShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the targetsystemList where password does not contain UPDATED_PASSWORD
        defaultTargetsystemShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where username equals to DEFAULT_USERNAME
        defaultTargetsystemShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the targetsystemList where username equals to UPDATED_USERNAME
        defaultTargetsystemShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where username not equals to DEFAULT_USERNAME
        defaultTargetsystemShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the targetsystemList where username not equals to UPDATED_USERNAME
        defaultTargetsystemShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultTargetsystemShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the targetsystemList where username equals to UPDATED_USERNAME
        defaultTargetsystemShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where username is not null
        defaultTargetsystemShouldBeFound("username.specified=true");

        // Get all the targetsystemList where username is null
        defaultTargetsystemShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where username contains DEFAULT_USERNAME
        defaultTargetsystemShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the targetsystemList where username contains UPDATED_USERNAME
        defaultTargetsystemShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetsystemsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        // Get all the targetsystemList where username does not contain DEFAULT_USERNAME
        defaultTargetsystemShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the targetsystemList where username does not contain UPDATED_USERNAME
        defaultTargetsystemShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTargetsystemShouldBeFound(String filter) throws Exception {
        restTargetsystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetsystem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)));

        // Check, that the count call also returns 1
        restTargetsystemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTargetsystemShouldNotBeFound(String filter) throws Exception {
        restTargetsystemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTargetsystemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTargetsystem() throws Exception {
        // Get the targetsystem
        restTargetsystemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTargetsystem() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();

        // Update the targetsystem
        Targetsystem updatedTargetsystem = targetsystemRepository.findById(targetsystem.getId()).get();
        // Disconnect from session so that the updates on updatedTargetsystem are not directly saved in db
        em.detach(updatedTargetsystem);
        updatedTargetsystem.name(UPDATED_NAME).type(UPDATED_TYPE).url(UPDATED_URL).password(UPDATED_PASSWORD).username(UPDATED_USERNAME);

        restTargetsystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTargetsystem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTargetsystem))
            )
            .andExpect(status().isOk());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
        Targetsystem testTargetsystem = targetsystemList.get(targetsystemList.size() - 1);
        assertThat(testTargetsystem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTargetsystem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTargetsystem.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTargetsystem.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testTargetsystem.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void putNonExistingTargetsystem() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();
        targetsystem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetsystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, targetsystem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTargetsystem() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();
        targetsystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTargetsystem() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();
        targetsystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTargetsystemWithPatch() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();

        // Update the targetsystem using partial update
        Targetsystem partialUpdatedTargetsystem = new Targetsystem();
        partialUpdatedTargetsystem.setId(targetsystem.getId());

        partialUpdatedTargetsystem.name(UPDATED_NAME).type(UPDATED_TYPE).url(UPDATED_URL).username(UPDATED_USERNAME);

        restTargetsystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetsystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetsystem))
            )
            .andExpect(status().isOk());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
        Targetsystem testTargetsystem = targetsystemList.get(targetsystemList.size() - 1);
        assertThat(testTargetsystem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTargetsystem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTargetsystem.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTargetsystem.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testTargetsystem.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void fullUpdateTargetsystemWithPatch() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();

        // Update the targetsystem using partial update
        Targetsystem partialUpdatedTargetsystem = new Targetsystem();
        partialUpdatedTargetsystem.setId(targetsystem.getId());

        partialUpdatedTargetsystem
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .url(UPDATED_URL)
            .password(UPDATED_PASSWORD)
            .username(UPDATED_USERNAME);

        restTargetsystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetsystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetsystem))
            )
            .andExpect(status().isOk());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
        Targetsystem testTargetsystem = targetsystemList.get(targetsystemList.size() - 1);
        assertThat(testTargetsystem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTargetsystem.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTargetsystem.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testTargetsystem.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testTargetsystem.getUsername()).isEqualTo(UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void patchNonExistingTargetsystem() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();
        targetsystem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetsystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, targetsystem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTargetsystem() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();
        targetsystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isBadRequest());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTargetsystem() throws Exception {
        int databaseSizeBeforeUpdate = targetsystemRepository.findAll().size();
        targetsystem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetsystemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetsystem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Targetsystem in the database
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTargetsystem() throws Exception {
        // Initialize the database
        targetsystemRepository.saveAndFlush(targetsystem);

        int databaseSizeBeforeDelete = targetsystemRepository.findAll().size();

        // Delete the targetsystem
        restTargetsystemMockMvc
            .perform(delete(ENTITY_API_URL_ID, targetsystem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Targetsystem> targetsystemList = targetsystemRepository.findAll();
        assertThat(targetsystemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
