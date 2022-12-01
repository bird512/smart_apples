package net.hackathon.smartapple.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import net.hackathon.smartapple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoanSubscriptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanSubscriptionDTO.class);
        LoanSubscriptionDTO loanSubscriptionDTO1 = new LoanSubscriptionDTO();
        loanSubscriptionDTO1.setId(1L);
        LoanSubscriptionDTO loanSubscriptionDTO2 = new LoanSubscriptionDTO();
        assertThat(loanSubscriptionDTO1).isNotEqualTo(loanSubscriptionDTO2);
        loanSubscriptionDTO2.setId(loanSubscriptionDTO1.getId());
        assertThat(loanSubscriptionDTO1).isEqualTo(loanSubscriptionDTO2);
        loanSubscriptionDTO2.setId(2L);
        assertThat(loanSubscriptionDTO1).isNotEqualTo(loanSubscriptionDTO2);
        loanSubscriptionDTO1.setId(null);
        assertThat(loanSubscriptionDTO1).isNotEqualTo(loanSubscriptionDTO2);
    }
}
