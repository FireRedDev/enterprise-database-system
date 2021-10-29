import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITargetSystemCredentials, TargetSystemCredentials } from '../target-system-credentials.model';
import { TargetSystemCredentialsService } from '../service/target-system-credentials.service';

@Injectable({ providedIn: 'root' })
export class TargetSystemCredentialsRoutingResolveService implements Resolve<ITargetSystemCredentials> {
  constructor(protected service: TargetSystemCredentialsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITargetSystemCredentials> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((targetSystemCredentials: HttpResponse<TargetSystemCredentials>) => {
          if (targetSystemCredentials.body) {
            return of(targetSystemCredentials.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TargetSystemCredentials());
  }
}
