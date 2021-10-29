import * as dayjs from 'dayjs';
import { IDepartment } from 'app/entities/department/department.model';

export interface IEmployee {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  password?: string | null;
  birthDate?: dayjs.Dayjs | null;
  entryDate?: dayjs.Dayjs | null;
  isEmployed?: boolean | null;
  email?: string | null;
  departments?: IDepartment[] | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public password?: string | null,
    public birthDate?: dayjs.Dayjs | null,
    public entryDate?: dayjs.Dayjs | null,
    public isEmployed?: boolean | null,
    public email?: string | null,
    public departments?: IDepartment[] | null
  ) {
    this.isEmployed = this.isEmployed ?? false;
  }
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
