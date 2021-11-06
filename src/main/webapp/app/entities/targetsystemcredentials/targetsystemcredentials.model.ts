import { ISystemuser } from 'app/entities/systemuser/systemuser.model';
import { ITargetsystem } from 'app/entities/targetsystem/targetsystem.model';

export interface ITargetsystemcredentials {
  id?: number;
  username?: string | null;
  password?: string;
  systemuser?: ISystemuser | null;
  targetsystem?: ITargetsystem | null;
}

export class Targetsystemcredentials implements ITargetsystemcredentials {
  constructor(
    public id?: number,
    public username?: string | null,
    public password?: string,
    public systemuser?: ISystemuser | null,
    public targetsystem?: ITargetsystem | null
  ) {}
}

export function getTargetsystemcredentialsIdentifier(targetsystemcredentials: ITargetsystemcredentials): number | undefined {
  return targetsystemcredentials.id;
}
