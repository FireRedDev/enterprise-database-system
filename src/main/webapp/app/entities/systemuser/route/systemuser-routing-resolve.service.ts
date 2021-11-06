import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISystemuser, Systemuser } from '../systemuser.model';
import { SystemuserService } from '../service/systemuser.service';

@Injectable({ providedIn: 'root' })
export class SystemuserRoutingResolveService implements Resolve<ISystemuser> {
  constructor(protected service: SystemuserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISystemuser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((systemuser: HttpResponse<Systemuser>) => {
          if (systemuser.body) {
            return of(systemuser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Systemuser());
  }
}
