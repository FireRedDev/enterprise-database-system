import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITargetSystemCredentials, getTargetSystemCredentialsIdentifier } from '../target-system-credentials.model';

export type EntityResponseType = HttpResponse<ITargetSystemCredentials>;
export type EntityArrayResponseType = HttpResponse<ITargetSystemCredentials[]>;

@Injectable({ providedIn: 'root' })
export class TargetSystemCredentialsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/target-system-credentials');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(targetSystemCredentials: ITargetSystemCredentials): Observable<EntityResponseType> {
    return this.http.post<ITargetSystemCredentials>(this.resourceUrl, targetSystemCredentials, { observe: 'response' });
  }

  update(targetSystemCredentials: ITargetSystemCredentials): Observable<EntityResponseType> {
    return this.http.put<ITargetSystemCredentials>(
      `${this.resourceUrl}/${getTargetSystemCredentialsIdentifier(targetSystemCredentials) as number}`,
      targetSystemCredentials,
      { observe: 'response' }
    );
  }

  partialUpdate(targetSystemCredentials: ITargetSystemCredentials): Observable<EntityResponseType> {
    return this.http.patch<ITargetSystemCredentials>(
      `${this.resourceUrl}/${getTargetSystemCredentialsIdentifier(targetSystemCredentials) as number}`,
      targetSystemCredentials,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITargetSystemCredentials>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITargetSystemCredentials[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTargetSystemCredentialsToCollectionIfMissing(
    targetSystemCredentialsCollection: ITargetSystemCredentials[],
    ...targetSystemCredentialsToCheck: (ITargetSystemCredentials | null | undefined)[]
  ): ITargetSystemCredentials[] {
    const targetSystemCredentials: ITargetSystemCredentials[] = targetSystemCredentialsToCheck.filter(isPresent);
    if (targetSystemCredentials.length > 0) {
      const targetSystemCredentialsCollectionIdentifiers = targetSystemCredentialsCollection.map(
        targetSystemCredentialsItem => getTargetSystemCredentialsIdentifier(targetSystemCredentialsItem)!
      );
      const targetSystemCredentialsToAdd = targetSystemCredentials.filter(targetSystemCredentialsItem => {
        const targetSystemCredentialsIdentifier = getTargetSystemCredentialsIdentifier(targetSystemCredentialsItem);
        if (
          targetSystemCredentialsIdentifier == null ||
          targetSystemCredentialsCollectionIdentifiers.includes(targetSystemCredentialsIdentifier)
        ) {
          return false;
        }
        targetSystemCredentialsCollectionIdentifiers.push(targetSystemCredentialsIdentifier);
        return true;
      });
      return [...targetSystemCredentialsToAdd, ...targetSystemCredentialsCollection];
    }
    return targetSystemCredentialsCollection;
  }
}
