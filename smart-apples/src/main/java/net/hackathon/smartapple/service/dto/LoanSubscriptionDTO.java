package net.hackathon.smartapple.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link net.hackathon.smartapple.domain.LoanSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoanSubscriptionDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String hash;

    @NotNull
    @Min(value = 0)
    private Integer subAmt;

    private Instant createAt;

    private Instant updateAt;

    private UserDTO subscriber;

    private LoanDTO loan;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getSubAmt() {
        return subAmt;
    }

    public void setSubAmt(Integer subAmt) {
        this.subAmt = subAmt;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public UserDTO getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(UserDTO subscriber) {
        this.subscriber = subscriber;
    }

    public LoanDTO getLoan() {
        return loan;
    }

    public void setLoan(LoanDTO loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanSubscriptionDTO)) {
            return false;
        }

        LoanSubscriptionDTO loanSubscriptionDTO = (LoanSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanSubscriptionDTO{" +
            "id=" + getId() +
            ", hash='" + getHash() + "'" +
            ", subAmt=" + getSubAmt() +
            ", createAt='" + getCreateAt() + "'" +
            ", updateAt='" + getUpdateAt() + "'" +
            ", subscriber=" + getSubscriber() +
            ", loan=" + getLoan() +
            "}";
    }
}
