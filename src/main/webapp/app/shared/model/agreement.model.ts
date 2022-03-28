import { AgreementCancelReason } from 'app/shared/model/enumerations/agreement-cancel-reason.model';

export interface IAgreement {
  id?: number;
  cancelReason?: AgreementCancelReason | null;
}

export const defaultValue: Readonly<IAgreement> = {};
