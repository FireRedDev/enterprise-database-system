import * as dayjs from 'dayjs';
import { IUser } from 'app/entities/user/user.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface ISystemuser {
  id?: number;
  entryDate?: dayjs.Dayjs | null;
  name?: string | null;
  socialSecurityNumber?: string | null;
  jobDescription?: string | null;
  user?: IUser;
  departments?: IDepartment[] | null;
}

export class Systemuser implements ISystemuser {
  constructor(
    public id?: number,
    public entryDate?: dayjs.Dayjs | null,
    public name?: string | null,
    public socialSecurityNumber?: string | null,
    public jobDescription?: string | null,
    public user?: IUser,
    public departments?: IDepartment[] | null
  ) {}
}

export function getSystemuserIdentifier(systemuser: ISystemuser): number | undefined {
  return systemuser.id;
}
