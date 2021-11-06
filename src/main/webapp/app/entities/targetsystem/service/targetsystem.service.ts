import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITargetsystem, getTargetsystemIdentifier } from '../targetsystem.model';

export type EntityResponseType = HttpResponse<ITargetsystem>;
export type EntityArrayResponseType = HttpResponse<ITargetsystem[]>;

@Injectable({ providedIn: 'root' })
export class TargetsystemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/targetsystems');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(targetsystem: ITargetsystem): Observable<EntityResponseType> {
    return this.http.post<ITargetsystem>(this.resourceUrl, targetsystem, { observe: 'response' });
  }

  update(targetsystem: ITargetsystem): Observable<EntityResponseType> {
    return this.http.put<ITargetsystem>(`${this.resourceUrl}/${getTargetsystemIdentifier(targetsystem) as number}`, targetsystem, {
      observe: 'response',
    });
  }

  partialUpdate(targetsystem: ITargetsystem): Observable<EntityResponseType> {
    return this.http.patch<ITargetsystem>(`${this.resourceUrl}/${getTargetsystemIdentifier(targetsystem) as number}`, targetsystem, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITargetsystem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITargetsystem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTargetsystemToCollectionIfMissing(
    targetsystemCollection: ITargetsystem[],
    ...targetsystemsToCheck: (ITargetsystem | null | undefined)[]
  ): ITargetsystem[] {
    const targetsystems: ITargetsystem[] = targetsystemsToCheck.filter(isPresent);
    if (targetsystems.length > 0) {
      const targetsystemCollectionIdentifiers = targetsystemCollection.map(
        targetsystemItem => getTargetsystemIdentifier(targetsystemItem)!
      );
      const targetsystemsToAdd = targetsystems.filter(targetsystemItem => {
        const targetsystemIdentifier = getTargetsystemIdentifier(targetsystemItem);
        if (targetsystemIdentifier == null || targetsystemCollectionIdentifiers.includes(targetsystemIdentifier)) {
          return false;
        }
        targetsystemCollectionIdentifiers.push(targetsystemIdentifier);
        return true;
      });
      return [...targetsystemsToAdd, ...targetsystemCollection];
    }
    return targetsystemCollection;
  }
}
