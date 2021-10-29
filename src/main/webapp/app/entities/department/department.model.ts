import { ITargetSystem } from 'app/entities/target-system/target-system.model';

export interface IDepartment {
  id?: number;
  name?: string | null;
  location?: string | null;
  description?: string | null;
  targetSystems?: ITargetSystem[] | null;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public name?: string | null,
    public location?: string | null,
    public description?: string | null,
    public targetSystems?: ITargetSystem[] | null
  ) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}
