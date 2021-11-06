import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';

export interface ISystemuser {
  id?: number;
  entryDate?: dayjs.Dayjs | null;
  user?: IUser;
}

export class Systemuser implements ISystemuser {
  constructor(public id?: number, public entryDate?: dayjs.Dayjs | null, public user?: IUser) {}
}

export function getSystemuserIdentifier(systemuser: ISystemuser): number | undefined {
  return systemuser.id;
}
