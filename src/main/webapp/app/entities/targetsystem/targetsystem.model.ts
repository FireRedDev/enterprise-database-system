export interface ITargetsystem {
  id?: number;
  name?: string | null;
}

export class Targetsystem implements ITargetsystem {
  constructor(public id?: number, public name?: string | null) {}
}

export function getTargetsystemIdentifier(targetsystem: ITargetsystem): number | undefined {
  return targetsystem.id;
}
