package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class VirtualProductLineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirtualProductLine.class);
        VirtualProductLine virtualProductLine1 = new VirtualProductLine();
        virtualProductLine1.setId(1L);
        VirtualProductLine virtualProductLine2 = new VirtualProductLine();
        virtualProductLine2.setId(virtualProductLine1.getId());
        assertThat(virtualProductLine1).isEqualTo(virtualProductLine2);
        virtualProductLine2.setId(2L);
        assertThat(virtualProductLine1).isNotEqualTo(virtualProductLine2);
        virtualProductLine1.setId(null);
        assertThat(virtualProductLine1).isNotEqualTo(virtualProductLine2);
    }
}
