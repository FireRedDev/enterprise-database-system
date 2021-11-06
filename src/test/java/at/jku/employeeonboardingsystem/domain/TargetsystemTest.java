package at.jku.employeeonboardingsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import at.jku.employeeonboardingsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TargetsystemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Targetsystem.class);
        Targetsystem targetsystem1 = new Targetsystem();
        targetsystem1.setId(1L);
        Targetsystem targetsystem2 = new Targetsystem();
        targetsystem2.setId(targetsystem1.getId());
        assertThat(targetsystem1).isEqualTo(targetsystem2);
        targetsystem2.setId(2L);
        assertThat(targetsystem1).isNotEqualTo(targetsystem2);
        targetsystem1.setId(null);
        assertThat(targetsystem1).isNotEqualTo(targetsystem2);
    }
}
