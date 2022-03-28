import { IVirtualProduct } from 'app/shared/model/virtual-product.model';

export interface IVirtualProductOrganization {
  id?: number;
  ancestorItemIdentifier?: string | null;
  itemIdentifier?: string | null;
  organization?: string | null;
  producerCode?: string | null;
  virtualProduct?: IVirtualProduct;
}

export const defaultValue: Readonly<IVirtualProductOrganization> = {};
