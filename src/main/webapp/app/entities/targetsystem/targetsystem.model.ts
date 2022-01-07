export enum TargetSystemTypes {
  CSV,
  LDAV,
  Database,
}

export interface ITargetsystem {
  id?: number;
  name?: string | null;
  type?: TargetSystemTypes;
  dbUrl?: string | null;
  dbuser?: string | null;
  dbpassword?: string | null;
  csvAttributes?: string[] | null;
}

export class Targetsystem implements ITargetsystem {
  constructor(
    public id?: number,
    public name?: string | null,
    public type?: TargetSystemTypes,
    dbUrl?: string | null,
    dbuser?: string | null,
    dbpassword?: string | null,
    csvAttributes?: boolean[] | null
  ) {}
}

export function getTargetsystemIdentifier(targetsystem: ITargetsystem): number | undefined {
  return targetsystem.id;
}

export function getDbUrl(targetsystem: ITargetsystem): string | null | undefined {
  return targetsystem.dbUrl;
}

export function getDbUser(targetsystem: ITargetsystem): string | null | undefined {
  return targetsystem.dbuser;
}

export function getDbPassword(targetsystem: ITargetsystem): string | null | undefined {
  return targetsystem.dbpassword;
}

export function getCsvAttributes(targetsystem: ITargetsystem): string[] | null | undefined {
  return targetsystem.csvAttributes;
}

export function getType(targetsystem: ITargetsystem): TargetSystemTypes | undefined {
  return targetsystem.type;
}
