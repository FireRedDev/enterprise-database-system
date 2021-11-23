package at.jku.employeeonboardingsystem.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link at.jku.employeeonboardingsystem.domain.Systemuser} entity. This class is used
 * in {@link at.jku.employeeonboardingsystem.web.rest.SystemuserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /systemusers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SystemuserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LocalDateFilter entryDate;

    private StringFilter name;

    private StringFilter socialSecurityNumber;

    private StringFilter jobDescription;

    private LongFilter userId;

    private LongFilter departmentId;

    private Boolean distinct;

    public SystemuserCriteria() {}

    public SystemuserCriteria(SystemuserCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.entryDate = other.entryDate == null ? null : other.entryDate.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.socialSecurityNumber = other.socialSecurityNumber == null ? null : other.socialSecurityNumber.copy();
        this.jobDescription = other.jobDescription == null ? null : other.jobDescription.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.departmentId = other.departmentId == null ? null : other.departmentId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SystemuserCriteria copy() {
        return new SystemuserCriteria(this);
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

    public LocalDateFilter getEntryDate() {
        return entryDate;
    }

    public LocalDateFilter entryDate() {
        if (entryDate == null) {
            entryDate = new LocalDateFilter();
        }
        return entryDate;
    }

    public void setEntryDate(LocalDateFilter entryDate) {
        this.entryDate = entryDate;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getSocialSecurityNumber() {
        return socialSecurityNumber;
    }

    public StringFilter socialSecurityNumber() {
        if (socialSecurityNumber == null) {
            socialSecurityNumber = new StringFilter();
        }
        return socialSecurityNumber;
    }

    public void setSocialSecurityNumber(StringFilter socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    public StringFilter getJobDescription() {
        return jobDescription;
    }

    public StringFilter jobDescription() {
        if (jobDescription == null) {
            jobDescription = new StringFilter();
        }
        return jobDescription;
    }

    public void setJobDescription(StringFilter jobDescription) {
        this.jobDescription = jobDescription;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getDepartmentId() {
        return departmentId;
    }

    public LongFilter departmentId() {
        if (departmentId == null) {
            departmentId = new LongFilter();
        }
        return departmentId;
    }

    public void setDepartmentId(LongFilter departmentId) {
        this.departmentId = departmentId;
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
        final SystemuserCriteria that = (SystemuserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(entryDate, that.entryDate) &&
            Objects.equals(name, that.name) &&
            Objects.equals(socialSecurityNumber, that.socialSecurityNumber) &&
            Objects.equals(jobDescription, that.jobDescription) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(departmentId, that.departmentId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entryDate, name, socialSecurityNumber, jobDescription, userId, departmentId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemuserCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (entryDate != null ? "entryDate=" + entryDate + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (socialSecurityNumber != null ? "socialSecurityNumber=" + socialSecurityNumber + ", " : "") +
            (jobDescription != null ? "jobDescription=" + jobDescription + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (departmentId != null ? "departmentId=" + departmentId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
