package at.jku.employeeonboardingsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import at.jku.employeeonboardingsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TargetsystemcredentialsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Targetsystemcredentials.class);
        Targetsystemcredentials targetsystemcredentials1 = new Targetsystemcredentials();
        targetsystemcredentials1.setId(1L);
        Targetsystemcredentials targetsystemcredentials2 = new Targetsystemcredentials();
        targetsystemcredentials2.setId(targetsystemcredentials1.getId());
        assertThat(targetsystemcredentials1).isEqualTo(targetsystemcredentials2);
        targetsystemcredentials2.setId(2L);
        assertThat(targetsystemcredentials1).isNotEqualTo(targetsystemcredentials2);
        targetsystemcredentials1.setId(null);
        assertThat(targetsystemcredentials1).isNotEqualTo(targetsystemcredentials2);
    }
}
