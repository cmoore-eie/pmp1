package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VirtualProductCategory.
 */
@Entity
@Table(name = "virtual_product_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ancestor_item_identifier")
    private String ancestorItemIdentifier;

    @Column(name = "code")
    private String code;

    @Column(name = "item_identifier")
    private String itemIdentifier;

    @Column(name = "name")
    private String name;

    @Column(name = "priority")
    private Integer priority;

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

    public VirtualProductCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAncestorItemIdentifier() {
        return this.ancestorItemIdentifier;
    }

    public VirtualProductCategory ancestorItemIdentifier(String ancestorItemIdentifier) {
        this.setAncestorItemIdentifier(ancestorItemIdentifier);
        return this;
    }

    public void setAncestorItemIdentifier(String ancestorItemIdentifier) {
        this.ancestorItemIdentifier = ancestorItemIdentifier;
    }

    public String getCode() {
        return this.code;
    }

    public VirtualProductCategory code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemIdentifier() {
        return this.itemIdentifier;
    }

    public VirtualProductCategory itemIdentifier(String itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        return this;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getName() {
        return this.name;
    }

    public VirtualProductCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public VirtualProductCategory priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public VirtualProduct getVirtualProduct() {
        return this.virtualProduct;
    }

    public void setVirtualProduct(VirtualProduct virtualProduct) {
        this.virtualProduct = virtualProduct;
    }

    public VirtualProductCategory virtualProduct(VirtualProduct virtualProduct) {
        this.setVirtualProduct(virtualProduct);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirtualProductCategory)) {
            return false;
        }
        return id != null && id.equals(((VirtualProductCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirtualProductCategory{" +
            "id=" + getId() +
            ", ancestorItemIdentifier='" + getAncestorItemIdentifier() + "'" +
            ", code='" + getCode() + "'" +
            ", itemIdentifier='" + getItemIdentifier() + "'" +
            ", name='" + getName() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
