package net.hackathon.smartapple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LoanSubscription.
 */
@Entity
@Table(name = "loan_subscription")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoanSubscription implements Serializable {

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
    @Min(value = 0)
    @Column(name = "sub_amt", nullable = false)
    private Integer subAmt;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @ManyToOne
    private User subscriber;

    @ManyToOne
    @JsonIgnoreProperties(value = { "owner" }, allowSetters = true)
    private Loan loan;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LoanSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public LoanSubscription hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getSubAmt() {
        return this.subAmt;
    }

    public LoanSubscription subAmt(Integer subAmt) {
        this.setSubAmt(subAmt);
        return this;
    }

    public void setSubAmt(Integer subAmt) {
        this.subAmt = subAmt;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public LoanSubscription createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public LoanSubscription updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public User getSubscriber() {
        return this.subscriber;
    }

    public void setSubscriber(User user) {
        this.subscriber = user;
    }

    public LoanSubscription subscriber(User user) {
        this.setSubscriber(user);
        return this;
    }

    public Loan getLoan() {
        return this.loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public LoanSubscription loan(Loan loan) {
        this.setLoan(loan);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanSubscription)) {
            return false;
        }
        return id != null && id.equals(((LoanSubscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanSubscription{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", subAmt=" + getSubAmt() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
