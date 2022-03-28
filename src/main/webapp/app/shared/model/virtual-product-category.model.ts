import { IVirtualProduct } from 'app/shared/model/virtual-product.model';

export interface IVirtualProductCategory {
  id?: number;
  ancestorItemIdentifier?: string | null;
  code?: string | null;
  itemIdentifier?: string | null;
  name?: string | null;
  priority?: number | null;
  virtualProduct?: IVirtualProduct;
}

export const defaultValue: Readonly<IVirtualProductCategory> = {};
