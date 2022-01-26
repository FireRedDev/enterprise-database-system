package at.jku.employeeonboardingsystem.domain;

import com.google.gson.annotations.Expose;
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
    @Expose
    @Column(name = "id")
    private Long id;

    @Expose
    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "url")
    private String url;

    @Column(name = "password")
    private String password;

    @Column(name = "username")
    private String username;

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

    public String getType() {
        return this.type;
    }

    public Targetsystem type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return this.url;
    }

    public Targetsystem url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return this.password;
    }

    public Targetsystem password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public Targetsystem username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Targetsystem csvAttributes(String csvAttributes) {
        this.setCsvAttributes(csvAttributes);
        return this;
    }

    public String getCsvAttributes() {
        return csvAttributes;
    }

    public void setCsvAttributes(String csvAttributes) {
        this.csvAttributes = csvAttributes;
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
            ", type='" + getType() + "'" +
            ", url='" + getUrl() + "'" +
            ", password='" + getPassword() + "'" +
            ", username='" + getUsername() + "'" +
            ",csvAttributes='"+getCsvAttributes()+"'"+
            "}";
    }
}
