import { IVirtualProductFlavour } from 'app/shared/model/virtual-product-flavour.model';

export interface IVirtualProductSeasoning {
  id?: number;
  ancestorItemIdentifier?: string | null;
  code?: string | null;
  condition?: string | null;
  defaultSeasoning?: boolean | null;
  itemIdentifier?: string | null;
  name?: string | null;
  priority?: number | null;
  virtualProductFlavour?: IVirtualProductFlavour;
}

export const defaultValue: Readonly<IVirtualProductSeasoning> = {
  defaultSeasoning: false,
};
