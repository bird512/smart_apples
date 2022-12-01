package net.hackathon.smartapple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanSubscriptionMapperTest {

    private LoanSubscriptionMapper loanSubscriptionMapper;

    @BeforeEach
    public void setUp() {
        loanSubscriptionMapper = new LoanSubscriptionMapperImpl();
    }
}
