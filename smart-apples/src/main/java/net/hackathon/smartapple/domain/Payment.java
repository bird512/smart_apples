package net.hackathon.smartapple.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Payment implements Serializable {

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
    @Column(name = "interest", nullable = false)
    private Integer interest;

    @NotNull
    @Min(value = 0)
    @Column(name = "principal", nullable = false)
    private Integer principal;

    @Column(name = "create_at")
    private Instant createAt;

    @Column(name = "update_at")
    private Instant updateAt;

    @ManyToOne
    @JsonIgnoreProperties(value = { "subscriber", "loan" }, allowSetters = true)
    private LoanSubscription subscription;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Payment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return this.hash;
    }

    public Payment hash(String hash) {
        this.setHash(hash);
        return this;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getInterest() {
        return this.interest;
    }

    public Payment interest(Integer interest) {
        this.setInterest(interest);
        return this;
    }

    public void setInterest(Integer interest) {
        this.interest = interest;
    }

    public Integer getPrincipal() {
        return this.principal;
    }

    public Payment principal(Integer principal) {
        this.setPrincipal(principal);
        return this;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    public Instant getCreateAt() {
        return this.createAt;
    }

    public Payment createAt(Instant createAt) {
        this.setCreateAt(createAt);
        return this;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return this.updateAt;
    }

    public Payment updateAt(Instant updateAt) {
        this.setUpdateAt(updateAt);
        return this;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public LoanSubscription getSubscription() {
        return this.subscription;
    }

    public void setSubscription(LoanSubscription loanSubscription) {
        this.subscription = loanSubscription;
    }

    public Payment subscription(LoanSubscription loanSubscription) {
        this.setSubscription(loanSubscription);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return id != null && id.equals(((Payment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", interest=" + getInterest() +
            ", principal=" + getPrincipal() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            "}";
    }
}
