import { IContract } from 'app/shared/model/contract.model';
import { IVirtualProduct } from 'app/shared/model/virtual-product.model';

export interface IVirtualProductContract {
  id?: number;
  ancestorItemIdentifier?: string | null;
  itemIdentifier?: string | null;
  priority?: number | null;
  contract?: IContract | null;
  virtualProduct?: IVirtualProduct;
}

export const defaultValue: Readonly<IVirtualProductContract> = {};
