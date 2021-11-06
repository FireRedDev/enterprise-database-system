import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITargetsystemcredentials, getTargetsystemcredentialsIdentifier } from '../targetsystemcredentials.model';

export type EntityResponseType = HttpResponse<ITargetsystemcredentials>;
export type EntityArrayResponseType = HttpResponse<ITargetsystemcredentials[]>;

@Injectable({ providedIn: 'root' })
export class TargetsystemcredentialsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/targetsystemcredentials');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(targetsystemcredentials: ITargetsystemcredentials): Observable<EntityResponseType> {
    return this.http.post<ITargetsystemcredentials>(this.resourceUrl, targetsystemcredentials, { observe: 'response' });
  }

  update(targetsystemcredentials: ITargetsystemcredentials): Observable<EntityResponseType> {
    return this.http.put<ITargetsystemcredentials>(
      `${this.resourceUrl}/${getTargetsystemcredentialsIdentifier(targetsystemcredentials) as number}`,
      targetsystemcredentials,
      { observe: 'response' }
    );
  }

  partialUpdate(targetsystemcredentials: ITargetsystemcredentials): Observable<EntityResponseType> {
    return this.http.patch<ITargetsystemcredentials>(
      `${this.resourceUrl}/${getTargetsystemcredentialsIdentifier(targetsystemcredentials) as number}`,
      targetsystemcredentials,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITargetsystemcredentials>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITargetsystemcredentials[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTargetsystemcredentialsToCollectionIfMissing(
    targetsystemcredentialsCollection: ITargetsystemcredentials[],
    ...targetsystemcredentialsToCheck: (ITargetsystemcredentials | null | undefined)[]
  ): ITargetsystemcredentials[] {
    const targetsystemcredentials: ITargetsystemcredentials[] = targetsystemcredentialsToCheck.filter(isPresent);
    if (targetsystemcredentials.length > 0) {
      const targetsystemcredentialsCollectionIdentifiers = targetsystemcredentialsCollection.map(
        targetsystemcredentialsItem => getTargetsystemcredentialsIdentifier(targetsystemcredentialsItem)!
      );
      const targetsystemcredentialsToAdd = targetsystemcredentials.filter(targetsystemcredentialsItem => {
        const targetsystemcredentialsIdentifier = getTargetsystemcredentialsIdentifier(targetsystemcredentialsItem);
        if (
          targetsystemcredentialsIdentifier == null ||
          targetsystemcredentialsCollectionIdentifiers.includes(targetsystemcredentialsIdentifier)
        ) {
          return false;
        }
        targetsystemcredentialsCollectionIdentifiers.push(targetsystemcredentialsIdentifier);
        return true;
      });
      return [...targetsystemcredentialsToAdd, ...targetsystemcredentialsCollection];
    }
    return targetsystemcredentialsCollection;
  }
}
