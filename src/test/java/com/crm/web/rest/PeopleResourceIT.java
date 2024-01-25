package com.crm.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.crm.IntegrationTest;
import com.crm.domain.People;
import com.crm.repository.PeopleRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PeopleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeopleResourceIT {

    private static final String DEFAULT_PEOPLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PEOPLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PERSONAL_DATA = "AAAAAAAAAA";
    private static final String UPDATED_PERSONAL_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_DEMOGRAPHIC_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DEMOGRAPHIC_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_OF_CONTACT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_OF_CONTACT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_EMPLOYEE = "AAAAAAAAAA";
    private static final String UPDATED_EMPLOYEE = "BBBBBBBBBB";

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/people";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeopleMockMvc;

    private People people;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static People createEntity(EntityManager em) {
        People people = new People()
            .peopleId(DEFAULT_PEOPLE_ID)
            .personalData(DEFAULT_PERSONAL_DATA)
            .demographicData(DEFAULT_DEMOGRAPHIC_DATA)
            .dateOfContact(DEFAULT_DATE_OF_CONTACT)
            .employee(DEFAULT_EMPLOYEE)
            .reason(DEFAULT_REASON)
            .description(DEFAULT_DESCRIPTION)
            .action(DEFAULT_ACTION);
        return people;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static People createUpdatedEntity(EntityManager em) {
        People people = new People()
            .peopleId(UPDATED_PEOPLE_ID)
            .personalData(UPDATED_PERSONAL_DATA)
            .demographicData(UPDATED_DEMOGRAPHIC_DATA)
            .dateOfContact(UPDATED_DATE_OF_CONTACT)
            .employee(UPDATED_EMPLOYEE)
            .reason(UPDATED_REASON)
            .description(UPDATED_DESCRIPTION)
            .action(UPDATED_ACTION);
        return people;
    }

    @BeforeEach
    public void initTest() {
        people = createEntity(em);
    }

    @Test
    @Transactional
    void createPeople() throws Exception {
        int databaseSizeBeforeCreate = peopleRepository.findAll().size();
        // Create the People
        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(people)))
            .andExpect(status().isCreated());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeCreate + 1);
        People testPeople = peopleList.get(peopleList.size() - 1);
        assertThat(testPeople.getPeopleId()).isEqualTo(DEFAULT_PEOPLE_ID);
        assertThat(testPeople.getPersonalData()).isEqualTo(DEFAULT_PERSONAL_DATA);
        assertThat(testPeople.getDemographicData()).isEqualTo(DEFAULT_DEMOGRAPHIC_DATA);
        assertThat(testPeople.getDateOfContact()).isEqualTo(DEFAULT_DATE_OF_CONTACT);
        assertThat(testPeople.getEmployee()).isEqualTo(DEFAULT_EMPLOYEE);
        assertThat(testPeople.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testPeople.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPeople.getAction()).isEqualTo(DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void createPeopleWithExistingId() throws Exception {
        // Create the People with an existing ID
        people.setId(1L);

        int databaseSizeBeforeCreate = peopleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(people)))
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPeopleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = peopleRepository.findAll().size();
        // set the field null
        people.setPeopleId(null);

        // Create the People, which fails.

        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(people)))
            .andExpect(status().isBadRequest());

        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = peopleRepository.findAll().size();
        // set the field null
        people.setDateOfContact(null);

        // Create the People, which fails.

        restPeopleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(people)))
            .andExpect(status().isBadRequest());

        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        // Get all the peopleList
        restPeopleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(people.getId().intValue())))
            .andExpect(jsonPath("$.[*].peopleId").value(hasItem(DEFAULT_PEOPLE_ID)))
            .andExpect(jsonPath("$.[*].personalData").value(hasItem(DEFAULT_PERSONAL_DATA)))
            .andExpect(jsonPath("$.[*].demographicData").value(hasItem(DEFAULT_DEMOGRAPHIC_DATA)))
            .andExpect(jsonPath("$.[*].dateOfContact").value(hasItem(DEFAULT_DATE_OF_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].employee").value(hasItem(DEFAULT_EMPLOYEE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)));
    }

    @Test
    @Transactional
    void getPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        // Get the people
        restPeopleMockMvc
            .perform(get(ENTITY_API_URL_ID, people.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(people.getId().intValue()))
            .andExpect(jsonPath("$.peopleId").value(DEFAULT_PEOPLE_ID))
            .andExpect(jsonPath("$.personalData").value(DEFAULT_PERSONAL_DATA))
            .andExpect(jsonPath("$.demographicData").value(DEFAULT_DEMOGRAPHIC_DATA))
            .andExpect(jsonPath("$.dateOfContact").value(DEFAULT_DATE_OF_CONTACT.toString()))
            .andExpect(jsonPath("$.employee").value(DEFAULT_EMPLOYEE))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION));
    }

    @Test
    @Transactional
    void getNonExistingPeople() throws Exception {
        // Get the people
        restPeopleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();

        // Update the people
        People updatedPeople = peopleRepository.findById(people.getId()).get();
        // Disconnect from session so that the updates on updatedPeople are not directly saved in db
        em.detach(updatedPeople);
        updatedPeople
            .peopleId(UPDATED_PEOPLE_ID)
            .personalData(UPDATED_PERSONAL_DATA)
            .demographicData(UPDATED_DEMOGRAPHIC_DATA)
            .dateOfContact(UPDATED_DATE_OF_CONTACT)
            .employee(UPDATED_EMPLOYEE)
            .reason(UPDATED_REASON)
            .description(UPDATED_DESCRIPTION)
            .action(UPDATED_ACTION);

        restPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeople.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeople))
            )
            .andExpect(status().isOk());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
        People testPeople = peopleList.get(peopleList.size() - 1);
        assertThat(testPeople.getPeopleId()).isEqualTo(UPDATED_PEOPLE_ID);
        assertThat(testPeople.getPersonalData()).isEqualTo(UPDATED_PERSONAL_DATA);
        assertThat(testPeople.getDemographicData()).isEqualTo(UPDATED_DEMOGRAPHIC_DATA);
        assertThat(testPeople.getDateOfContact()).isEqualTo(UPDATED_DATE_OF_CONTACT);
        assertThat(testPeople.getEmployee()).isEqualTo(UPDATED_EMPLOYEE);
        assertThat(testPeople.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testPeople.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPeople.getAction()).isEqualTo(UPDATED_ACTION);
    }

    @Test
    @Transactional
    void putNonExistingPeople() throws Exception {
        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();
        people.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, people.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(people))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeople() throws Exception {
        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();
        people.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(people))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeople() throws Exception {
        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();
        people.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(people)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeopleWithPatch() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();

        // Update the people using partial update
        People partialUpdatedPeople = new People();
        partialUpdatedPeople.setId(people.getId());

        partialUpdatedPeople.personalData(UPDATED_PERSONAL_DATA).demographicData(UPDATED_DEMOGRAPHIC_DATA).employee(UPDATED_EMPLOYEE);

        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeople.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeople))
            )
            .andExpect(status().isOk());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
        People testPeople = peopleList.get(peopleList.size() - 1);
        assertThat(testPeople.getPeopleId()).isEqualTo(DEFAULT_PEOPLE_ID);
        assertThat(testPeople.getPersonalData()).isEqualTo(UPDATED_PERSONAL_DATA);
        assertThat(testPeople.getDemographicData()).isEqualTo(UPDATED_DEMOGRAPHIC_DATA);
        assertThat(testPeople.getDateOfContact()).isEqualTo(DEFAULT_DATE_OF_CONTACT);
        assertThat(testPeople.getEmployee()).isEqualTo(UPDATED_EMPLOYEE);
        assertThat(testPeople.getReason()).isEqualTo(DEFAULT_REASON);
        assertThat(testPeople.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPeople.getAction()).isEqualTo(DEFAULT_ACTION);
    }

    @Test
    @Transactional
    void fullUpdatePeopleWithPatch() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();

        // Update the people using partial update
        People partialUpdatedPeople = new People();
        partialUpdatedPeople.setId(people.getId());

        partialUpdatedPeople
            .peopleId(UPDATED_PEOPLE_ID)
            .personalData(UPDATED_PERSONAL_DATA)
            .demographicData(UPDATED_DEMOGRAPHIC_DATA)
            .dateOfContact(UPDATED_DATE_OF_CONTACT)
            .employee(UPDATED_EMPLOYEE)
            .reason(UPDATED_REASON)
            .description(UPDATED_DESCRIPTION)
            .action(UPDATED_ACTION);

        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeople.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeople))
            )
            .andExpect(status().isOk());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
        People testPeople = peopleList.get(peopleList.size() - 1);
        assertThat(testPeople.getPeopleId()).isEqualTo(UPDATED_PEOPLE_ID);
        assertThat(testPeople.getPersonalData()).isEqualTo(UPDATED_PERSONAL_DATA);
        assertThat(testPeople.getDemographicData()).isEqualTo(UPDATED_DEMOGRAPHIC_DATA);
        assertThat(testPeople.getDateOfContact()).isEqualTo(UPDATED_DATE_OF_CONTACT);
        assertThat(testPeople.getEmployee()).isEqualTo(UPDATED_EMPLOYEE);
        assertThat(testPeople.getReason()).isEqualTo(UPDATED_REASON);
        assertThat(testPeople.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPeople.getAction()).isEqualTo(UPDATED_ACTION);
    }

    @Test
    @Transactional
    void patchNonExistingPeople() throws Exception {
        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();
        people.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, people.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(people))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeople() throws Exception {
        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();
        people.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(people))
            )
            .andExpect(status().isBadRequest());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeople() throws Exception {
        int databaseSizeBeforeUpdate = peopleRepository.findAll().size();
        people.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeopleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(people)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the People in the database
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeople() throws Exception {
        // Initialize the database
        peopleRepository.saveAndFlush(people);

        int databaseSizeBeforeDelete = peopleRepository.findAll().size();

        // Delete the people
        restPeopleMockMvc
            .perform(delete(ENTITY_API_URL_ID, people.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<People> peopleList = peopleRepository.findAll();
        assertThat(peopleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
