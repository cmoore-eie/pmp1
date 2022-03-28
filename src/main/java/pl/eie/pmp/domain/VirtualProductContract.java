package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VirtualProductContract.
 */
@Entity
@Table(name = "virtual_product_contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualProductContract implements Serializable {

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

    @Column(name = "priority")
    private Integer priority;

    @JsonIgnoreProperties(value = { "contractVersions" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Contract contract;

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

    public VirtualProductContract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAncestorItemIdentifier() {
        return this.ancestorItemIdentifier;
    }

    public VirtualProductContract ancestorItemIdentifier(String ancestorItemIdentifier) {
        this.setAncestorItemIdentifier(ancestorItemIdentifier);
        return this;
    }

    public void setAncestorItemIdentifier(String ancestorItemIdentifier) {
        this.ancestorItemIdentifier = ancestorItemIdentifier;
    }

    public String getItemIdentifier() {
        return this.itemIdentifier;
    }

    public VirtualProductContract itemIdentifier(String itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        return this;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public VirtualProductContract priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Contract getContract() {
        return this.contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public VirtualProductContract contract(Contract contract) {
        this.setContract(contract);
        return this;
    }

    public VirtualProduct getVirtualProduct() {
        return this.virtualProduct;
    }

    public void setVirtualProduct(VirtualProduct virtualProduct) {
        this.virtualProduct = virtualProduct;
    }

    public VirtualProductContract virtualProduct(VirtualProduct virtualProduct) {
        this.setVirtualProduct(virtualProduct);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirtualProductContract)) {
            return false;
        }
        return id != null && id.equals(((VirtualProductContract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirtualProductContract{" +
            "id=" + getId() +
            ", ancestorItemIdentifier='" + getAncestorItemIdentifier() + "'" +
            ", itemIdentifier='" + getItemIdentifier() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
