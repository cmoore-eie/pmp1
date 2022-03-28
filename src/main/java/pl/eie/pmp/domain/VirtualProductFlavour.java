package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pl.eie.pmp.domain.enumeration.VirtualFlavourAction;

/**
 * A VirtualProductFlavour.
 */
@Entity
@Table(name = "virtual_product_flavour")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualProductFlavour implements Serializable {

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

    @Column(name = "default_flavour")
    private Boolean defaultFlavour;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "grandfathering")
    private VirtualFlavourAction grandfathering;

    @Column(name = "item_identifier")
    private String itemIdentifier;

    @Column(name = "line_code")
    private String lineCode;

    @Column(name = "name")
    private String name;

    @Column(name = "priority")
    private Integer priority;

    @Column(name = "rank")
    private Integer rank;

    @JsonIgnoreProperties(value = { "virtualProduct" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private VirtualProductCategory category;

    @OneToMany(mappedBy = "virtualProductFlavour")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "virtualProductFlavour" }, allowSetters = true)
    private Set<VirtualProductSeasoning> virtualProductSeasonings = new HashSet<>();

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

    public VirtualProductFlavour id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAncestorItemIdentifier() {
        return this.ancestorItemIdentifier;
    }

    public VirtualProductFlavour ancestorItemIdentifier(String ancestorItemIdentifier) {
        this.setAncestorItemIdentifier(ancestorItemIdentifier);
        return this;
    }

    public void setAncestorItemIdentifier(String ancestorItemIdentifier) {
        this.ancestorItemIdentifier = ancestorItemIdentifier;
    }

    public String getCode() {
        return this.code;
    }

    public VirtualProductFlavour code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCondition() {
        return this.condition;
    }

    public VirtualProductFlavour condition(String condition) {
        this.setCondition(condition);
        return this;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public Boolean getDefaultFlavour() {
        return this.defaultFlavour;
    }

    public VirtualProductFlavour defaultFlavour(Boolean defaultFlavour) {
        this.setDefaultFlavour(defaultFlavour);
        return this;
    }

    public void setDefaultFlavour(Boolean defaultFlavour) {
        this.defaultFlavour = defaultFlavour;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public VirtualProductFlavour effectiveDate(LocalDate effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public VirtualProductFlavour expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public VirtualFlavourAction getGrandfathering() {
        return this.grandfathering;
    }

    public VirtualProductFlavour grandfathering(VirtualFlavourAction grandfathering) {
        this.setGrandfathering(grandfathering);
        return this;
    }

    public void setGrandfathering(VirtualFlavourAction grandfathering) {
        this.grandfathering = grandfathering;
    }

    public String getItemIdentifier() {
        return this.itemIdentifier;
    }

    public VirtualProductFlavour itemIdentifier(String itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        return this;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public String getLineCode() {
        return this.lineCode;
    }

    public VirtualProductFlavour lineCode(String lineCode) {
        this.setLineCode(lineCode);
        return this;
    }

    public void setLineCode(String lineCode) {
        this.lineCode = lineCode;
    }

    public String getName() {
        return this.name;
    }

    public VirtualProductFlavour name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return this.priority;
    }

    public VirtualProductFlavour priority(Integer priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getRank() {
        return this.rank;
    }

    public VirtualProductFlavour rank(Integer rank) {
        this.setRank(rank);
        return this;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public VirtualProductCategory getCategory() {
        return this.category;
    }

    public void setCategory(VirtualProductCategory virtualProductCategory) {
        this.category = virtualProductCategory;
    }

    public VirtualProductFlavour category(VirtualProductCategory virtualProductCategory) {
        this.setCategory(virtualProductCategory);
        return this;
    }

    public Set<VirtualProductSeasoning> getVirtualProductSeasonings() {
        return this.virtualProductSeasonings;
    }

    public void setVirtualProductSeasonings(Set<VirtualProductSeasoning> virtualProductSeasonings) {
        if (this.virtualProductSeasonings != null) {
            this.virtualProductSeasonings.forEach(i -> i.setVirtualProductFlavour(null));
        }
        if (virtualProductSeasonings != null) {
            virtualProductSeasonings.forEach(i -> i.setVirtualProductFlavour(this));
        }
        this.virtualProductSeasonings = virtualProductSeasonings;
    }

    public VirtualProductFlavour virtualProductSeasonings(Set<VirtualProductSeasoning> virtualProductSeasonings) {
        this.setVirtualProductSeasonings(virtualProductSeasonings);
        return this;
    }

    public VirtualProductFlavour addVirtualProductSeasoning(VirtualProductSeasoning virtualProductSeasoning) {
        this.virtualProductSeasonings.add(virtualProductSeasoning);
        virtualProductSeasoning.setVirtualProductFlavour(this);
        return this;
    }

    public VirtualProductFlavour removeVirtualProductSeasoning(VirtualProductSeasoning virtualProductSeasoning) {
        this.virtualProductSeasonings.remove(virtualProductSeasoning);
        virtualProductSeasoning.setVirtualProductFlavour(null);
        return this;
    }

    public VirtualProduct getVirtualProduct() {
        return this.virtualProduct;
    }

    public void setVirtualProduct(VirtualProduct virtualProduct) {
        this.virtualProduct = virtualProduct;
    }

    public VirtualProductFlavour virtualProduct(VirtualProduct virtualProduct) {
        this.setVirtualProduct(virtualProduct);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirtualProductFlavour)) {
            return false;
        }
        return id != null && id.equals(((VirtualProductFlavour) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirtualProductFlavour{" +
            "id=" + getId() +
            ", ancestorItemIdentifier='" + getAncestorItemIdentifier() + "'" +
            ", code='" + getCode() + "'" +
            ", condition='" + getCondition() + "'" +
            ", defaultFlavour='" + getDefaultFlavour() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", grandfathering='" + getGrandfathering() + "'" +
            ", itemIdentifier='" + getItemIdentifier() + "'" +
            ", lineCode='" + getLineCode() + "'" +
            ", name='" + getName() + "'" +
            ", priority=" + getPriority() +
            ", rank=" + getRank() +
            "}";
    }
}
