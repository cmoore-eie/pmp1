import { IVirtualProduct } from 'app/shared/model/virtual-product.model';

export interface IVirtualProductLine {
  id?: number;
  ancestorItemIdentifier?: string | null;
  itemIdentifier?: string | null;
  lineAvailable?: boolean | null;
  lineCode?: string | null;
  virtualProduct?: IVirtualProduct;
}

export const defaultValue: Readonly<IVirtualProductLine> = {
  lineAvailable: false,
};
