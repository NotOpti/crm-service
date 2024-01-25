package com.crm.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A People.
 */
@Entity
@Table(name = "people")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class People implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "people_id", nullable = false)
    private String peopleId;

    @Column(name = "personal_data")
    private String personalData;

    @Column(name = "demographic_data")
    private String demographicData;

    @NotNull
    @Column(name = "date_of_contact", nullable = false)
    private Instant dateOfContact;

    @Column(name = "employee")
    private String employee;

    @Column(name = "reason")
    private String reason;

    @Column(name = "description")
    private String description;

    @Column(name = "action")
    private String action;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public People id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPeopleId() {
        return this.peopleId;
    }

    public People peopleId(String peopleId) {
        this.setPeopleId(peopleId);
        return this;
    }

    public void setPeopleId(String peopleId) {
        this.peopleId = peopleId;
    }

    public String getPersonalData() {
        return this.personalData;
    }

    public People personalData(String personalData) {
        this.setPersonalData(personalData);
        return this;
    }

    public void setPersonalData(String personalData) {
        this.personalData = personalData;
    }

    public String getDemographicData() {
        return this.demographicData;
    }

    public People demographicData(String demographicData) {
        this.setDemographicData(demographicData);
        return this;
    }

    public void setDemographicData(String demographicData) {
        this.demographicData = demographicData;
    }

    public Instant getDateOfContact() {
        return this.dateOfContact;
    }

    public People dateOfContact(Instant dateOfContact) {
        this.setDateOfContact(dateOfContact);
        return this;
    }

    public void setDateOfContact(Instant dateOfContact) {
        this.dateOfContact = dateOfContact;
    }

    public String getEmployee() {
        return this.employee;
    }

    public People employee(String employee) {
        this.setEmployee(employee);
        return this;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getReason() {
        return this.reason;
    }

    public People reason(String reason) {
        this.setReason(reason);
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDescription() {
        return this.description;
    }

    public People description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAction() {
        return this.action;
    }

    public People action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof People)) {
            return false;
        }
        return id != null && id.equals(((People) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "People{" +
            "id=" + getId() +
            ", peopleId='" + getPeopleId() + "'" +
            ", personalData='" + getPersonalData() + "'" +
            ", demographicData='" + getDemographicData() + "'" +
            ", dateOfContact='" + getDateOfContact() + "'" +
            ", employee='" + getEmployee() + "'" +
            ", reason='" + getReason() + "'" +
            ", description='" + getDescription() + "'" +
            ", action='" + getAction() + "'" +
            "}";
    }
}
