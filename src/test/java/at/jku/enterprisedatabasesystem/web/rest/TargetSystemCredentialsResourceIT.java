package at.jku.enterprisedatabasesystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.jku.enterprisedatabasesystem.IntegrationTest;
import at.jku.enterprisedatabasesystem.domain.Department;
import at.jku.enterprisedatabasesystem.domain.Employee;
import at.jku.enterprisedatabasesystem.domain.TargetSystem;
import at.jku.enterprisedatabasesystem.domain.TargetSystemCredentials;
import at.jku.enterprisedatabasesystem.repository.TargetSystemCredentialsRepository;
import at.jku.enterprisedatabasesystem.service.criteria.TargetSystemCredentialsCriteria;
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
 * Integration tests for the {@link TargetSystemCredentialsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TargetSystemCredentialsResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/target-system-credentials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TargetSystemCredentialsRepository targetSystemCredentialsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTargetSystemCredentialsMockMvc;

    private TargetSystemCredentials targetSystemCredentials;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TargetSystemCredentials createEntity(EntityManager em) {
        TargetSystemCredentials targetSystemCredentials = new TargetSystemCredentials()
            .username(DEFAULT_USERNAME)
            .password(DEFAULT_PASSWORD);
        return targetSystemCredentials;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TargetSystemCredentials createUpdatedEntity(EntityManager em) {
        TargetSystemCredentials targetSystemCredentials = new TargetSystemCredentials()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD);
        return targetSystemCredentials;
    }

    @BeforeEach
    public void initTest() {
        targetSystemCredentials = createEntity(em);
    }

    @Test
    @Transactional
    void createTargetSystemCredentials() throws Exception {
        int databaseSizeBeforeCreate = targetSystemCredentialsRepository.findAll().size();
        // Create the TargetSystemCredentials
        restTargetSystemCredentialsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isCreated());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeCreate + 1);
        TargetSystemCredentials testTargetSystemCredentials = targetSystemCredentialsList.get(targetSystemCredentialsList.size() - 1);
        assertThat(testTargetSystemCredentials.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTargetSystemCredentials.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void createTargetSystemCredentialsWithExistingId() throws Exception {
        // Create the TargetSystemCredentials with an existing ID
        targetSystemCredentials.setId(1L);

        int databaseSizeBeforeCreate = targetSystemCredentialsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetSystemCredentialsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetSystemCredentialsRepository.findAll().size();
        // set the field null
        targetSystemCredentials.setUsername(null);

        // Create the TargetSystemCredentials, which fails.

        restTargetSystemCredentialsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isBadRequest());

        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetSystemCredentialsRepository.findAll().size();
        // set the field null
        targetSystemCredentials.setPassword(null);

        // Create the TargetSystemCredentials, which fails.

        restTargetSystemCredentialsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isBadRequest());

        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentials() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList
        restTargetSystemCredentialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetSystemCredentials.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    void getTargetSystemCredentials() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get the targetSystemCredentials
        restTargetSystemCredentialsMockMvc
            .perform(get(ENTITY_API_URL_ID, targetSystemCredentials.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(targetSystemCredentials.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }

    @Test
    @Transactional
    void getTargetSystemCredentialsByIdFiltering() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        Long id = targetSystemCredentials.getId();

        defaultTargetSystemCredentialsShouldBeFound("id.equals=" + id);
        defaultTargetSystemCredentialsShouldNotBeFound("id.notEquals=" + id);

        defaultTargetSystemCredentialsShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTargetSystemCredentialsShouldNotBeFound("id.greaterThan=" + id);

        defaultTargetSystemCredentialsShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTargetSystemCredentialsShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByUsernameIsEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where username equals to DEFAULT_USERNAME
        defaultTargetSystemCredentialsShouldBeFound("username.equals=" + DEFAULT_USERNAME);

        // Get all the targetSystemCredentialsList where username equals to UPDATED_USERNAME
        defaultTargetSystemCredentialsShouldNotBeFound("username.equals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByUsernameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where username not equals to DEFAULT_USERNAME
        defaultTargetSystemCredentialsShouldNotBeFound("username.notEquals=" + DEFAULT_USERNAME);

        // Get all the targetSystemCredentialsList where username not equals to UPDATED_USERNAME
        defaultTargetSystemCredentialsShouldBeFound("username.notEquals=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByUsernameIsInShouldWork() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where username in DEFAULT_USERNAME or UPDATED_USERNAME
        defaultTargetSystemCredentialsShouldBeFound("username.in=" + DEFAULT_USERNAME + "," + UPDATED_USERNAME);

        // Get all the targetSystemCredentialsList where username equals to UPDATED_USERNAME
        defaultTargetSystemCredentialsShouldNotBeFound("username.in=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByUsernameIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where username is not null
        defaultTargetSystemCredentialsShouldBeFound("username.specified=true");

        // Get all the targetSystemCredentialsList where username is null
        defaultTargetSystemCredentialsShouldNotBeFound("username.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByUsernameContainsSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where username contains DEFAULT_USERNAME
        defaultTargetSystemCredentialsShouldBeFound("username.contains=" + DEFAULT_USERNAME);

        // Get all the targetSystemCredentialsList where username contains UPDATED_USERNAME
        defaultTargetSystemCredentialsShouldNotBeFound("username.contains=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByUsernameNotContainsSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where username does not contain DEFAULT_USERNAME
        defaultTargetSystemCredentialsShouldNotBeFound("username.doesNotContain=" + DEFAULT_USERNAME);

        // Get all the targetSystemCredentialsList where username does not contain UPDATED_USERNAME
        defaultTargetSystemCredentialsShouldBeFound("username.doesNotContain=" + UPDATED_USERNAME);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where password equals to DEFAULT_PASSWORD
        defaultTargetSystemCredentialsShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the targetSystemCredentialsList where password equals to UPDATED_PASSWORD
        defaultTargetSystemCredentialsShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByPasswordIsNotEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where password not equals to DEFAULT_PASSWORD
        defaultTargetSystemCredentialsShouldNotBeFound("password.notEquals=" + DEFAULT_PASSWORD);

        // Get all the targetSystemCredentialsList where password not equals to UPDATED_PASSWORD
        defaultTargetSystemCredentialsShouldBeFound("password.notEquals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultTargetSystemCredentialsShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the targetSystemCredentialsList where password equals to UPDATED_PASSWORD
        defaultTargetSystemCredentialsShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where password is not null
        defaultTargetSystemCredentialsShouldBeFound("password.specified=true");

        // Get all the targetSystemCredentialsList where password is null
        defaultTargetSystemCredentialsShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByPasswordContainsSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where password contains DEFAULT_PASSWORD
        defaultTargetSystemCredentialsShouldBeFound("password.contains=" + DEFAULT_PASSWORD);

        // Get all the targetSystemCredentialsList where password contains UPDATED_PASSWORD
        defaultTargetSystemCredentialsShouldNotBeFound("password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        // Get all the targetSystemCredentialsList where password does not contain DEFAULT_PASSWORD
        defaultTargetSystemCredentialsShouldNotBeFound("password.doesNotContain=" + DEFAULT_PASSWORD);

        // Get all the targetSystemCredentialsList where password does not contain UPDATED_PASSWORD
        defaultTargetSystemCredentialsShouldBeFound("password.doesNotContain=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByTargetSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);
        TargetSystem targetSystem;
        if (TestUtil.findAll(em, TargetSystem.class).isEmpty()) {
            targetSystem = TargetSystemResourceIT.createEntity(em);
            em.persist(targetSystem);
            em.flush();
        } else {
            targetSystem = TestUtil.findAll(em, TargetSystem.class).get(0);
        }
        em.persist(targetSystem);
        em.flush();
        targetSystemCredentials.setTargetSystem(targetSystem);
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);
        Long targetSystemId = targetSystem.getId();

        // Get all the targetSystemCredentialsList where targetSystem equals to targetSystemId
        defaultTargetSystemCredentialsShouldBeFound("targetSystemId.equals=" + targetSystemId);

        // Get all the targetSystemCredentialsList where targetSystem equals to (targetSystemId + 1)
        defaultTargetSystemCredentialsShouldNotBeFound("targetSystemId.equals=" + (targetSystemId + 1));
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByEmployeeIsEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);
        Employee employee;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            employee = EmployeeResourceIT.createEntity(em);
            em.persist(employee);
            em.flush();
        } else {
            employee = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(employee);
        em.flush();
        targetSystemCredentials.setEmployee(employee);
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);
        Long employeeId = employee.getId();

        // Get all the targetSystemCredentialsList where employee equals to employeeId
        defaultTargetSystemCredentialsShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the targetSystemCredentialsList where employee equals to (employeeId + 1)
        defaultTargetSystemCredentialsShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllTargetSystemCredentialsByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);
        Department department;
        if (TestUtil.findAll(em, Department.class).isEmpty()) {
            department = DepartmentResourceIT.createEntity(em);
            em.persist(department);
            em.flush();
        } else {
            department = TestUtil.findAll(em, Department.class).get(0);
        }
        em.persist(department);
        em.flush();
        targetSystemCredentials.setDepartment(department);
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);
        Long departmentId = department.getId();

        // Get all the targetSystemCredentialsList where department equals to departmentId
        defaultTargetSystemCredentialsShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the targetSystemCredentialsList where department equals to (departmentId + 1)
        defaultTargetSystemCredentialsShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTargetSystemCredentialsShouldBeFound(String filter) throws Exception {
        restTargetSystemCredentialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(targetSystemCredentials.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restTargetSystemCredentialsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTargetSystemCredentialsShouldNotBeFound(String filter) throws Exception {
        restTargetSystemCredentialsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTargetSystemCredentialsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTargetSystemCredentials() throws Exception {
        // Get the targetSystemCredentials
        restTargetSystemCredentialsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTargetSystemCredentials() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();

        // Update the targetSystemCredentials
        TargetSystemCredentials updatedTargetSystemCredentials = targetSystemCredentialsRepository
            .findById(targetSystemCredentials.getId())
            .get();
        // Disconnect from session so that the updates on updatedTargetSystemCredentials are not directly saved in db
        em.detach(updatedTargetSystemCredentials);
        updatedTargetSystemCredentials.username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restTargetSystemCredentialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTargetSystemCredentials.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTargetSystemCredentials))
            )
            .andExpect(status().isOk());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
        TargetSystemCredentials testTargetSystemCredentials = targetSystemCredentialsList.get(targetSystemCredentialsList.size() - 1);
        assertThat(testTargetSystemCredentials.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTargetSystemCredentials.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void putNonExistingTargetSystemCredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();
        targetSystemCredentials.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetSystemCredentialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, targetSystemCredentials.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTargetSystemCredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();
        targetSystemCredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemCredentialsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTargetSystemCredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();
        targetSystemCredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemCredentialsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTargetSystemCredentialsWithPatch() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();

        // Update the targetSystemCredentials using partial update
        TargetSystemCredentials partialUpdatedTargetSystemCredentials = new TargetSystemCredentials();
        partialUpdatedTargetSystemCredentials.setId(targetSystemCredentials.getId());

        restTargetSystemCredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetSystemCredentials.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetSystemCredentials))
            )
            .andExpect(status().isOk());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
        TargetSystemCredentials testTargetSystemCredentials = targetSystemCredentialsList.get(targetSystemCredentialsList.size() - 1);
        assertThat(testTargetSystemCredentials.getUsername()).isEqualTo(DEFAULT_USERNAME);
        assertThat(testTargetSystemCredentials.getPassword()).isEqualTo(DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void fullUpdateTargetSystemCredentialsWithPatch() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();

        // Update the targetSystemCredentials using partial update
        TargetSystemCredentials partialUpdatedTargetSystemCredentials = new TargetSystemCredentials();
        partialUpdatedTargetSystemCredentials.setId(targetSystemCredentials.getId());

        partialUpdatedTargetSystemCredentials.username(UPDATED_USERNAME).password(UPDATED_PASSWORD);

        restTargetSystemCredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTargetSystemCredentials.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTargetSystemCredentials))
            )
            .andExpect(status().isOk());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
        TargetSystemCredentials testTargetSystemCredentials = targetSystemCredentialsList.get(targetSystemCredentialsList.size() - 1);
        assertThat(testTargetSystemCredentials.getUsername()).isEqualTo(UPDATED_USERNAME);
        assertThat(testTargetSystemCredentials.getPassword()).isEqualTo(UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void patchNonExistingTargetSystemCredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();
        targetSystemCredentials.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetSystemCredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, targetSystemCredentials.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTargetSystemCredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();
        targetSystemCredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemCredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isBadRequest());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTargetSystemCredentials() throws Exception {
        int databaseSizeBeforeUpdate = targetSystemCredentialsRepository.findAll().size();
        targetSystemCredentials.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetSystemCredentialsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(targetSystemCredentials))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TargetSystemCredentials in the database
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTargetSystemCredentials() throws Exception {
        // Initialize the database
        targetSystemCredentialsRepository.saveAndFlush(targetSystemCredentials);

        int databaseSizeBeforeDelete = targetSystemCredentialsRepository.findAll().size();

        // Delete the targetSystemCredentials
        restTargetSystemCredentialsMockMvc
            .perform(delete(ENTITY_API_URL_ID, targetSystemCredentials.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TargetSystemCredentials> targetSystemCredentialsList = targetSystemCredentialsRepository.findAll();
        assertThat(targetSystemCredentialsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
