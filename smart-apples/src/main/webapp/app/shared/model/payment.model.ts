import dayjs from 'dayjs';
import { ILoanSubscription } from 'app/shared/model/loan-subscription.model';

export interface IPayment {
  id?: number;
  hash?: string;
  interest?: number;
  principal?: number;
  createAt?: string | null;
  updateAt?: string | null;
  subscription?: ILoanSubscription | null;
}

export const defaultValue: Readonly<IPayment> = {};
