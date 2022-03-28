package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class VirtualProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirtualProduct.class);
        VirtualProduct virtualProduct1 = new VirtualProduct();
        virtualProduct1.setId(1L);
        VirtualProduct virtualProduct2 = new VirtualProduct();
        virtualProduct2.setId(virtualProduct1.getId());
        assertThat(virtualProduct1).isEqualTo(virtualProduct2);
        virtualProduct2.setId(2L);
        assertThat(virtualProduct1).isNotEqualTo(virtualProduct2);
        virtualProduct1.setId(null);
        assertThat(virtualProduct1).isNotEqualTo(virtualProduct2);
    }
}
