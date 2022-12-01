import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ILoan } from 'app/shared/model/loan.model';

export interface ILoanSubscription {
  id?: number;
  hash?: string;
  subAmt?: number;
  createAt?: string | null;
  updateAt?: string | null;
  subscriber?: IUser | null;
  loan?: ILoan | null;
}

export const defaultValue: Readonly<ILoanSubscription> = {};
