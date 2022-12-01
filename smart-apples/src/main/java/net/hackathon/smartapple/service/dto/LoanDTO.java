package net.hackathon.smartapple.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import net.hackathon.smartapple.domain.enumeration.CURRENCY;
import net.hackathon.smartapple.domain.enumeration.LoanStatus;

/**
 * A DTO for the {@link net.hackathon.smartapple.domain.Loan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LoanDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String hash;

    @NotNull
    private Integer loanAmt;

    @NotNull
    private Integer availableAmt;

    @NotNull
    @Min(value = 0)
    private Integer interestRate;

    @NotNull
    @Min(value = 2)
    @Max(value = 12)
    private Integer terms;

    private Instant createAt;

    private Instant updateAt;

    private CURRENCY currency;

    private LoanStatus status;

    private UserDTO owner;

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

    public Integer getLoanAmt() {
        return loanAmt;
    }

    public void setLoanAmt(Integer loanAmt) {
        this.loanAmt = loanAmt;
    }

    public Integer getAvailableAmt() {
        return availableAmt;
    }

    public void setAvailableAmt(Integer availableAmt) {
        this.availableAmt = availableAmt;
    }

    public Integer getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Integer interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getTerms() {
        return terms;
    }

    public void setTerms(Integer terms) {
        this.terms = terms;
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

    public CURRENCY getCurrency() {
        return currency;
    }

    public void setCurrency(CURRENCY currency) {
        this.currency = currency;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LoanDTO)) {
            return false;
        }

        LoanDTO loanDTO = (LoanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, loanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LoanDTO{" +
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
            ", owner=" + getOwner() +
            "}";
    }
}
