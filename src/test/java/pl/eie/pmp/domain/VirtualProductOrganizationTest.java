package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class VirtualProductOrganizationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirtualProductOrganization.class);
        VirtualProductOrganization virtualProductOrganization1 = new VirtualProductOrganization();
        virtualProductOrganization1.setId(1L);
        VirtualProductOrganization virtualProductOrganization2 = new VirtualProductOrganization();
        virtualProductOrganization2.setId(virtualProductOrganization1.getId());
        assertThat(virtualProductOrganization1).isEqualTo(virtualProductOrganization2);
        virtualProductOrganization2.setId(2L);
        assertThat(virtualProductOrganization1).isNotEqualTo(virtualProductOrganization2);
        virtualProductOrganization1.setId(null);
        assertThat(virtualProductOrganization1).isNotEqualTo(virtualProductOrganization2);
    }
}
