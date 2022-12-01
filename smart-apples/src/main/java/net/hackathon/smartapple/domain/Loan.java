package net.hackathon.smartapple.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import net.hackathon.smartapple.domain.enumeration.CURRENCY;
import net.hackathon.smartapple.domain.enumeration.LoanStatus;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Loan.
 */
@Entity
@Table(name = "loan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Loan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "hash", length = 100, nullable = false)
    private String hash;

    @NotNull
    @Min(value = 10000)
    @Column(name = "loan_amt", nullable = false)
    private Integer loanAmt;

    @NotNull
    @Min(value = 0)
    @Column(name = "available_amt", nullable = false)
    private Integer availableAmt;

    @NotNull
    @Min(value = 0)
    @Column(name = "interest_rate", nullable = false)
    private Integer interestRate;

    @NotNull
    @Min(value = 1)
    @Max(value = 12)
    @Column(name = "terms", nullable = false)
    private Integer terms;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private CURRENCY currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LoanStatus status;

    @ManyToOne
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Loan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public Loan hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getLoanAmt() {
        return this.loanAmt;
    }

    public Loan loanAmt(Integer loanAmt) {
        this.setLoanAmt(loanAmt);
        return this;
    }

    public void setLoanAmt(Integer loanAmt) {
        this.loanAmt = loanAmt;
    }

    public Integer getAvailableAmt() {
        return this.availableAmt;
    }

    public Loan availableAmt(Integer availableAmt) {
        this.setAvailableAmt(availableAmt);
        return this;
    }

    public void setAvailableAmt(Integer availableAmt) {
        this.availableAmt = availableAmt;
    }

    public Integer getInterestRate() {
        return this.interestRate;
    }

    public Loan interestRate(Integer interestRate) {
        this.setInterestRate(interestRate);
        return this;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTerms() {
        return this.terms;
    }

    public Loan terms(Integer terms) {
        this.setTerms(terms);
        return this;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public Loan createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public Loan updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public CURRENCY getCurrency() {
        return this.currency;
    }

    public Loan currency(CURRENCY currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(CURRENCY currency) {
        this.currency = currency;
    }

    public LoanStatus getStatus() {
        return this.status;
    }

    public Loan status(LoanStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Loan owner(User user) {
        this.setOwner(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Loan)) {
            return false;
        }
        return id != null && id.equals(((Loan) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Loan{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", loanAmt=" + getLoanAmt() +
            ", availableAmt=" + getAvailableAmt() +
            ", interestRate=" + getInterestRate() +
            ", terms=" + getTerms() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
