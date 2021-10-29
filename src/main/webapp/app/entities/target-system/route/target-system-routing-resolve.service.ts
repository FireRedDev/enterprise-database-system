import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITargetSystem, TargetSystem } from '../target-system.model';
import { TargetSystemService } from '../service/target-system.service';

@Injectable({ providedIn: 'root' })
export class TargetSystemRoutingResolveService implements Resolve<ITargetSystem> {
  constructor(protected service: TargetSystemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITargetSystem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((targetSystem: HttpResponse<TargetSystem>) => {
          if (targetSystem.body) {
            return of(targetSystem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TargetSystem());
  }
}
