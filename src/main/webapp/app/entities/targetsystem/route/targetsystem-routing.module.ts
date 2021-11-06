import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TargetsystemComponent } from '../list/targetsystem.component';
import { TargetsystemDetailComponent } from '../detail/targetsystem-detail.component';
import { TargetsystemUpdateComponent } from '../update/targetsystem-update.component';
import { TargetsystemRoutingResolveService } from './targetsystem-routing-resolve.service';

const targetsystemRoute: Routes = [
  {
    path: '',
    component: TargetsystemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TargetsystemDetailComponent,
    resolve: {
      targetsystem: TargetsystemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TargetsystemUpdateComponent,
    resolve: {
      targetsystem: TargetsystemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TargetsystemUpdateComponent,
    resolve: {
      targetsystem: TargetsystemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(targetsystemRoute)],
  exports: [RouterModule],
})
export class TargetsystemRoutingModule {}
