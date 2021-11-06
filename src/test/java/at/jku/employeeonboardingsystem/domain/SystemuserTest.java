package at.jku.employeeonboardingsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

import at.jku.employeeonboardingsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemuserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Systemuser.class);
        Systemuser systemuser1 = new Systemuser();
        systemuser1.setId(1L);
        Systemuser systemuser2 = new Systemuser();
        systemuser2.setId(systemuser1.getId());
        assertThat(systemuser1).isEqualTo(systemuser2);
        systemuser2.setId(2L);
        assertThat(systemuser1).isNotEqualTo(systemuser2);
        systemuser1.setId(null);
        assertThat(systemuser1).isNotEqualTo(systemuser2);
    }
}
