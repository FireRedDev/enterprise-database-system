import { ITargetSystem } from 'app/entities/target-system/target-system.model';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface ITargetSystemCredentials {
  id?: number;
  username?: string;
  password?: string;
  targetSystem?: ITargetSystem | null;
  employee?: IEmployee | null;
  department?: IDepartment | null;
}

export class TargetSystemCredentials implements ITargetSystemCredentials {
  constructor(
    public id?: number,
    public username?: string,
    public password?: string,
    public targetSystem?: ITargetSystem | null,
    public employee?: IEmployee | null,
    public department?: IDepartment | null
  ) {}
}

export function getTargetSystemCredentialsIdentifier(targetSystemCredentials: ITargetSystemCredentials): number | undefined {
  return targetSystemCredentials.id;
}
