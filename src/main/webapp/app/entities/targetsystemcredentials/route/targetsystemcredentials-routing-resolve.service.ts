import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITargetsystemcredentials, Targetsystemcredentials } from '../targetsystemcredentials.model';
import { TargetsystemcredentialsService } from '../service/targetsystemcredentials.service';

@Injectable({ providedIn: 'root' })
export class TargetsystemcredentialsRoutingResolveService implements Resolve<ITargetsystemcredentials> {
  constructor(protected service: TargetsystemcredentialsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITargetsystemcredentials> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((targetsystemcredentials: HttpResponse<Targetsystemcredentials>) => {
          if (targetsystemcredentials.body) {
            return of(targetsystemcredentials.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Targetsystemcredentials());
  }
}
