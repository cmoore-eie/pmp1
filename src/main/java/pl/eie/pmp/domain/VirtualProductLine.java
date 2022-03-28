package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VirtualProductLine.
 */
@Entity
@Table(name = "virtual_product_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualProductLine implements Serializable {

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

    @Column(name = "line_available")
    private Boolean lineAvailable;

    @Column(name = "line_code")
    private String lineCode;

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

    public VirtualProductLine id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAncestorItemIdentifier() {
        return this.ancestorItemIdentifier;
    }

    public VirtualProductLine ancestorItemIdentifier(String ancestorItemIdentifier) {
        this.setAncestorItemIdentifier(ancestorItemIdentifier);
        return this;
    }

    public void setAncestorItemIdentifier(String ancestorItemIdentifier) {
        this.ancestorItemIdentifier = ancestorItemIdentifier;
    }

    public String getItemIdentifier() {
        return this.itemIdentifier;
    }

    public VirtualProductLine itemIdentifier(String itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        return this;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public Boolean getLineAvailable() {
        return this.lineAvailable;
    }

    public VirtualProductLine lineAvailable(Boolean lineAvailable) {
        this.setLineAvailable(lineAvailable);
        return this;
    }

    public void setLineAvailable(Boolean lineAvailable) {
        this.lineAvailable = lineAvailable;
    }

    public String getLineCode() {
        return this.lineCode;
    }

    public VirtualProductLine lineCode(String lineCode) {
        this.setLineCode(lineCode);
        return this;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public VirtualProduct getVirtualProduct() {
        return this.virtualProduct;
    }

    public void setVirtualProduct(VirtualProduct virtualProduct) {
        this.virtualProduct = virtualProduct;
    }

    public VirtualProductLine virtualProduct(VirtualProduct virtualProduct) {
        this.setVirtualProduct(virtualProduct);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirtualProductLine)) {
            return false;
        }
        return id != null && id.equals(((VirtualProductLine) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirtualProductLine{" +
            "id=" + getId() +
            ", ancestorItemIdentifier='" + getAncestorItemIdentifier() + "'" +
            ", itemIdentifier='" + getItemIdentifier() + "'" +
            ", lineAvailable='" + getLineAvailable() + "'" +
            ", lineCode='" + getLineCode() + "'" +
            "}";
    }
}
