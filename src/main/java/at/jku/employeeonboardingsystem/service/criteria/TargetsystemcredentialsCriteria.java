package at.jku.employeeonboardingsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link at.jku.employeeonboardingsystem.domain.Targetsystemcredentials} entity. This class is used
 * in {@link at.jku.employeeonboardingsystem.web.rest.TargetsystemcredentialsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /targetsystemcredentials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TargetsystemcredentialsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter username;

    private StringFilter password;

    private LongFilter systemuserId;

    private LongFilter targetsystemId;

    private Boolean distinct;

    public TargetsystemcredentialsCriteria() {}

    public TargetsystemcredentialsCriteria(TargetsystemcredentialsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.username = other.username == null ? null : other.username.copy();
        this.password = other.password == null ? null : other.password.copy();
        this.systemuserId = other.systemuserId == null ? null : other.systemuserId.copy();
        this.targetsystemId = other.targetsystemId == null ? null : other.targetsystemId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TargetsystemcredentialsCriteria copy() {
        return new TargetsystemcredentialsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUsername() {
        return username;
    }

    public StringFilter username() {
        if (username == null) {
            username = new StringFilter();
        }
        return username;
    }

    public void setUsername(StringFilter username) {
        this.username = username;
    }

    public StringFilter getPassword() {
        return password;
    }

    public StringFilter password() {
        if (password == null) {
            password = new StringFilter();
        }
        return password;
    }

    public void setPassword(StringFilter password) {
        this.password = password;
    }

    public LongFilter getSystemuserId() {
        return systemuserId;
    }

    public LongFilter systemuserId() {
        if (systemuserId == null) {
            systemuserId = new LongFilter();
        }
        return systemuserId;
    }

    public void setSystemuserId(LongFilter systemuserId) {
        this.systemuserId = systemuserId;
    }

    public LongFilter getTargetsystemId() {
        return targetsystemId;
    }

    public LongFilter targetsystemId() {
        if (targetsystemId == null) {
            targetsystemId = new LongFilter();
        }
        return targetsystemId;
    }

    public void setTargetsystemId(LongFilter targetsystemId) {
        this.targetsystemId = targetsystemId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TargetsystemcredentialsCriteria that = (TargetsystemcredentialsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(systemuserId, that.systemuserId) &&
            Objects.equals(targetsystemId, that.targetsystemId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, systemuserId, targetsystemId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TargetsystemcredentialsCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (username != null ? "username=" + username + ", " : "") +
            (password != null ? "password=" + password + ", " : "") +
            (systemuserId != null ? "systemuserId=" + systemuserId + ", " : "") +
            (targetsystemId != null ? "targetsystemId=" + targetsystemId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
