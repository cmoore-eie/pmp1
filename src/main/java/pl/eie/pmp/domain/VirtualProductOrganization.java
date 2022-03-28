package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VirtualProductOrganization.
 */
@Entity
@Table(name = "virtual_product_organization")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualProductOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ancestor_item_identifier")
    private String ancestorItemIdentifier;

    @Column(name = "item_identifier")
    private String itemIdentifier;

    @Column(name = "organization")
    private String organization;

    @Column(name = "producer_code")
    private String producerCode;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "virtualProductFlavours",
            "virtualProductCategories",
            "virtualProductContracts",
            "virtualProductLines",
            "virtualProductOrganizations",
        },
        allowSetters = true
    )
    private VirtualProduct virtualProduct;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VirtualProductOrganization id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAncestorItemIdentifier() {
        return this.ancestorItemIdentifier;
    }

    public VirtualProductOrganization ancestorItemIdentifier(String ancestorItemIdentifier) {
        this.setAncestorItemIdentifier(ancestorItemIdentifier);
        return this;
    }

    public void setAncestorItemIdentifier(String ancestorItemIdentifier) {
        this.ancestorItemIdentifier = ancestorItemIdentifier;
    }

    public String getItemIdentifier() {
        return this.itemIdentifier;
    }

    public VirtualProductOrganization itemIdentifier(String itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        return this;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getOrganization() {
        return this.organization;
    }

    public VirtualProductOrganization organization(String organization) {
        this.setOrganization(organization);
        return this;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getProducerCode() {
        return this.producerCode;
    }

    public VirtualProductOrganization producerCode(String producerCode) {
        this.setProducerCode(producerCode);
        return this;
    }

    public void setProducerCode(String producerCode) {
        this.producerCode = producerCode;
    }

    public VirtualProduct getVirtualProduct() {
        return this.virtualProduct;
    }

    public void setVirtualProduct(VirtualProduct virtualProduct) {
        this.virtualProduct = virtualProduct;
    }

    public VirtualProductOrganization virtualProduct(VirtualProduct virtualProduct) {
        this.setVirtualProduct(virtualProduct);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirtualProductOrganization)) {
            return false;
        }
        return id != null && id.equals(((VirtualProductOrganization) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirtualProductOrganization{" +
            "id=" + getId() +
            ", ancestorItemIdentifier='" + getAncestorItemIdentifier() + "'" +
            ", itemIdentifier='" + getItemIdentifier() + "'" +
            ", organization='" + getOrganization() + "'" +
            ", producerCode='" + getProducerCode() + "'" +
            "}";
    }
}
