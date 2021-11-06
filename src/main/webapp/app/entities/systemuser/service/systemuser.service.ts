import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISystemuser, getSystemuserIdentifier } from '../systemuser.model';

export type EntityResponseType = HttpResponse<ISystemuser>;
export type EntityArrayResponseType = HttpResponse<ISystemuser[]>;

@Injectable({ providedIn: 'root' })
export class SystemuserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/systemusers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(systemuser: ISystemuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemuser);
    return this.http
      .post<ISystemuser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(systemuser: ISystemuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemuser);
    return this.http
      .put<ISystemuser>(`${this.resourceUrl}/${getSystemuserIdentifier(systemuser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(systemuser: ISystemuser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(systemuser);
    return this.http
      .patch<ISystemuser>(`${this.resourceUrl}/${getSystemuserIdentifier(systemuser) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISystemuser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISystemuser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSystemuserToCollectionIfMissing(
    systemuserCollection: ISystemuser[],
    ...systemusersToCheck: (ISystemuser | null | undefined)[]
  ): ISystemuser[] {
    const systemusers: ISystemuser[] = systemusersToCheck.filter(isPresent);
    if (systemusers.length > 0) {
      const systemuserCollectionIdentifiers = systemuserCollection.map(systemuserItem => getSystemuserIdentifier(systemuserItem)!);
      const systemusersToAdd = systemusers.filter(systemuserItem => {
        const systemuserIdentifier = getSystemuserIdentifier(systemuserItem);
        if (systemuserIdentifier == null || systemuserCollectionIdentifiers.includes(systemuserIdentifier)) {
          return false;
        }
        systemuserCollectionIdentifiers.push(systemuserIdentifier);
        return true;
      });
      return [...systemusersToAdd, ...systemuserCollection];
    }
    return systemuserCollection;
  }

  protected convertDateFromClient(systemuser: ISystemuser): ISystemuser {
    return Object.assign({}, systemuser, {
      entryDate: systemuser.entryDate?.isValid() ? systemuser.entryDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.entryDate = res.body.entryDate ? dayjs(res.body.entryDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((systemuser: ISystemuser) => {
        systemuser.entryDate = systemuser.entryDate ? dayjs(systemuser.entryDate) : undefined;
      });
    }
    return res;
  }
}
