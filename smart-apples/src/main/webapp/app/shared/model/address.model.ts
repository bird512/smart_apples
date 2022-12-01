import { IUser } from 'app/shared/model/user.model';

export interface IAddress {
  id?: number;
  address?: string;
  privatekey?: string;
  user?: IUser | null;
}

export const defaultValue: Readonly<IAddress> = {};
