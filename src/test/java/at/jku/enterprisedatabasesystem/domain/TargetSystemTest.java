package at.jku.enterprisedatabasesystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import at.jku.enterprisedatabasesystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TargetSystemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TargetSystem.class);
        TargetSystem targetSystem1 = new TargetSystem();
        targetSystem1.setId(1L);
        TargetSystem targetSystem2 = new TargetSystem();
        targetSystem2.setId(targetSystem1.getId());
        assertThat(targetSystem1).isEqualTo(targetSystem2);
        targetSystem2.setId(2L);
        assertThat(targetSystem1).isNotEqualTo(targetSystem2);
        targetSystem1.setId(null);
        assertThat(targetSystem1).isNotEqualTo(targetSystem2);
    }
}
