package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class VirtualProductContractTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirtualProductContract.class);
        VirtualProductContract virtualProductContract1 = new VirtualProductContract();
        virtualProductContract1.setId(1L);
        VirtualProductContract virtualProductContract2 = new VirtualProductContract();
        virtualProductContract2.setId(virtualProductContract1.getId());
        assertThat(virtualProductContract1).isEqualTo(virtualProductContract2);
        virtualProductContract2.setId(2L);
        assertThat(virtualProductContract1).isNotEqualTo(virtualProductContract2);
        virtualProductContract1.setId(null);
        assertThat(virtualProductContract1).isNotEqualTo(virtualProductContract2);
    }
}
