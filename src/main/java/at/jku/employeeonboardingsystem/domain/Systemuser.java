package at.jku.employeeonboardingsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Systemuser.
 */
@Entity
@Table(name = "systemuser")
public class Systemuser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "name")
    private String name;

    @Column(name = "social_security_number")
    private String socialSecurityNumber;

    @Column(name = "job_description")
    private String jobDescription;

    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
        name = "rel_systemuser__department",
        joinColumns = @JoinColumn(name = "systemuser_id"),
        inverseJoinColumns = @JoinColumn(name = "department_id")
    )
    @JsonIgnoreProperties(value = { "targetsystems" }, allowSetters = true)
    private Set<Department> departments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Systemuser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getEntryDate() {
        return this.entryDate;
    }

    public Systemuser entryDate(LocalDate entryDate) {
        this.setEntryDate(entryDate);
        return this;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public String getName() {
        return this.name;
    }

    public Systemuser name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSocialSecurityNumber() {
        return this.socialSecurityNumber;
    }

    public Systemuser socialSecurityNumber(String socialSecurityNumber) {
        this.setSocialSecurityNumber(socialSecurityNumber);
        return this;
    }

    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public String getJobDescription() {
        return this.jobDescription;
    }

    public Systemuser jobDescription(String jobDescription) {
        this.setJobDescription(jobDescription);
        return this;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Systemuser user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Department> getDepartments() {
        return this.departments;
    }

    public void setDepartments(Set<Department> departments) {
        this.departments = departments;
    }

    public Systemuser departments(Set<Department> departments) {
        this.setDepartments(departments);
        return this;
    }

    public Systemuser addDepartment(Department department) {
        this.departments.add(department);
        return this;
    }

    public Systemuser removeDepartment(Department department) {
        this.departments.remove(department);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Systemuser)) {
            return false;
        }
        return id != null && id.equals(((Systemuser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Systemuser{" +
            "id=" + getId() +
            ", entryDate='" + getEntryDate() + "'" +
            ", name='" + getName() + "'" +
            ", socialSecurityNumber='" + getSocialSecurityNumber() + "'" +
            ", jobDescription='" + getJobDescription() + "'" +
            "}";
    }
}
