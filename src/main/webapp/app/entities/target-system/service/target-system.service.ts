import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITargetSystem, getTargetSystemIdentifier } from '../target-system.model';

export type EntityResponseType = HttpResponse<ITargetSystem>;
export type EntityArrayResponseType = HttpResponse<ITargetSystem[]>;

@Injectable({ providedIn: 'root' })
export class TargetSystemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/target-systems');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(targetSystem: ITargetSystem): Observable<EntityResponseType> {
    return this.http.post<ITargetSystem>(this.resourceUrl, targetSystem, { observe: 'response' });
  }

  update(targetSystem: ITargetSystem): Observable<EntityResponseType> {
    return this.http.put<ITargetSystem>(`${this.resourceUrl}/${getTargetSystemIdentifier(targetSystem) as number}`, targetSystem, {
      observe: 'response',
    });
  }

  partialUpdate(targetSystem: ITargetSystem): Observable<EntityResponseType> {
    return this.http.patch<ITargetSystem>(`${this.resourceUrl}/${getTargetSystemIdentifier(targetSystem) as number}`, targetSystem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITargetSystem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITargetSystem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTargetSystemToCollectionIfMissing(
    targetSystemCollection: ITargetSystem[],
    ...targetSystemsToCheck: (ITargetSystem | null | undefined)[]
  ): ITargetSystem[] {
    const targetSystems: ITargetSystem[] = targetSystemsToCheck.filter(isPresent);
    if (targetSystems.length > 0) {
      const targetSystemCollectionIdentifiers = targetSystemCollection.map(
        targetSystemItem => getTargetSystemIdentifier(targetSystemItem)!
      );
      const targetSystemsToAdd = targetSystems.filter(targetSystemItem => {
        const targetSystemIdentifier = getTargetSystemIdentifier(targetSystemItem);
        if (targetSystemIdentifier == null || targetSystemCollectionIdentifiers.includes(targetSystemIdentifier)) {
          return false;
        }
        targetSystemCollectionIdentifiers.push(targetSystemIdentifier);
        return true;
      });
      return [...targetSystemsToAdd, ...targetSystemCollection];
    }
    return targetSystemCollection;
  }
}
