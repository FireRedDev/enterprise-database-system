import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TargetSystemComponent } from '../list/target-system.component';
import { TargetSystemDetailComponent } from '../detail/target-system-detail.component';
import { TargetSystemUpdateComponent } from '../update/target-system-update.component';
import { TargetSystemRoutingResolveService } from './target-system-routing-resolve.service';

const targetSystemRoute: Routes = [
  {
    path: '',
    component: TargetSystemComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TargetSystemDetailComponent,
    resolve: {
      targetSystem: TargetSystemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TargetSystemUpdateComponent,
    resolve: {
      targetSystem: TargetSystemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TargetSystemUpdateComponent,
    resolve: {
      targetSystem: TargetSystemRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(targetSystemRoute)],
  exports: [RouterModule],
})
export class TargetSystemRoutingModule {}
