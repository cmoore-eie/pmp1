package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class SchemeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scheme.class);
        Scheme scheme1 = new Scheme();
        scheme1.setId(1L);
        Scheme scheme2 = new Scheme();
        scheme2.setId(scheme1.getId());
        assertThat(scheme1).isEqualTo(scheme2);
        scheme2.setId(2L);
        assertThat(scheme1).isNotEqualTo(scheme2);
        scheme1.setId(null);
        assertThat(scheme1).isNotEqualTo(scheme2);
    }
}
