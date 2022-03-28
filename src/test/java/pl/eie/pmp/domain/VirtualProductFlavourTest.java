package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class VirtualProductFlavourTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirtualProductFlavour.class);
        VirtualProductFlavour virtualProductFlavour1 = new VirtualProductFlavour();
        virtualProductFlavour1.setId(1L);
        VirtualProductFlavour virtualProductFlavour2 = new VirtualProductFlavour();
        virtualProductFlavour2.setId(virtualProductFlavour1.getId());
        assertThat(virtualProductFlavour1).isEqualTo(virtualProductFlavour2);
        virtualProductFlavour2.setId(2L);
        assertThat(virtualProductFlavour1).isNotEqualTo(virtualProductFlavour2);
        virtualProductFlavour1.setId(null);
        assertThat(virtualProductFlavour1).isNotEqualTo(virtualProductFlavour2);
    }
}
