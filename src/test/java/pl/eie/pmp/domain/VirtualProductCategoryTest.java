package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class VirtualProductCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirtualProductCategory.class);
        VirtualProductCategory virtualProductCategory1 = new VirtualProductCategory();
        virtualProductCategory1.setId(1L);
        VirtualProductCategory virtualProductCategory2 = new VirtualProductCategory();
        virtualProductCategory2.setId(virtualProductCategory1.getId());
        assertThat(virtualProductCategory1).isEqualTo(virtualProductCategory2);
        virtualProductCategory2.setId(2L);
        assertThat(virtualProductCategory1).isNotEqualTo(virtualProductCategory2);
        virtualProductCategory1.setId(null);
        assertThat(virtualProductCategory1).isNotEqualTo(virtualProductCategory2);
    }
}
