import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { CURRENCY } from 'app/shared/model/enumerations/currency.model';
import { LoanStatus } from 'app/shared/model/enumerations/loan-status.model';

export interface ILoan {
  id?: number;
  hash?: string;
  loanAmt?: number;
  availableAmt?: number;
  interestRate?: number;
  terms?: number;
  createAt?: string | null;
  updateAt?: string | null;
  currency?: CURRENCY | null;
  status?: LoanStatus | null;
  owner?: IUser | null;
}

export const defaultValue: Readonly<ILoan> = {};
