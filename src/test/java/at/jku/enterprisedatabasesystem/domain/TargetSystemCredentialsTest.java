package at.jku.enterprisedatabasesystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import at.jku.enterprisedatabasesystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TargetSystemCredentialsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TargetSystemCredentials.class);
        TargetSystemCredentials targetSystemCredentials1 = new TargetSystemCredentials();
        targetSystemCredentials1.setId(1L);
        TargetSystemCredentials targetSystemCredentials2 = new TargetSystemCredentials();
        targetSystemCredentials2.setId(targetSystemCredentials1.getId());
        assertThat(targetSystemCredentials1).isEqualTo(targetSystemCredentials2);
        targetSystemCredentials2.setId(2L);
        assertThat(targetSystemCredentials1).isNotEqualTo(targetSystemCredentials2);
        targetSystemCredentials1.setId(null);
        assertThat(targetSystemCredentials1).isNotEqualTo(targetSystemCredentials2);
    }
}
