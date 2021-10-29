export interface ITargetSystem {
  id?: number;
  name?: string | null;
}

export class TargetSystem implements ITargetSystem {
  constructor(public id?: number, public name?: string | null) {}
}

export function getTargetSystemIdentifier(targetSystem: ITargetSystem): number | undefined {
  return targetSystem.id;
}
