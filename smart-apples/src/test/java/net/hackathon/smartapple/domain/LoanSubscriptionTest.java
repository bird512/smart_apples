package net.hackathon.smartapple.domain;

import static org.assertj.core.api.Assertions.assertThat;

import net.hackathon.smartapple.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LoanSubscriptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LoanSubscription.class);
        LoanSubscription loanSubscription1 = new LoanSubscription();
        loanSubscription1.setId(1L);
        LoanSubscription loanSubscription2 = new LoanSubscription();
        loanSubscription2.setId(loanSubscription1.getId());
        assertThat(loanSubscription1).isEqualTo(loanSubscription2);
        loanSubscription2.setId(2L);
        assertThat(loanSubscription1).isNotEqualTo(loanSubscription2);
        loanSubscription1.setId(null);
        assertThat(loanSubscription1).isNotEqualTo(loanSubscription2);
    }
}
