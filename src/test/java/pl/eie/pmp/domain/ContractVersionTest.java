package pl.eie.pmp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import pl.eie.pmp.web.rest.TestUtil;

class ContractVersionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContractVersion.class);
        ContractVersion contractVersion1 = new ContractVersion();
        contractVersion1.setId(1L);
        ContractVersion contractVersion2 = new ContractVersion();
        contractVersion2.setId(contractVersion1.getId());
        assertThat(contractVersion1).isEqualTo(contractVersion2);
        contractVersion2.setId(2L);
        assertThat(contractVersion1).isNotEqualTo(contractVersion2);
        contractVersion1.setId(null);
        assertThat(contractVersion1).isNotEqualTo(contractVersion2);
    }
}
