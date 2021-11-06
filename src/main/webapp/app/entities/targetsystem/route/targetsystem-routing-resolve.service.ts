import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITargetsystem, Targetsystem } from '../targetsystem.model';
import { TargetsystemService } from '../service/targetsystem.service';

@Injectable({ providedIn: 'root' })
export class TargetsystemRoutingResolveService implements Resolve<ITargetsystem> {
  constructor(protected service: TargetsystemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITargetsystem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((targetsystem: HttpResponse<Targetsystem>) => {
          if (targetsystem.body) {
            return of(targetsystem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Targetsystem());
  }
}
