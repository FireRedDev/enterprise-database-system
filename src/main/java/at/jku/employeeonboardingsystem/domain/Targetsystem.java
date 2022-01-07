package at.jku.employeeonboardingsystem.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * A Targetsystem.
 */
@Entity
@Table(name = "targetsystem")
public class Targetsystem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private TargetSystemTypes systemTypes;

    @Column(name = "dbUrl")
    private String dbUrl;

    @Column(name = "dbUser")
    private String dbUser;

    @Column(name = "dbPassword")
    private String dbPassword;

    @Column(name = "csvAttributes")
    private String csvAttributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Targetsystem id(Long id) {
        this.setId(id);
        return this;
    }

    public TargetSystemTypes getSystemTypes() {
        return systemTypes;
    }

    public void setSystemTypes(TargetSystemTypes systemTypes) {
        this.systemTypes = systemTypes;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getCsvAttributes() {
        return csvAttributes;
    }

    public void setCsvAttributes(String csvAttributes) {
        this.csvAttributes = csvAttributes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Targetsystem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Targetsystem)) {
            return false;
        }
        return id != null && id.equals(((Targetsystem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Targetsystem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
