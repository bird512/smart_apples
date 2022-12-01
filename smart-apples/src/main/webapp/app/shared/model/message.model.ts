import { MessageLevel } from 'app/shared/model/enumerations/message-level.model';

export interface IMessage {
  id?: number;
  level?: MessageLevel;
  msg?: string | null;
}

export const defaultValue: Readonly<IMessage> = {};
