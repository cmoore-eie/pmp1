import dayjs from 'dayjs';
import { IContract } from 'app/shared/model/contract.model';

export interface IContractVersion {
  id?: number;
  effectiveDate?: string | null;
  expirationDate?: string | null;
  hiddenContract?: boolean | null;
  versionNumber?: number | null;
  contract?: IContract;
}

export const defaultValue: Readonly<IContractVersion> = {
  hiddenContract: false,
};
