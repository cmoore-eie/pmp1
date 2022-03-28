package pl.eie.pmp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import pl.eie.pmp.domain.enumeration.ItemStatus;

/**
 * A Contract.
 */
@Entity
@Table(name = "contract")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contract implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "item_status")
    private ItemStatus itemStatus;

    @Column(name = "locked")
    private Boolean locked;

    @Column(name = "name")
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Column(name = "version_number")
    private Integer versionNumber;

    @OneToMany(mappedBy = "contract")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "contract" }, allowSetters = true)
    private Set<ContractVersion> contractVersions = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contract id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAncestorItemIdentifier() {
        return this.ancestorItemIdentifier;
    }

    public Contract ancestorItemIdentifier(String ancestorItemIdentifier) {
        this.setAncestorItemIdentifier(ancestorItemIdentifier);
        return this;
    }

    public void setAncestorItemIdentifier(String ancestorItemIdentifier) {
        this.ancestorItemIdentifier = ancestorItemIdentifier;
    }

    public String getCode() {
        return this.code;
    }

    public Contract code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getItemIdentifier() {
        return this.itemIdentifier;
    }

    public Contract itemIdentifier(String itemIdentifier) {
        this.setItemIdentifier(itemIdentifier);
        return this;
    }

    public void setItemIdentifier(String itemIdentifier) {
        this.itemIdentifier = itemIdentifier;
    }

    public ItemStatus getItemStatus() {
        return this.itemStatus;
    }

    public Contract itemStatus(ItemStatus itemStatus) {
        this.setItemStatus(itemStatus);
        return this;
    }

    public void setItemStatus(ItemStatus itemStatus) {
        this.itemStatus = itemStatus;
    }

    public Boolean getLocked() {
        return this.locked;
    }

    public Contract locked(Boolean locked) {
        this.setLocked(locked);
        return this;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getName() {
        return this.name;
    }

    public Contract name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductCode() {
        return this.productCode;
    }

    public Contract productCode(String productCode) {
        this.setProductCode(productCode);
        return this;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getVersionNumber() {
        return this.versionNumber;
    }

    public Contract versionNumber(Integer versionNumber) {
        this.setVersionNumber(versionNumber);
        return this;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Set<ContractVersion> getContractVersions() {
        return this.contractVersions;
    }

    public void setContractVersions(Set<ContractVersion> contractVersions) {
        if (this.contractVersions != null) {
            this.contractVersions.forEach(i -> i.setContract(null));
        }
        if (contractVersions != null) {
            contractVersions.forEach(i -> i.setContract(this));
        }
        this.contractVersions = contractVersions;
    }

    public Contract contractVersions(Set<ContractVersion> contractVersions) {
        this.setContractVersions(contractVersions);
        return this;
    }

    public Contract addContractVersion(ContractVersion contractVersion) {
        this.contractVersions.add(contractVersion);
        contractVersion.setContract(this);
        return this;
    }

    public Contract removeContractVersion(ContractVersion contractVersion) {
        this.contractVersions.remove(contractVersion);
        contractVersion.setContract(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contract)) {
            return false;
        }
        return id != null && id.equals(((Contract) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contract{" +
            "id=" + getId() +
            ", ancestorItemIdentifier='" + getAncestorItemIdentifier() + "'" +
            ", code='" + getCode() + "'" +
            ", itemIdentifier='" + getItemIdentifier() + "'" +
            ", itemStatus='" + getItemStatus() + "'" +
            ", locked='" + getLocked() + "'" +
            ", name='" + getName() + "'" +
            ", productCode='" + getProductCode() + "'" +
            ", versionNumber=" + getVersionNumber() +
            "}";
    }
}
