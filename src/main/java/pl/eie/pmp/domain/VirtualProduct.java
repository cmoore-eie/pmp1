package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pl.eie.pmp.domain.enumeration.VirtualProductType;

/**
 * A VirtualProduct.
 */
@Entity
@Table(name = "virtual_product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class VirtualProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "allow_affinity")
    private Boolean allowAffinity;

    @Column(name = "allow_campaign")
    private Boolean allowCampaign;

    @Column(name = "code")
    private String code;

    @Column(name = "effective_date")
    private LocalDate effectiveDate;

    @Column(name = "expiration_date")
    private LocalDate expirationDate;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "name")
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "virtual_product_type")
    private VirtualProductType virtualProductType;

    @OneToMany(mappedBy = "virtualProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "category", "virtualProductSeasonings", "virtualProduct" }, allowSetters = true)
    private Set<VirtualProductFlavour> virtualProductFlavours = new HashSet<>();

    @OneToMany(mappedBy = "virtualProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "virtualProduct" }, allowSetters = true)
    private Set<VirtualProductCategory> virtualProductCategories = new HashSet<>();

    @OneToMany(mappedBy = "virtualProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract", "virtualProduct" }, allowSetters = true)
    private Set<VirtualProductContract> virtualProductContracts = new HashSet<>();

    @OneToMany(mappedBy = "virtualProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "virtualProduct" }, allowSetters = true)
    private Set<VirtualProductLine> virtualProductLines = new HashSet<>();

    @OneToMany(mappedBy = "virtualProduct")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "virtualProduct" }, allowSetters = true)
    private Set<VirtualProductOrganization> virtualProductOrganizations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public VirtualProduct id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getAllowAffinity() {
        return this.allowAffinity;
    }

    public VirtualProduct allowAffinity(Boolean allowAffinity) {
        this.setAllowAffinity(allowAffinity);
        return this;
    }

    public void setAllowAffinity(Boolean allowAffinity) {
        this.allowAffinity = allowAffinity;
    }

    public Boolean getAllowCampaign() {
        return this.allowCampaign;
    }

    public VirtualProduct allowCampaign(Boolean allowCampaign) {
        this.setAllowCampaign(allowCampaign);
        return this;
    }

    public void setAllowCampaign(Boolean allowCampaign) {
        this.allowCampaign = allowCampaign;
    }

    public String getCode() {
        return this.code;
    }

    public VirtualProduct code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public VirtualProduct effectiveDate(LocalDate effectiveDate) {
        this.setEffectiveDate(effectiveDate);
        return this;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }

    public VirtualProduct expirationDate(LocalDate expirationDate) {
        this.setExpirationDate(expirationDate);
        return this;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getLocked() {
        return this.locked;
    }

    public VirtualProduct locked(Boolean locked) {
        this.setLocked(locked);
        return this;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getName() {
        return this.name;
    }

    public VirtualProduct name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public VirtualProduct productCode(String productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public VirtualProductType getVirtualProductType() {
        return this.virtualProductType;
    }

    public VirtualProduct virtualProductType(VirtualProductType virtualProductType) {
        this.setVirtualProductType(virtualProductType);
        return this;
    }

    public void setVirtualProductType(VirtualProductType virtualProductType) {
        this.virtualProductType = virtualProductType;
    }

    public Set<VirtualProductFlavour> getVirtualProductFlavours() {
        return this.virtualProductFlavours;
    }

    public void setVirtualProductFlavours(Set<VirtualProductFlavour> virtualProductFlavours) {
        if (this.virtualProductFlavours != null) {
            this.virtualProductFlavours.forEach(i -> i.setVirtualProduct(null));
        }
        if (virtualProductFlavours != null) {
            virtualProductFlavours.forEach(i -> i.setVirtualProduct(this));
        }
        this.virtualProductFlavours = virtualProductFlavours;
    }

    public VirtualProduct virtualProductFlavours(Set<VirtualProductFlavour> virtualProductFlavours) {
        this.setVirtualProductFlavours(virtualProductFlavours);
        return this;
    }

    public VirtualProduct addVirtualProductFlavour(VirtualProductFlavour virtualProductFlavour) {
        this.virtualProductFlavours.add(virtualProductFlavour);
        virtualProductFlavour.setVirtualProduct(this);
        return this;
    }

    public VirtualProduct removeVirtualProductFlavour(VirtualProductFlavour virtualProductFlavour) {
        this.virtualProductFlavours.remove(virtualProductFlavour);
        virtualProductFlavour.setVirtualProduct(null);
        return this;
    }

    public Set<VirtualProductCategory> getVirtualProductCategories() {
        return this.virtualProductCategories;
    }

    public void setVirtualProductCategories(Set<VirtualProductCategory> virtualProductCategories) {
        if (this.virtualProductCategories != null) {
            this.virtualProductCategories.forEach(i -> i.setVirtualProduct(null));
        }
        if (virtualProductCategories != null) {
            virtualProductCategories.forEach(i -> i.setVirtualProduct(this));
        }
        this.virtualProductCategories = virtualProductCategories;
    }

    public VirtualProduct virtualProductCategories(Set<VirtualProductCategory> virtualProductCategories) {
        this.setVirtualProductCategories(virtualProductCategories);
        return this;
    }

    public VirtualProduct addVirtualProductCategory(VirtualProductCategory virtualProductCategory) {
        this.virtualProductCategories.add(virtualProductCategory);
        virtualProductCategory.setVirtualProduct(this);
        return this;
    }

    public VirtualProduct removeVirtualProductCategory(VirtualProductCategory virtualProductCategory) {
        this.virtualProductCategories.remove(virtualProductCategory);
        virtualProductCategory.setVirtualProduct(null);
        return this;
    }

    public Set<VirtualProductContract> getVirtualProductContracts() {
        return this.virtualProductContracts;
    }

    public void setVirtualProductContracts(Set<VirtualProductContract> virtualProductContracts) {
        if (this.virtualProductContracts != null) {
            this.virtualProductContracts.forEach(i -> i.setVirtualProduct(null));
        }
        if (virtualProductContracts != null) {
            virtualProductContracts.forEach(i -> i.setVirtualProduct(this));
        }
        this.virtualProductContracts = virtualProductContracts;
    }

    public VirtualProduct virtualProductContracts(Set<VirtualProductContract> virtualProductContracts) {
        this.setVirtualProductContracts(virtualProductContracts);
        return this;
    }

    public VirtualProduct addVirtualProductContract(VirtualProductContract virtualProductContract) {
        this.virtualProductContracts.add(virtualProductContract);
        virtualProductContract.setVirtualProduct(this);
        return this;
    }

    public VirtualProduct removeVirtualProductContract(VirtualProductContract virtualProductContract) {
        this.virtualProductContracts.remove(virtualProductContract);
        virtualProductContract.setVirtualProduct(null);
        return this;
    }

    public Set<VirtualProductLine> getVirtualProductLines() {
        return this.virtualProductLines;
    }

    public void setVirtualProductLines(Set<VirtualProductLine> virtualProductLines) {
        if (this.virtualProductLines != null) {
            this.virtualProductLines.forEach(i -> i.setVirtualProduct(null));
        }
        if (virtualProductLines != null) {
            virtualProductLines.forEach(i -> i.setVirtualProduct(this));
        }
        this.virtualProductLines = virtualProductLines;
    }

    public VirtualProduct virtualProductLines(Set<VirtualProductLine> virtualProductLines) {
        this.setVirtualProductLines(virtualProductLines);
        return this;
    }

    public VirtualProduct addVirtualProductLine(VirtualProductLine virtualProductLine) {
        this.virtualProductLines.add(virtualProductLine);
        virtualProductLine.setVirtualProduct(this);
        return this;
    }

    public VirtualProduct removeVirtualProductLine(VirtualProductLine virtualProductLine) {
        this.virtualProductLines.remove(virtualProductLine);
        virtualProductLine.setVirtualProduct(null);
        return this;
    }

    public Set<VirtualProductOrganization> getVirtualProductOrganizations() {
        return this.virtualProductOrganizations;
    }

    public void setVirtualProductOrganizations(Set<VirtualProductOrganization> virtualProductOrganizations) {
        if (this.virtualProductOrganizations != null) {
            this.virtualProductOrganizations.forEach(i -> i.setVirtualProduct(null));
        }
        if (virtualProductOrganizations != null) {
            virtualProductOrganizations.forEach(i -> i.setVirtualProduct(this));
        }
        this.virtualProductOrganizations = virtualProductOrganizations;
    }

    public VirtualProduct virtualProductOrganizations(Set<VirtualProductOrganization> virtualProductOrganizations) {
        this.setVirtualProductOrganizations(virtualProductOrganizations);
        return this;
    }

    public VirtualProduct addVirtualProductOrganization(VirtualProductOrganization virtualProductOrganization) {
        this.virtualProductOrganizations.add(virtualProductOrganization);
        virtualProductOrganization.setVirtualProduct(this);
        return this;
    }

    public VirtualProduct removeVirtualProductOrganization(VirtualProductOrganization virtualProductOrganization) {
        this.virtualProductOrganizations.remove(virtualProductOrganization);
        virtualProductOrganization.setVirtualProduct(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VirtualProduct)) {
            return false;
        }
        return id != null && id.equals(((VirtualProduct) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VirtualProduct{" +
            "id=" + getId() +
            ", allowAffinity='" + getAllowAffinity() + "'" +
            ", allowCampaign='" + getAllowCampaign() + "'" +
            ", code='" + getCode() + "'" +
            ", effectiveDate='" + getEffectiveDate() + "'" +
            ", expirationDate='" + getExpirationDate() + "'" +
            ", locked='" + getLocked() + "'" +
            ", name='" + getName() + "'" +
            ", productCode='" + getProductCode() + "'" +
            ", virtualProductType='" + getVirtualProductType() + "'" +
            "}";
    }
}
