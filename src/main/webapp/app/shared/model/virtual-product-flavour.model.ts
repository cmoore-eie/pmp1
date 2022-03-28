import dayjs from 'dayjs';
import { IVirtualProductCategory } from 'app/shared/model/virtual-product-category.model';
import { IVirtualProductSeasoning } from 'app/shared/model/virtual-product-seasoning.model';
import { IVirtualProduct } from 'app/shared/model/virtual-product.model';
import { VirtualFlavourAction } from 'app/shared/model/enumerations/virtual-flavour-action.model';

export interface IVirtualProductFlavour {
  id?: number;
  ancestorItemIdentifier?: string | null;
  code?: string | null;
  condition?: string | null;
  defaultFlavour?: boolean | null;
  effectiveDate?: string | null;
  expirationDate?: string | null;
  grandfathering?: VirtualFlavourAction | null;
  itemIdentifier?: string | null;
  lineCode?: string | null;
  name?: string | null;
  priority?: number | null;
  rank?: number | null;
  category?: IVirtualProductCategory | null;
  virtualProductSeasonings?: IVirtualProductSeasoning[] | null;
  virtualProduct?: IVirtualProduct;
}

export const defaultValue: Readonly<IVirtualProductFlavour> = {
  defaultFlavour: false,
};
