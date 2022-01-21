package at.jku.employeeonboardingsystem.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * A Targetsystemcredentials.
 */
@XmlRootElement
@Entity
@Table(name = "targetsystemcredentials")
public class Targetsystemcredentials implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Expose
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "username")
    private String username;

    @Expose
    @NotNull
    @Size(min = 6)
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departments" }, allowSetters = true)
    private Systemuser systemuser;

    @ManyToOne
    @XmlTransient
    private Targetsystem targetsystem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Targetsystemcredentials id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public Targetsystemcredentials username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Targetsystemcredentials password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Systemuser getSystemuser() {
        return this.systemuser;
    }

    public void setSystemuser(Systemuser systemuser) {
        this.systemuser = systemuser;
    }

    public Targetsystemcredentials systemuser(Systemuser systemuser) {
        this.setSystemuser(systemuser);
        return this;
    }

    @XmlTransient
    public Targetsystem getTargetsystem() {
        return this.targetsystem;
    }

    public void setTargetsystem(Targetsystem targetsystem) {
        this.targetsystem = targetsystem;
    }

    public Targetsystemcredentials targetsystem(Targetsystem targetsystem) {
        this.setTargetsystem(targetsystem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Targetsystemcredentials)) {
            return false;
        }
        return id != null && id.equals(((Targetsystemcredentials) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Targetsystemcredentials{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
