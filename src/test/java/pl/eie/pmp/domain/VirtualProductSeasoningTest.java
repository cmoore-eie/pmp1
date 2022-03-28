package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class VirtualProductSeasoningTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VirtualProductSeasoning.class);
        VirtualProductSeasoning virtualProductSeasoning1 = new VirtualProductSeasoning();
        virtualProductSeasoning1.setId(1L);
        VirtualProductSeasoning virtualProductSeasoning2 = new VirtualProductSeasoning();
        virtualProductSeasoning2.setId(virtualProductSeasoning1.getId());
        assertThat(virtualProductSeasoning1).isEqualTo(virtualProductSeasoning2);
        virtualProductSeasoning2.setId(2L);
        assertThat(virtualProductSeasoning1).isNotEqualTo(virtualProductSeasoning2);
        virtualProductSeasoning1.setId(null);
        assertThat(virtualProductSeasoning1).isNotEqualTo(virtualProductSeasoning2);
    }
}
