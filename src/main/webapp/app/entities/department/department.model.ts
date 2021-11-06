import { ITargetsystem } from 'app/entities/targetsystem/targetsystem.model';

export interface IDepartment {
  id?: number;
  name?: string;
  targetsystems?: ITargetsystem[] | null;
}

export class Department implements IDepartment {
  constructor(public id?: number, public name?: string, public targetsystems?: ITargetsystem[] | null) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}
