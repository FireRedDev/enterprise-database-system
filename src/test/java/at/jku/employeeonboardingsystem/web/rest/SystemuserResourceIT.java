package at.jku.employeeonboardingsystem.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import at.jku.employeeonboardingsystem.IntegrationTest;
import at.jku.employeeonboardingsystem.domain.Department;
import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.repository.SystemuserRepository;
import at.jku.employeeonboardingsystem.service.SystemuserService;
import at.jku.employeeonboardingsystem.service.criteria.SystemuserCriteria;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SystemuserResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SystemuserResourceIT {

    private static final LocalDate DEFAULT_ENTRY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ENTRY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ENTRY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOCIAL_SECURITY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_SECURITY_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_JOB_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_JOB_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/systemusers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SystemuserRepository systemuserRepository;

    @Mock
    private SystemuserRepository systemuserRepositoryMock;

    @Mock
    private SystemuserService systemuserServiceMock;

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
        Systemuser systemuser = new Systemuser()
            .entryDate(DEFAULT_ENTRY_DATE)
            .name(DEFAULT_NAME)
            .socialSecurityNumber(DEFAULT_SOCIAL_SECURITY_NUMBER)
            .jobDescription(DEFAULT_JOB_DESCRIPTION);
        return systemuser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Systemuser createUpdatedEntity(EntityManager em) {
        Systemuser systemuser = new Systemuser()
            .entryDate(UPDATED_ENTRY_DATE)
            .name(UPDATED_NAME)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .jobDescription(UPDATED_JOB_DESCRIPTION);
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
        assertThat(testSystemuser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystemuser.getSocialSecurityNumber()).isEqualTo(DEFAULT_SOCIAL_SECURITY_NUMBER);
        assertThat(testSystemuser.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
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
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].socialSecurityNumber").value(hasItem(DEFAULT_SOCIAL_SECURITY_NUMBER)))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSystemusersWithEagerRelationshipsIsEnabled() throws Exception {
        when(systemuserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSystemuserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(systemuserServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSystemusersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(systemuserServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSystemuserMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(systemuserServiceMock, times(1)).findAllWithEagerRelationships(any());
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
            .andExpect(jsonPath("$.entryDate").value(DEFAULT_ENTRY_DATE.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.socialSecurityNumber").value(DEFAULT_SOCIAL_SECURITY_NUMBER))
            .andExpect(jsonPath("$.jobDescription").value(DEFAULT_JOB_DESCRIPTION));
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
    void getAllSystemusersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where name equals to DEFAULT_NAME
        defaultSystemuserShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the systemuserList where name equals to UPDATED_NAME
        defaultSystemuserShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemusersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where name not equals to DEFAULT_NAME
        defaultSystemuserShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the systemuserList where name not equals to UPDATED_NAME
        defaultSystemuserShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemusersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where name in DEFAULT_NAME or UPDATED_NAME
        defaultSystemuserShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the systemuserList where name equals to UPDATED_NAME
        defaultSystemuserShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemusersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where name is not null
        defaultSystemuserShouldBeFound("name.specified=true");

        // Get all the systemuserList where name is null
        defaultSystemuserShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemusersByNameContainsSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where name contains DEFAULT_NAME
        defaultSystemuserShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the systemuserList where name contains UPDATED_NAME
        defaultSystemuserShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemusersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where name does not contain DEFAULT_NAME
        defaultSystemuserShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the systemuserList where name does not contain UPDATED_NAME
        defaultSystemuserShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSystemusersBySocialSecurityNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where socialSecurityNumber equals to DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldBeFound("socialSecurityNumber.equals=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the systemuserList where socialSecurityNumber equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldNotBeFound("socialSecurityNumber.equals=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllSystemusersBySocialSecurityNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where socialSecurityNumber not equals to DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldNotBeFound("socialSecurityNumber.notEquals=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the systemuserList where socialSecurityNumber not equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldBeFound("socialSecurityNumber.notEquals=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllSystemusersBySocialSecurityNumberIsInShouldWork() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where socialSecurityNumber in DEFAULT_SOCIAL_SECURITY_NUMBER or UPDATED_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldBeFound("socialSecurityNumber.in=" + DEFAULT_SOCIAL_SECURITY_NUMBER + "," + UPDATED_SOCIAL_SECURITY_NUMBER);

        // Get all the systemuserList where socialSecurityNumber equals to UPDATED_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldNotBeFound("socialSecurityNumber.in=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllSystemusersBySocialSecurityNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where socialSecurityNumber is not null
        defaultSystemuserShouldBeFound("socialSecurityNumber.specified=true");

        // Get all the systemuserList where socialSecurityNumber is null
        defaultSystemuserShouldNotBeFound("socialSecurityNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemusersBySocialSecurityNumberContainsSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where socialSecurityNumber contains DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldBeFound("socialSecurityNumber.contains=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the systemuserList where socialSecurityNumber contains UPDATED_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldNotBeFound("socialSecurityNumber.contains=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllSystemusersBySocialSecurityNumberNotContainsSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where socialSecurityNumber does not contain DEFAULT_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldNotBeFound("socialSecurityNumber.doesNotContain=" + DEFAULT_SOCIAL_SECURITY_NUMBER);

        // Get all the systemuserList where socialSecurityNumber does not contain UPDATED_SOCIAL_SECURITY_NUMBER
        defaultSystemuserShouldBeFound("socialSecurityNumber.doesNotContain=" + UPDATED_SOCIAL_SECURITY_NUMBER);
    }

    @Test
    @Transactional
    void getAllSystemusersByJobDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where jobDescription equals to DEFAULT_JOB_DESCRIPTION
        defaultSystemuserShouldBeFound("jobDescription.equals=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the systemuserList where jobDescription equals to UPDATED_JOB_DESCRIPTION
        defaultSystemuserShouldNotBeFound("jobDescription.equals=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemusersByJobDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where jobDescription not equals to DEFAULT_JOB_DESCRIPTION
        defaultSystemuserShouldNotBeFound("jobDescription.notEquals=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the systemuserList where jobDescription not equals to UPDATED_JOB_DESCRIPTION
        defaultSystemuserShouldBeFound("jobDescription.notEquals=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemusersByJobDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where jobDescription in DEFAULT_JOB_DESCRIPTION or UPDATED_JOB_DESCRIPTION
        defaultSystemuserShouldBeFound("jobDescription.in=" + DEFAULT_JOB_DESCRIPTION + "," + UPDATED_JOB_DESCRIPTION);

        // Get all the systemuserList where jobDescription equals to UPDATED_JOB_DESCRIPTION
        defaultSystemuserShouldNotBeFound("jobDescription.in=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemusersByJobDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where jobDescription is not null
        defaultSystemuserShouldBeFound("jobDescription.specified=true");

        // Get all the systemuserList where jobDescription is null
        defaultSystemuserShouldNotBeFound("jobDescription.specified=false");
    }

    @Test
    @Transactional
    void getAllSystemusersByJobDescriptionContainsSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where jobDescription contains DEFAULT_JOB_DESCRIPTION
        defaultSystemuserShouldBeFound("jobDescription.contains=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the systemuserList where jobDescription contains UPDATED_JOB_DESCRIPTION
        defaultSystemuserShouldNotBeFound("jobDescription.contains=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemusersByJobDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);

        // Get all the systemuserList where jobDescription does not contain DEFAULT_JOB_DESCRIPTION
        defaultSystemuserShouldNotBeFound("jobDescription.doesNotContain=" + DEFAULT_JOB_DESCRIPTION);

        // Get all the systemuserList where jobDescription does not contain UPDATED_JOB_DESCRIPTION
        defaultSystemuserShouldBeFound("jobDescription.doesNotContain=" + UPDATED_JOB_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllSystemusersByDepartmentIsEqualToSomething() throws Exception {
        // Initialize the database
        systemuserRepository.saveAndFlush(systemuser);
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
        systemuser.addDepartment(department);
        systemuserRepository.saveAndFlush(systemuser);
        Long departmentId = department.getId();

        // Get all the systemuserList where department equals to departmentId
        defaultSystemuserShouldBeFound("departmentId.equals=" + departmentId);

        // Get all the systemuserList where department equals to (departmentId + 1)
        defaultSystemuserShouldNotBeFound("departmentId.equals=" + (departmentId + 1));
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
            .andExpect(jsonPath("$.[*].entryDate").value(hasItem(DEFAULT_ENTRY_DATE.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].socialSecurityNumber").value(hasItem(DEFAULT_SOCIAL_SECURITY_NUMBER)))
            .andExpect(jsonPath("$.[*].jobDescription").value(hasItem(DEFAULT_JOB_DESCRIPTION)));

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
        updatedSystemuser
            .entryDate(UPDATED_ENTRY_DATE)
            .name(UPDATED_NAME)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .jobDescription(UPDATED_JOB_DESCRIPTION);

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
        assertThat(testSystemuser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemuser.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testSystemuser.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
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
        assertThat(testSystemuser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSystemuser.getSocialSecurityNumber()).isEqualTo(DEFAULT_SOCIAL_SECURITY_NUMBER);
        assertThat(testSystemuser.getJobDescription()).isEqualTo(DEFAULT_JOB_DESCRIPTION);
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

        partialUpdatedSystemuser
            .entryDate(UPDATED_ENTRY_DATE)
            .name(UPDATED_NAME)
            .socialSecurityNumber(UPDATED_SOCIAL_SECURITY_NUMBER)
            .jobDescription(UPDATED_JOB_DESCRIPTION);

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
        assertThat(testSystemuser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSystemuser.getSocialSecurityNumber()).isEqualTo(UPDATED_SOCIAL_SECURITY_NUMBER);
        assertThat(testSystemuser.getJobDescription()).isEqualTo(UPDATED_JOB_DESCRIPTION);
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
