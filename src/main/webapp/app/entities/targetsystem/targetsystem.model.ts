export interface ITargetsystem {
  id?: number;
  name?: string | null;
  type?: string | null;
  url?: string | null;
  password?: string | null;
  username?: string | null;
  csvAttributes?: string | null;
}

export class Targetsystem implements ITargetsystem {
  constructor(
    public id?: number,
    public name?: string | null,
    public type?: string | null,
    public url?: string | null,
    public password?: string | null,
    public username?: string | null,
    public csvAttributes?: string | null
  ) {}
}

export function getTargetsystemIdentifier(targetsystem: ITargetsystem): number | undefined {
  return targetsystem.id;
}
