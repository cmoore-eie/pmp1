import { IContractVersion } from 'app/shared/model/contract-version.model';
import { ItemStatus } from 'app/shared/model/enumerations/item-status.model';

export interface IContract {
  id?: number;
  ancestorItemIdentifier?: string | null;
  code?: string | null;
  itemIdentifier?: string | null;
  itemStatus?: ItemStatus | null;
  locked?: boolean | null;
  name?: string | null;
  productCode?: string | null;
  versionNumber?: number | null;
  contractVersions?: IContractVersion[] | null;
}

export const defaultValue: Readonly<IContract> = {
  locked: false,
};
