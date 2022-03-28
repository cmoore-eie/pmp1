package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A VirtualProductSeasoning.
 */
@Entity
@Table(name = "virtual_product_seasoning")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualProductSeasoning implements Serializable {

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

    @Column(name = "condition")
    private String condition;

    @Column(name = "default_seasoning")
    private Boolean defaultSeasoning;

    @Column(name = "item_identifier")
    private String itemIdentifier;

    @Column(name = "name")
    private String name;

    @Column(name = "priority")
    private Integer priority;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "category", "virtualProductSeasonings", "virtualProduct" }, allowSetters = true)
    private VirtualProductFlavour virtualProductFlavour;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VirtualProductSeasoning id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAncestorItemIdentifier() {
        return this.ancestorItemIdentifier;
    }

    public VirtualProductSeasoning ancestorItemIdentifier(String ancestorItemIdentifier) {
        this.setAncestorItemIdentifier(ancestorItemIdentifier);
        return this;
    }

    public void setAncestorItemIdentifier(String ancestorItemIdentifier) {
        this.ancestorItemIdentifier = ancestorItemIdentifier;
    }

    public String getCode() {
        return this.code;
    }

    public VirtualProductSeasoning code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCondition() {
        return this.condition;
    }

    public VirtualProductSeasoning condition(String condition) {
        this.setCondition(condition);
        return this;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getDefaultSeasoning() {
        return this.defaultSeasoning;
    }

    public VirtualProductSeasoning defaultSeasoning(Boolean defaultSeasoning) {
        this.setDefaultSeasoning(defaultSeasoning);
        return this;
    }

    public void setDefaultSeasoning(Boolean defaultSeasoning) {
        this.defaultSeasoning = defaultSeasoning;
    }

    public String getItemIdentifier() {
        return this.itemIdentifier;
    }

    public VirtualProductSeasoning itemIdentifier(String itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        return this;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getName() {
        return this.name;
    }

    public VirtualProductSeasoning name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public VirtualProductSeasoning priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public VirtualProductFlavour getVirtualProductFlavour() {
        return this.virtualProductFlavour;
    }

    public void setVirtualProductFlavour(VirtualProductFlavour virtualProductFlavour) {
        this.virtualProductFlavour = virtualProductFlavour;
    }

    public VirtualProductSeasoning virtualProductFlavour(VirtualProductFlavour virtualProductFlavour) {
        this.setVirtualProductFlavour(virtualProductFlavour);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirtualProductSeasoning)) {
            return false;
        }
        return id != null && id.equals(((VirtualProductSeasoning) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirtualProductSeasoning{" +
            "id=" + getId() +
            ", ancestorItemIdentifier='" + getAncestorItemIdentifier() + "'" +
            ", code='" + getCode() + "'" +
            ", condition='" + getCondition() + "'" +
            ", defaultSeasoning='" + getDefaultSeasoning() + "'" +
            ", itemIdentifier='" + getItemIdentifier() + "'" +
            ", name='" + getName() + "'" +
            ", priority=" + getPriority() +
            "}";
    }
}
