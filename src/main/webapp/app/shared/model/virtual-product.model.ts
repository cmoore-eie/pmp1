import dayjs from 'dayjs';
import { IVirtualProductFlavour } from 'app/shared/model/virtual-product-flavour.model';
import { IVirtualProductCategory } from 'app/shared/model/virtual-product-category.model';
import { IVirtualProductContract } from 'app/shared/model/virtual-product-contract.model';
import { IVirtualProductLine } from 'app/shared/model/virtual-product-line.model';
import { IVirtualProductOrganization } from 'app/shared/model/virtual-product-organization.model';
import { VirtualProductType } from 'app/shared/model/enumerations/virtual-product-type.model';

export interface IVirtualProduct {
  id?: number;
  allowAffinity?: boolean | null;
  allowCampaign?: boolean | null;
  code?: string | null;
  effectiveDate?: string | null;
  expirationDate?: string | null;
  locked?: boolean | null;
  name?: string | null;
  productCode?: string | null;
  virtualProductType?: VirtualProductType | null;
  virtualProductFlavours?: IVirtualProductFlavour[] | null;
  virtualProductCategories?: IVirtualProductCategory[] | null;
  virtualProductContracts?: IVirtualProductContract[] | null;
  virtualProductLines?: IVirtualProductLine[] | null;
  virtualProductOrganizations?: IVirtualProductOrganization[] | null;
}

export const defaultValue: Readonly<IVirtualProduct> = {
  allowAffinity: false,
  allowCampaign: false,
  locked: false,
};
