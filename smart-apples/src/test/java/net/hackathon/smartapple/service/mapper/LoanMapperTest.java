package net.hackathon.smartapple.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoanMapperTest {

    private LoanMapper loanMapper;

    @BeforeEach
    public void setUp() {
        loanMapper = new LoanMapperImpl();
    }
}
